package banque.compte;

import banque.client.Client;

/**
 * Classe qui gère les comptes courant d'un client 
 * @auteur yohan 
 *
 */
public class CompteCourant extends CompteBanquaire {

	public CompteCourant(Long num, String libelle, Client client) {
		super(num, libelle, client);
	}

}
