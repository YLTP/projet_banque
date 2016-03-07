package banque.operation;

import java.util.Date;

/**
 * Classe qui g�re toutes les op�rations sur des comptes 
 * @auteur yohan
 *
 */
public class Operation {

	/**
	 * num�ro de l'op�ration
	 */
	private Long num;
	/**
	 * libell� de l'op�ration 
	 */
	private String libelle;

	/**
	 * statut de l'op�ration (type �num�r�)
	 */
	private Statut statut;

	/**
	 * date de prise � effet de l'op�ration 
	 */
	private Date datePriseEffet;

	/**
	 * Constructeur qui cr�e une op�ration avec un num�ro et un libell� 
	 * @param num correspond au num�ro de l'op�ration 
 	 * @param libelle correspond au libell� de l'op�ration 
	 */
	public Operation(Long num, String libelle) {
		this.num = num;
		this.libelle = libelle;
	}

	/**
	 * @return retourne le num�ro de l'op�ration 
	 */
	public Long getNum() {
		return num;
	}

	/**
	 * @param num
	 * permet de modifier le num�ro de l'op�ration par le nouveau num rentr� en param�tre 
	 */
	public void setNum(Long num) {
		this.num = num;
	}

	/**
	 * @return retourne le libell� de l'op�ration 
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle
	 * permet de modifier le libell� de l'op�ration par le nouveau libell� rentr� en param�tre 
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return retourne le statut de l'op�ration
	 */
	public Statut getStatut() {
		return statut;
	}

	/**
	 * @param statut
	 * permet de changer le statut de l'op�ration par le statut rentr� en param�tre 
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
	 * permet de changer la date de l'op�ration 
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
