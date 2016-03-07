package banque.compte;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import banque.client.Client;

import banque.client.AttacheClientele;
import banque.exception.OperationBancaireException;
import banque.exception.PersonnelNonAutoriseException;
import banque.operation.Operation;
import banque.operation.Statut;

/**
 * Classe qui gère les comptes bancaires d'un client 
 * @auteur yohan
 * 
 */
public class CompteBanquaire {

	/**
	 * numéro d'identification du compte bancaire
	 */
	private Long num;

	/**
	 * libellé du compte bancaire
	 */
	private String libelle;

	/**
	 * solde du compte bancaire, on initialise la variable à 0
	 */
	private BigDecimal solde = BigDecimal.ZERO;

	/**
	 * Client titulaire du compte
	 */
	private Client client;

	/**
	 * Liste des opérations associés au compte bancaire
	 */
	private List<Operation> historique = new ArrayList<>();

	/**
	 * Ce constructeur permet de créer une instance de CompteBanquaire en lui passant un numéro, un libellé et un client en paramètre
	 * @param num correspond au numéro du compte bancaire
	 * @param libelle correspond au libellé du compte 
	 * @param client correspond au client titulaire du compte bancaire
	 */
	public CompteBanquaire(Long num, String libelle, Client client) {
		this.num = num;
		this.libelle = libelle;
		this.client = client;
	}

	public boolean debiter(BigDecimal montant)
			throws OperationBancaireException {
		return this.debiter(montant, null);
	}

	/**
	 * Débite le compte de la somme rentré en paramètre. Si le débit de la somme est un échec, l'opération prend comme statut KO sinon OK
	 * Une exception est levée si après le montant débité, le solde est inférieur à 0
	 * @param montant correspond au montant à débiter du compte
	 * @param operation correspond à l'opération affecté par ce débit 
	 * @return retourne true si tout se passe bien sinon une des exception est levé
	 * @throws OperationBancaireException l'exception est levé si le montant est négatif ou nul
	 */
	public boolean debiter(BigDecimal montant, Operation operation)
			throws OperationBancaireException {

		if (montant.compareTo(BigDecimal.ZERO) < 0
				|| montant.compareTo(BigDecimal.ZERO) == 0) { // si le montant rentré est négatif

			if (operation != null) {
				// Opération échec est créée
				operation.setStatut(Statut.KO);
				// On ajoute alors l'opération dans notre historique avec le statut KO
				this.historique.add(operation);
			} // On lance alors l'exception lié à ce problème
			throw new OperationBancaireException(
					"Opération de débit rejetée, veuillez entrer un montant strictement positif");
		}

		if (montant.compareTo(solde) > 0) { // si le montant est supérieur au solde
			if (operation != null) {
				// Opération échec est créée
				operation.setStatut(Statut.KO);
				// On ajoute alors l'opération dans notre historique avec le statut KO
				this.historique.add(operation);
			} // On lance alors l'exception lié à ce problème 
			throw new OperationBancaireException(
					"Opération de débit rejetée, le montant de "
							+ montant
							+ " euros que vous avez entré est supérieur au solde = "
							+ this.solde + " euros");
		}

		if (operation != null) {
			// Nouveau solde
			this.solde = solde.subtract(montant);
			// Opération de succés est créée
			operation.setStatut(Statut.OK);
			// historisation de l'opération
			this.historique.add(operation);
			System.out.println("Opération de débit d'un montant de " + montant
					+ "euros efféctuée avec succés sur le  "
					+ this.getLibelle() + " portant le numéro : " + this.getNum()
					+ ", appartenant au client " + this.getClient().getNom()
					+ " portant le numéro " + this.getClient().getNum()
					+ ".\nNouveau solde = " + this.solde + " euros");
		}

		return true;
	}

	protected boolean crediter(BigDecimal montant)
			throws OperationBancaireException {
		return crediter(montant, null);
	}

	/**
	 * Débite le compte de la somme rentré en paramètre. Si le crédit de la somme est un échec, l'opération prend comme statut KO sinon OK
	 * Une exception est levée si après le montant débité, le solde est inférieur à 0
	 * @param montant correspond au montant à créditer du compte
	 * @param operation operation correspond à l'opération affecté par ce crédit  
	 * @return retourne true si tout se passe bien sinon une des exception est levé
	 * @throws OperationBancaireException l'exception est levé si le montant à créditer est négatif ou nul
	 */
	public boolean crediter(BigDecimal montant, Operation operation)
			throws OperationBancaireException {

		if (montant.compareTo(BigDecimal.ZERO) < 0
				|| montant.compareTo(BigDecimal.ZERO) == 0) {
			if (operation != null) {
				// Opération d'échec est créée
				operation.setStatut(Statut.KO);
				// historisation de l'opération
				this.historique.add(operation);
			} // On lance l'exception avec un message d'erreur qui avertit l'utilisateur du problème
			throw new OperationBancaireException(
					"Opération de crédit rejetée, veuillez entrer un montant strictement positif");
		}

		// S'il s'agit d'un compte plan épargne bloqué
		if (this instanceof PlanEpargneBloque) {
			if (montant.add(solde).compareTo(
					((PlanEpargneBloque) this).getSoldePlafond()) > 0) {// si le montant + solde dépasse le soldePlafond fixé
				if (operation != null) {
					// Opération échec est créée
					operation.setStatut(Statut.KO);
					// On ajoute alors l'opération dans notre historique avec le statut KO
					this.historique.add(operation);
				} // On lance l'exception avec un message d'erreur qui avertit l'utilisateur du problème
				throw new OperationBancaireException(
						"Opération de crédit rejetée, vous avez dépassé le solde plafond qui est de : "
								+ ((PlanEpargneBloque) this).getSoldePlafond());
			}
		}

		if (operation != null) {
			// Nouveau solde
			this.solde = this.solde.add(montant);
			// Opération de succés est créée
			operation.setStatut(Statut.OK);
			this.historique.add(operation);
			System.out.println("Opération de crédit d'un montant de " + montant
					+ " euros efféctuée avec succés sur le "
					+ this.getLibelle() + " portant le numéro " + this.getNum()
					+ ", appartenant au client " + this.getClient().getNom()
					+ " portant le numéro " + this.getClient().getNum()
					+ ".\nNouveau solde = " + this.solde + " euros");
		}

		return true;
	}

	/**
	 * @return retourne le solde du compte bancaire du client
	 */
	protected BigDecimal consultationSolde() {
		return solde;
	}

	/**
	 * @return retourne le libelle du compte bancaire
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle correspond au libelle du compte bancaire
	 *  permet de changer le libellé du compte bancaire
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return retourne le solde
	 */
	public BigDecimal getSolde() {
		return solde;
	}

	/**
	 * @param solde
	 * permet de modifier le solde du compte lié au client
	 */
	public void setSolde(BigDecimal solde) {
		this.solde = solde;
	}

	/**
	 * @return retourne le titulaire du compte
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 * permet de changer le titulaire du compte bancaire
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return retourne l'historique du compte
	 * permet d'avoir accès aux opérations liés aux comptes bancaires
	 */
	public List<Operation> getHistorique() {
		return historique;
	}

	/**
	 * @param historique
	 *            the historique to set
	 */
	public void setHistorique(List<Operation> historique) {
		this.historique = historique;
	}

	/**
	 * méthode qui permet de forcer le credit d'un compte par l'intermédiaire de l'attaché du client.
	 * lève une exception si l’attaché de clientèle qui tente l’opération n’est pas autorisé.
	 * @param montant correspond au montant à crediter 
	 * @param op correspond à l'opération en cours (statut de l'operation)
	 * @param lAttacheAutorise correspond à l'attaché lié au client
	 * @return retourne le nouveau solde 
	 * @throws PersonnelNonAutoriseException exception qui est levé si ce n'est pas l'attaché associé au client qui tente de forcer l'opération
	 */
	public BigDecimal forcerCredit(BigDecimal montant, Operation op,
			AttacheClientele lAttacheAutorise)
			throws PersonnelNonAutoriseException {

		if (!lAttacheAutorise.equals(this.getClient().getAttache())) {
			throw new PersonnelNonAutoriseException(
					"L'attaché de clientéle de ce cllient "
							+ this.getClient().getNom()
							+ " portant le numéro : "
							+ this.getClient().getNum()
							+ " n'est pas autorisé à forcer le crédit de ce virement");
		}

		this.solde = this.solde.add(montant);

		return this.solde;
	}

	/**
	 * méthode qui permet de forcer le débit d'un compte par l'intermédiaire de l'attaché du client.
	 * lève une exception si l’attaché de clientèle qui tente l’opération n’est pas autorisé.
	 * @param montant correspond au montant à débiter 
	 * @param op correspond à l'opération en cours (statut de l'operation)
	 * @param lAttacheAutorise correspond à l'attaché lié au client
	 * @return retourne le nouveau solde 
	 * @throws PersonnelNonAutoriseException exception qui est levé si ce n'est pas l'attaché associé au client qui tente de forcer l'opération
	 */
	public BigDecimal forcerDebit(BigDecimal montant, Operation op,
			AttacheClientele lAttacheAutorise)
			throws PersonnelNonAutoriseException {

		if (!lAttacheAutorise.equals(this.getClient().getAttache())) {
			throw new PersonnelNonAutoriseException(
					"L'attaché de clientéle de ce cllient "
							+ this.getClient().getNom()
							+ " portant le numéro : "
							+ this.getClient().getNum()
							+ " n'est pas autorisé à forcer le débit de ce virement");
		}

		// Nouveau solde suite au débit
		this.solde = this.solde.subtract(montant);

		return this.solde;
	}

	@Override
	public String toString() {
		return "Compte banquaire [id = " + getNum() + ", libelle = " + libelle
				+ ", solde = " + solde + ", client = " + client
				+ ", historique = " + historique + "]";
	}

	/**
	 * @return retourne le numéro du compte bancaire 
	 */
	public Long getNum() {
		return num;
	}

	/**
	 * @param num
	 * permet de changer le numéro de compte bancaire
	 */
	public void setNum(Long num) {
		this.num = num;
	}

}
