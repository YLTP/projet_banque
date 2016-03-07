package banque.operation;

import java.util.Date;

/**
 * Classe qui gère toutes les opérations sur des comptes 
 * @auteur yohan
 *
 */
public class Operation {

	/**
	 * numéro de l'opération
	 */
	private Long num;
	/**
	 * libellé de l'opération 
	 */
	private String libelle;

	/**
	 * statut de l'opération (type énuméré)
	 */
	private Statut statut;

	/**
	 * date de prise à effet de l'opération 
	 */
	private Date datePriseEffet;

	/**
	 * Constructeur qui crée une opération avec un numéro et un libellé 
	 * @param num correspond au numéro de l'opération 
 	 * @param libelle correspond au libellé de l'opération 
	 */
	public Operation(Long num, String libelle) {
		this.num = num;
		this.libelle = libelle;
	}

	/**
	 * @return retourne le numéro de l'opération 
	 */
	public Long getNum() {
		return num;
	}

	/**
	 * @param num
	 * permet de modifier le numéro de l'opération par le nouveau num rentré en paramètre 
	 */
	public void setNum(Long num) {
		this.num = num;
	}

	/**
	 * @return retourne le libellé de l'opération 
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle
	 * permet de modifier le libellé de l'opération par le nouveau libellé rentré en paramètre 
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return retourne le statut de l'opération
	 */
	public Statut getStatut() {
		return statut;
	}

	/**
	 * @param statut
	 * permet de changer le statut de l'opération par le statut rentré en paramètre 
	 */
	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	/**
	 * @return retourne la date de prise en effet 
	 */
	public Date getDatePriseEffet() {
		return datePriseEffet;
	}

	/**
	 * @param datePriseEffet
	 * permet de changer la date de l'opération 
	 */
	public void setDatePriseEffet(Date datePriseEffet) {
		this.datePriseEffet = datePriseEffet;
	}

	@Override
	public String toString() {
		return "Operation [num = " + num + ", libelle = " + libelle
				+ ", statut = " + statut + ", date de prise d'effet = "
				+ datePriseEffet + "]";
	}

}
