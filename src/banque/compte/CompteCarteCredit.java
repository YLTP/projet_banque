package banque.compte;

import banque.client.Client;

/**
 * Classe qui gère les comptes carte credit d'un client 
 * @author yohan
 *
 */
public class CompteCarteCredit extends CompteBanquaire {

	/**
	 * Constructeur qui créée un compte carte de credit avec un numéro, un libellé et un client titulaire 
	 * @param num
	 * @param libelle
	 * @param client
	 */
	public CompteCarteCredit(Long num, String libelle, Client client) {
		super(num, libelle, client);
	}
	
}
