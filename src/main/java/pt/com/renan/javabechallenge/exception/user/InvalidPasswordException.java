package pt.com.renan.javabechallenge.exception.user;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class InvalidPasswordException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super("Incorrect Password");
	}
}
