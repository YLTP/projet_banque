package banque.operation;

/**
 * classe énumération permettant de controler le statut d'une opération 
 * @auteur yohan
 *
 */
public enum Statut {

	OK, // succès
	KO, // échec, pour opération rejetée,
	ATTENTE // en attente de validation
	
}
