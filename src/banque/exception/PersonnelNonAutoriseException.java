package banque.exception;

/**
 * Classe qui gère l'exception lié à un attaché 
 * qui effecturai une opération de débit ou de crédit qui ne correspond à l'attaché associé au client titulaire du compte
 * @author yohan
 *
 */
public class PersonnelNonAutoriseException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersonnelNonAutoriseException() {
		super();
	}

	public PersonnelNonAutoriseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PersonnelNonAutoriseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersonnelNonAutoriseException(String message) {
		super(message);
	}

	public PersonnelNonAutoriseException(Throwable cause) {
		super(cause);
	}

}
