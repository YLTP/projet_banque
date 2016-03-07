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
	 * Ce constructeur permet de cr�er une instance de Client en lui passant un num�ro et un nom en param�tre
	 * @param num correspond au num�ro du client
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
	 * permet de changer le num�ro du client
	 * @param num correspond au nouveau num�ro du client
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
	 * @return retourne le num�ro pour un client suivant 
	 */
	public Long getNumClientSuivant() {
		return numClientSuivant;
	}


	/**
	 * @return retourne la liste des comptes bancaires associ�s au client
	 * permet de donner un acc�s � la liste des comptes du client
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
	 * @return retourne l'attach� affil� au client
	 */
	public AttacheClientele getAttache() {
		return attache;
	}

	/**
	 * @param attache correspond au nouvel attach� du client
	 * permet de changer l'attach� du client
	 */
	public void setAttache(AttacheClientele attache) {
		this.attache = attache;
	}

	/**
	 * m�thode qui teste dans un premier temps si les comptes origine et destinataire sont contenues dans la liste ComptesBanquaires
	 * elle teste si le compte origine peut �tre d�biter, si cela est impossible une exeption est lev�
	 * Si le d�bit est possible, alors une tentative de cr�dit est effectu�e, avec cr�ation d�op�ration
	 * Si la tentative �choue, alors on envoie la suite de l'orde de virement a effectu� � la m�thode envoyeAttacheClientele
	 * @param origine correspond au compte sur lequel on va effectu� la premi�re op�ration
	 * @param destinataire correspond au compte sur lequel on va effectu� la seconde op�ration
	 * @param montant correspond au montant � transf�rer d'un compte � un autre 
	 * @throws OperationBancaireException l'exception est lev� si le montant est n�gatif ou nul
	 * @throws PersonnelNonAutoriseException l'exception est lev� si ce n'est pas l'attach� associ� au client qui effectue l'ordre de virement
	 */
	public void passerOrdreVirement(CompteBanquaire origine,
			CompteBanquaire destinataire, BigDecimal montant)
			throws OperationBancaireException, PersonnelNonAutoriseException {

		if (!this.getComptesBanquaire().contains(origine)) { // si la liste de comptes bancaires ne contient pas le compte d'origine
			throw new OperationBancaireException( // on l�ve l'exception
					"Op�ration de virement rejet�e. le " + origine.getLibelle()
							+ " portant le num�ro " + origine.getNum()
							+ ", n'appartient pas au client " + this.getNom()
							+ " portant le num�ro " + this.getNum());
		} else if (!this.getComptesBanquaire().contains(destinataire)) { // si la liste de comptes bancaires ne contient pas le compte destinataire
			throw new OperationBancaireException( // on l�ve l'exception
					"Op�ration de virement rejet�e. le "
							+ destinataire.getLibelle() + " portant le num�ro "
							+ destinataire.getNum()
							+ ", n'appartient pas au client " + this.getNom()
							+ " portant le num�ro " + this.getNum());
		}

		boolean isDebitPossible = false;

		try {
			isDebitPossible = origine.debiter(montant); // on essaye de d�biter le compte origine 
		} catch (OperationBancaireException e) { // si impossible :
			System.err.print("Virement impossible"); // message d'erreur 
			e.printStackTrace();
			return;
		}

		if (isDebitPossible) { // si on a pu correctement d�biter le comtpe origine 
			Operation operationCreditDestinataire = new Operation(
					destinataire.getNum() + destinataire.getClient().getNum(),
					"credit");
			Operation operationDebitOrigine = new Operation(origine.getNum()
					+ origine.getClient().getNum(), "debit");
			try {
				origine.debiter(montant, operationDebitOrigine); //  on d�bite le compte origine
				destinataire.crediter(montant, operationCreditDestinataire); // on cr�dite le compte destinataire
				
			} catch (OperationBancaireException e) { // si le d�bit est possible mais que le cr�dit pose probl�me on envoy� la suite des op�rations � la m�thode envoyeAttacheClient qui va les traiter
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
				.println("Op�rations envoy�es � l'attach� de client�le du client "
						+ origine.getClient().getNom()
						+ " portant le num�ro "
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
