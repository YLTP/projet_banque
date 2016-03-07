package banque.client;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import banque.compte.CompteBanquaire;
import banque.exception.OperationBancaireException;
import banque.exception.PersonnelNonAutoriseException;
import banque.operation.Operation;
import banque.operation.Statut;

/**
 * @author yohan_000
 * 
 */
public class Client {

	private Long num;
	private String nom;
	private Long numClientSuivant;
	private AttacheClientele attache;

	private Set<CompteBanquaire> ComptesBanquaire = new HashSet<CompteBanquaire>();

	/**
	 * Ce constructeur permet de créer une instance de Client en lui passant un numéro et un nom en paramètre
	 * @param num correspond au numéro du client
	 * @param nom correspond au nom du client
	 */
	public Client(Long num, String nom) {
		this.num = num;
		this.nom = nom;
	}

	/**
	 * @return retourne le numero du client
	 */
	public Long getNum() {
		return num;
	}

	/**
	 * permet de changer le numéro du client
	 * @param num correspond au nouveau numéro du client
	 */
	public void setNum(Long num) {
		this.num = num;
	}

	/**
	 * @return retourne le nom du client
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom correspond au nouveau nom du client
	 * permet de changer le nom du client
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return retourne le numéro pour un client suivant 
	 */
	public Long getNumClientSuivant() {
		return numClientSuivant;
	}


	/**
	 * @return retourne la liste des comptes bancaires associés au client
	 * permet de donner un accès à la liste des comptes du client
	 */
	public Set<CompteBanquaire> getComptesBanquaire() {
		return ComptesBanquaire;
	}

	/**
	 * @param comptesBanquaire correspond au nouveau compte bancaire du client
	 * permet de changer la liste des comptes du client
	 */
	public void setComptesBanquaire(Set<CompteBanquaire> comptesBanquaire) {
		ComptesBanquaire = comptesBanquaire;
	}

	/**
	 * @return retourne l'attaché affilé au client
	 */
	public AttacheClientele getAttache() {
		return attache;
	}

	/**
	 * @param attache correspond au nouvel attaché du client
	 * permet de changer l'attaché du client
	 */
	public void setAttache(AttacheClientele attache) {
		this.attache = attache;
	}

	/**
	 * méthode qui teste dans un premier temps si les comptes origine et destinataire sont contenues dans la liste ComptesBanquaires
	 * elle teste si le compte origine peut être débiter, si cela est impossible une exeption est levé
	 * Si le débit est possible, alors une tentative de crédit est effectuée, avec création d’opération
	 * Si la tentative échoue, alors on envoie la suite de l'orde de virement a effectué à la méthode envoyeAttacheClientele
	 * @param origine correspond au compte sur lequel on va effectué la première opération
	 * @param destinataire correspond au compte sur lequel on va effectué la seconde opération
	 * @param montant correspond au montant à transférer d'un compte à un autre 
	 * @throws OperationBancaireException l'exception est levé si le montant est négatif ou nul
	 * @throws PersonnelNonAutoriseException l'exception est levé si ce n'est pas l'attaché associé au client qui effectue l'ordre de virement
	 */
	public void passerOrdreVirement(CompteBanquaire origine,
			CompteBanquaire destinataire, BigDecimal montant)
			throws OperationBancaireException, PersonnelNonAutoriseException {

		if (!this.getComptesBanquaire().contains(origine)) { // si la liste de comptes bancaires ne contient pas le compte d'origine
			throw new OperationBancaireException( // on lève l'exception
					"Opération de virement rejetée. le " + origine.getLibelle()
							+ " portant le numéro " + origine.getNum()
							+ ", n'appartient pas au client " + this.getNom()
							+ " portant le numéro " + this.getNum());
		} else if (!this.getComptesBanquaire().contains(destinataire)) { // si la liste de comptes bancaires ne contient pas le compte destinataire
			throw new OperationBancaireException( // on lève l'exception
					"Opération de virement rejetée. le "
							+ destinataire.getLibelle() + " portant le numéro "
							+ destinataire.getNum()
							+ ", n'appartient pas au client " + this.getNom()
							+ " portant le numéro " + this.getNum());
		}

		boolean isDebitPossible = false;

		try {
			isDebitPossible = origine.debiter(montant); // on essaye de débiter le compte origine 
		} catch (OperationBancaireException e) { // si impossible :
			System.err.print("Virement impossible"); // message d'erreur 
			e.printStackTrace();
			return;
		}

		if (isDebitPossible) { // si on a pu correctement débiter le comtpe origine 
			Operation operationCreditDestinataire = new Operation(
					destinataire.getNum() + destinataire.getClient().getNum(),
					"credit");
			Operation operationDebitOrigine = new Operation(origine.getNum()
					+ origine.getClient().getNum(), "debit");
			try {
				origine.debiter(montant, operationDebitOrigine); //  on débite le compte origine
				destinataire.crediter(montant, operationCreditDestinataire); // on crédite le compte destinataire
				
			} catch (OperationBancaireException e) { // si le débit est possible mais que le crédit pose problème on envoyé la suite des opérations à la méthode envoyeAttacheClient qui va les traiter
				this.envoyeAttacheClientele(origine, destinataire, montant,
						operationCreditDestinataire, operationDebitOrigine);
			}
		}
	}

	private void envoyeAttacheClientele(CompteBanquaire origine,
			CompteBanquaire destinataire, BigDecimal montant,
			Operation operationCreditDestinataire,
			Operation operationDebitOrigine)
			throws PersonnelNonAutoriseException {

		System.out
				.println("Opérations envoyées à l'attaché de clientéle du client "
						+ origine.getClient().getNom()
						+ " portant le numéro "
						+ origine.getClient().getNum());
		operationCreditDestinataire.setStatut(Statut.ATTENTE);
		operationDebitOrigine.setStatut(Statut.ATTENTE);
		BigDecimal solde = origine.forcerDebit(montant, operationDebitOrigine,
				this.getAttache());
		origine.setSolde(solde);
		solde = destinataire.forcerCredit(montant, operationCreditDestinataire,
				this.getAttache());
		destinataire.setSolde(solde);
	}

	
	public String toString() {
		return "Client [num = " + num + ", nom = " + nom
				+ ", comptes Banquaire = " + ComptesBanquaire + "]";
	}

}
