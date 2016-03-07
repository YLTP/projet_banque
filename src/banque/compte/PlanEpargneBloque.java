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
	 * permet de créer un compte plan épargne bloqué avec un solde plafond 
	 * @param num correspond au numéro du compte
	 * @param libelle correspond au libellé du compte 
	 * @param client correspond du client titulaire 
	 * @param soldePlafond correspond au solde plafond du compte plan épargne bloqué 
	 */
	public PlanEpargneBloque(Long num, String libelle, Client client,
			BigDecimal soldePlafond) {
		super(num, libelle, client, soldePlafond);
	}

	/**
	 * date de mise à disposition des fonds du compte avant laquelle aucune opération n'est acceptée
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
	 * permet de changer la date de mise à disposition des fonds du compte  
	 */
	public void setDateMiseADispositionFonds(Date dateMiseADispositionFonds) {
		this.dateMiseADispositionFonds = dateMiseADispositionFonds;
	}

	@Override
	public String toString() {
		return "Plan epargne bloqué [date de mise à disposition des fonds = "
				+ dateMiseADispositionFonds + "]";
	}

}
