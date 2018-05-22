package almundo.com.backend.exception;

public class WithoutWaitCallException extends InterruptedException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WithoutWaitCallException(String message) {
		super(message);
	}
}
