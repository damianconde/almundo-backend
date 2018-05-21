package almundo.com.backend.exception;

public class WithoutEmployeeException extends InterruptedException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WithoutEmployeeException(String message) {
		super(message);
	}
}
