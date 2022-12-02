package pt.com.renan.javabechallenge.exception;

public class InvalidPasswordException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super("Incorrect Password");
	}
}
