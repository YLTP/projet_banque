package banque.client;

import java.util.Set;

/**
 * Classe qui gére un ou plusieurs clients
 * 
 * @auteur yohan
 * 
 */
public class AttacheClientele {

	/**
	 * L'ensemble des clients gérés par cette classe
	 */
	private Set<Client> clients;

	/**
	 * @return retourne l'ensemble des clients
	 */
	public Set<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 * permet de changer les clients de la liste
	 */
	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

	@Override
	public String toString() {
		return "Attaché de clientèle [clients = " + clients + "]";
	}

}
