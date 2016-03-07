package banque.compte;

import java.math.BigDecimal;

import banque.client.Client;

/**
 * 
 * @auteur yohan
 * 
 */
public class CompteRemunere extends CompteBanquaire {

	/**
	 * Taux de rémunération du compte
	 */
	private BigDecimal tauxRemuneration;

	/**
	 * solde plafond du compte
	 */
	private BigDecimal soldePlafond;

	/**
	 *  permet de créer un compte remunéré avec un solde plafond, un client titulaire, un numéro et un libellé 
	 * @param num correspond au numéro du compte rémunéré 
	 * @param libelle correspond au libellé du compte rémunéré
	 * @param client correspond au client titulaire 
	 * @param soldePlafond correspond au solde plafond du compte 
	 */
	public CompteRemunere(Long num, String libelle, Client client,
			BigDecimal soldePlafond) {
		super(num, libelle, client);
		this.soldePlafond = soldePlafond;
	}

	/**
	 * @return retourne le tauxRemuneration
	 */
	public BigDecimal getTauxRemuneration() {
		return tauxRemuneration;
	}

	/**
	 * @param tauxRemuneration nouveau taux de rémuneration
	 * permet de modifier le taux de remuneration du compte
	 */
	public void setTauxRemuneration(BigDecimal tauxRemuneration) {
		this.tauxRemuneration = tauxRemuneration;
	}

	/**
	 * @return retourne le solde plafond du compte
	 */
	public BigDecimal getSoldePlafond() {
		return soldePlafond;
	}

	/**
	 * @param soldePlafond correspond au nouveau solde plafond
	 * permet de modifier le solde plafond du compte
	 */
	public void setSoldePlafond(BigDecimal soldePlafond) {
		this.soldePlafond = soldePlafond;
	}

	@Override
	public String toString() {
		return "Compte rémunéré [taux de rémunération = " + tauxRemuneration
				+ ", solde plafond = " + soldePlafond + "]";
	}

}
