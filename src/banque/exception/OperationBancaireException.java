package banque.exception;

/**
 * @auteur yohan 
 *
 */
public class OperationBancaireException extends Exception {

	private static final long serialVersionUID = 1L;

	public OperationBancaireException() {
		super();
	}

	public OperationBancaireException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OperationBancaireException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationBancaireException(String message) {
		super(message);
	}

	public OperationBancaireException(Throwable cause) {
		super(cause);
	}

}
