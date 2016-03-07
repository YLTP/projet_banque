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
 * Classe qui g�re les comptes bancaires d'un client 
 * @auteur yohan
 * 
 */
public class CompteBanquaire {

	/**
	 * num�ro d'identification du compte bancaire
	 */
	private Long num;

	/**
	 * libell� du compte bancaire
	 */
	private String libelle;

	/**
	 * solde du compte bancaire, on initialise la variable � 0
	 */
	private BigDecimal solde = BigDecimal.ZERO;

	/**
	 * Client titulaire du compte
	 */
	private Client client;

	/**
	 * Liste des op�rations associ�s au compte bancaire
	 */
	private List<Operation> historique = new ArrayList<>();

	/**
	 * Ce constructeur permet de cr�er une instance de CompteBanquaire en lui passant un num�ro, un libell� et un client en param�tre
	 * @param num correspond au num�ro du compte bancaire
	 * @param libelle correspond au libell� du compte 
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
	 * D�bite le compte de la somme rentr� en param�tre. Si le d�bit de la somme est un �chec, l'op�ration prend comme statut KO sinon OK
	 * Une exception est lev�e si apr�s le montant d�bit�, le solde est inf�rieur � 0
	 * @param montant correspond au montant � d�biter du compte
	 * @param operation correspond � l'op�ration affect� par ce d�bit 
	 * @return retourne true si tout se passe bien sinon une des exception est lev�
	 * @throws OperationBancaireException l'exception est lev� si le montant est n�gatif ou nul
	 */
	public boolean debiter(BigDecimal montant, Operation operation)
			throws OperationBancaireException {

		if (montant.compareTo(BigDecimal.ZERO) < 0
				|| montant.compareTo(BigDecimal.ZERO) == 0) { // si le montant rentr� est n�gatif

			if (operation != null) {
				// Op�ration �chec est cr��e
				operation.setStatut(Statut.KO);
				// On ajoute alors l'op�ration dans notre historique avec le statut KO
				this.historique.add(operation);
			} // On lance alors l'exception li� � ce probl�me
			throw new OperationBancaireException(
					"Op�ration de d�bit rejet�e, veuillez entrer un montant strictement positif");
		}

		if (montant.compareTo(solde) > 0) { // si le montant est sup�rieur au solde
			if (operation != null) {
				// Op�ration �chec est cr��e
				operation.setStatut(Statut.KO);
				// On ajoute alors l'op�ration dans notre historique avec le statut KO
				this.historique.add(operation);
			} // On lance alors l'exception li� � ce probl�me 
			throw new OperationBancaireException(
					"Op�ration de d�bit rejet�e, le montant de "
							+ montant
							+ " euros que vous avez entr� est sup�rieur au solde = "
							+ this.solde + " euros");
		}

		if (operation != null) {
			// Nouveau solde
			this.solde = solde.subtract(montant);
			// Op�ration de succ�s est cr��e
			operation.setStatut(Statut.OK);
			// historisation de l'op�ration
			this.historique.add(operation);
			System.out.println("Op�ration de d�bit d'un montant de " + montant
					+ "euros eff�ctu�e avec succ�s sur le  "
					+ this.getLibelle() + " portant le num�ro : " + this.getNum()
					+ ", appartenant au client " + this.getClient().getNom()
					+ " portant le num�ro " + this.getClient().getNum()
					+ ".\nNouveau solde = " + this.solde + " euros");
		}

		return true;
	}

	protected boolean crediter(BigDecimal montant)
			throws OperationBancaireException {
		return crediter(montant, null);
	}

	/**
	 * D�bite le compte de la somme rentr� en param�tre. Si le cr�dit de la somme est un �chec, l'op�ration prend comme statut KO sinon OK
	 * Une exception est lev�e si apr�s le montant d�bit�, le solde est inf�rieur � 0
	 * @param montant correspond au montant � cr�diter du compte
	 * @param operation operation correspond � l'op�ration affect� par ce cr�dit  
	 * @return retourne true si tout se passe bien sinon une des exception est lev�
	 * @throws OperationBancaireException l'exception est lev� si le montant � cr�diter est n�gatif ou nul
	 */
	public boolean crediter(BigDecimal montant, Operation operation)
			throws OperationBancaireException {

		if (montant.compareTo(BigDecimal.ZERO) < 0
				|| montant.compareTo(BigDecimal.ZERO) == 0) {
			if (operation != null) {
				// Op�ration d'�chec est cr��e
				operation.setStatut(Statut.KO);
				// historisation de l'op�ration
				this.historique.add(operation);
			} // On lance l'exception avec un message d'erreur qui avertit l'utilisateur du probl�me
			throw new OperationBancaireException(
					"Op�ration de cr�dit rejet�e, veuillez entrer un montant strictement positif");
		}

		// S'il s'agit d'un compte plan �pargne bloqu�
		if (this instanceof PlanEpargneBloque) {
			if (montant.add(solde).compareTo(
					((PlanEpargneBloque) this).getSoldePlafond()) > 0) {// si le montant + solde d�passe le soldePlafond fix�
				if (operation != null) {
					// Op�ration �chec est cr��e
					operation.setStatut(Statut.KO);
					// On ajoute alors l'op�ration dans notre historique avec le statut KO
					this.historique.add(operation);
				} // On lance l'exception avec un message d'erreur qui avertit l'utilisateur du probl�me
				throw new OperationBancaireException(
						"Op�ration de cr�dit rejet�e, vous avez d�pass� le solde plafond qui est de : "
								+ ((PlanEpargneBloque) this).getSoldePlafond());
			}
		}

		if (operation != null) {
			// Nouveau solde
			this.solde = this.solde.add(montant);
			// Op�ration de succ�s est cr��e
			operation.setStatut(Statut.OK);
			this.historique.add(operation);
			System.out.println("Op�ration de cr�dit d'un montant de " + montant
					+ " euros eff�ctu�e avec succ�s sur le "
					+ this.getLibelle() + " portant le num�ro " + this.getNum()
					+ ", appartenant au client " + this.getClient().getNom()
					+ " portant le num�ro " + this.getClient().getNum()
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
	 *  permet de changer le libell� du compte bancaire
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
	 * permet de modifier le solde du compte li� au client
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
	 * permet d'avoir acc�s aux op�rations li�s aux comptes bancaires
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
	 * m�thode qui permet de forcer le credit d'un compte par l'interm�diaire de l'attach� du client.
	 * l�ve une exception si l�attach� de client�le qui tente l�op�ration n�est pas autoris�.
	 * @param montant correspond au montant � crediter 
	 * @param op correspond � l'op�ration en cours (statut de l'operation)
	 * @param lAttacheAutorise correspond � l'attach� li� au client
	 * @return retourne le nouveau solde 
	 * @throws PersonnelNonAutoriseException exception qui est lev� si ce n'est pas l'attach� associ� au client qui tente de forcer l'op�ration
	 */
	public BigDecimal forcerCredit(BigDecimal montant, Operation op,
			AttacheClientele lAttacheAutorise)
			throws PersonnelNonAutoriseException {

		if (!lAttacheAutorise.equals(this.getClient().getAttache())) {
			throw new PersonnelNonAutoriseException(
					"L'attach� de client�le de ce cllient "
							+ this.getClient().getNom()
							+ " portant le num�ro : "
							+ this.getClient().getNum()
							+ " n'est pas autoris� � forcer le cr�dit de ce virement");
		}

		this.solde = this.solde.add(montant);

		return this.solde;
	}

	/**
	 * m�thode qui permet de forcer le d�bit d'un compte par l'interm�diaire de l'attach� du client.
	 * l�ve une exception si l�attach� de client�le qui tente l�op�ration n�est pas autoris�.
	 * @param montant correspond au montant � d�biter 
	 * @param op correspond � l'op�ration en cours (statut de l'operation)
	 * @param lAttacheAutorise correspond � l'attach� li� au client
	 * @return retourne le nouveau solde 
	 * @throws PersonnelNonAutoriseException exception qui est lev� si ce n'est pas l'attach� associ� au client qui tente de forcer l'op�ration
	 */
	public BigDecimal forcerDebit(BigDecimal montant, Operation op,
			AttacheClientele lAttacheAutorise)
			throws PersonnelNonAutoriseException {

		if (!lAttacheAutorise.equals(this.getClient().getAttache())) {
			throw new PersonnelNonAutoriseException(
					"L'attach� de client�le de ce cllient "
							+ this.getClient().getNom()
							+ " portant le num�ro : "
							+ this.getClient().getNum()
							+ " n'est pas autoris� � forcer le d�bit de ce virement");
		}

		// Nouveau solde suite au d�bit
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
	 * @return retourne le num�ro du compte bancaire 
	 */
	public Long getNum() {
		return num;
	}

	/**
	 * @param num
	 * permet de changer le num�ro de compte bancaire
	 */
	public void setNum(Long num) {
		this.num = num;
	}

}
