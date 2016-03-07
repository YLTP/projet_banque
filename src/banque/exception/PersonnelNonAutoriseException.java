package banque.exception;

/**
 * Classe qui g�re l'exception li� � un attach� 
 * qui effecturai une op�ration de d�bit ou de cr�dit qui ne correspond � l'attach� associ� au client titulaire du compte
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
