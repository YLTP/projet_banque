package banque.compte;

import java.math.BigDecimal;
import java.util.Date;

import banque.client.Client;

/**
 * @auteur yohan 
 * 
 */
public class PlanEpargneBloque extends CompteRemunere {

	
	/**
	 * permet de cr�er un compte plan �pargne bloqu� avec un solde plafond 
	 * @param num correspond au num�ro du compte
	 * @param libelle correspond au libell� du compte 
	 * @param client correspond du client titulaire 
	 * @param soldePlafond correspond au solde plafond du compte plan �pargne bloqu� 
	 */
	public PlanEpargneBloque(Long num, String libelle, Client client,
			BigDecimal soldePlafond) {
		super(num, libelle, client, soldePlafond);
	}

	/**
	 * date de mise � disposition des fonds du compte avant laquelle aucune op�ration n'est accept�e
	 */
	private Date dateMiseADispositionFonds;

	/**
	 * @return the dateMiseADispositionFonds
	 */
	public Date getDateMiseADispositionFonds() {
		return dateMiseADispositionFonds;
	}

	/**
	 * @param dateMiseADispositionFonds
	 * permet de changer la date de mise � disposition des fonds du compte  
	 */
	public void setDateMiseADispositionFonds(Date dateMiseADispositionFonds) {
		this.dateMiseADispositionFonds = dateMiseADispositionFonds;
	}

	@Override
	public String toString() {
		return "Plan epargne bloqu� [date de mise � disposition des fonds = "
				+ dateMiseADispositionFonds + "]";
	}

}
