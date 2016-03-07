package banque;

import java.math.BigDecimal;
import java.math.RoundingMode;

import banque.client.Client;

import banque.client.AttacheClientele;
import banque.compte.CompteBanquaire;
import banque.compte.CompteCourant;
import banque.compte.PlanEpargneBloque;
import banque.exception.OperationBancaireException;
import banque.exception.PersonnelNonAutoriseException;
import banque.operation.Operation;

public class Banque {

	public static void main(String[] args) {

		// 1er jeu de tests
		Client Yohan = new Client(1L, "Yohan");
		Client Thomas = new Client(2L, "Thomas");

		CompteBanquaire compteCourant = new CompteCourant(1L, "compte courant",
				Yohan);
		CompteBanquaire comptePlanEpargneBloque = new PlanEpargneBloque(2L,
				"compte plan epargne bloque", Yohan, new BigDecimal(700));

		Yohan.getComptesBanquaire().add(compteCourant);
		Yohan.getComptesBanquaire().add(comptePlanEpargneBloque);

		AttacheClientele attache1 = new AttacheClientele();
		AttacheClientele attache2 = new AttacheClientele();

		Yohan.setAttache(attache1);
		Thomas.setAttache(attache2);

		System.out.println("************* Premier jeu de tests *************");
		System.out.println("Création du client " + Yohan.getNom()
				+ " portant le numéro : " + Yohan.getNum()
				+ ", possédant les comptes suivants : "
				+ compteCourant.getLibelle() + " portant le numéro "
				+ compteCourant.getNum() + ", "
				+ comptePlanEpargneBloque.getLibelle()
				+ " portant le numéro : " + comptePlanEpargneBloque.getNum());

		// 2e jeu de tests
		try {

			System.out
					.println("************* Deuxième jeu de tests *************");
			Operation operationDebitCptCourant = new Operation(
					compteCourant.getNum() + compteCourant.getClient().getNum(),
					"debit");
			Operation operationCreditCptCourant = new Operation(
					comptePlanEpargneBloque.getNum()
							+ compteCourant.getClient().getNum(), "credit");
			Operation operationCreditCptPlanEpargneBloque = new Operation(
					compteCourant.getNum() + compteCourant.getClient().getNum(),
					"credit");
			compteCourant.crediter(new BigDecimal(2000),
					operationCreditCptCourant);
			comptePlanEpargneBloque.crediter(new BigDecimal(33),
					operationCreditCptPlanEpargneBloque);
			compteCourant
					.debiter(new BigDecimal(500), operationDebitCptCourant);
			compteCourant.debiter(new BigDecimal(2500),
					operationDebitCptCourant);
		} catch (OperationBancaireException e) {
			e.printStackTrace();
		}

		// 3e jeu de tests
		try {
			System.out
					.println("************* Troisième jeu de tests *************");
			Operation operationDebitCptCourant = new Operation(
					compteCourant.getNum() + compteCourant.getClient().getNum(),
					"debit");
			Operation operationCreditCptCourant = new Operation(
					comptePlanEpargneBloque.getNum()
							+ compteCourant.getClient().getNum(), "credit");
			compteCourant
					.debiter(new BigDecimal(123), operationDebitCptCourant);
			compteCourant.crediter(
					new BigDecimal(1.55).setScale(2, RoundingMode.HALF_DOWN),
					operationCreditCptCourant);

			Yohan.passerOrdreVirement(compteCourant, comptePlanEpargneBloque,
					new BigDecimal(666));
			Yohan.passerOrdreVirement(compteCourant, comptePlanEpargneBloque,
					new BigDecimal(100000));
			Yohan.passerOrdreVirement(compteCourant, comptePlanEpargneBloque,
					new BigDecimal(10));
		} catch (OperationBancaireException e) {
			e.printStackTrace();
		} catch (PersonnelNonAutoriseException e) {
			e.printStackTrace();
		}

	}
}
