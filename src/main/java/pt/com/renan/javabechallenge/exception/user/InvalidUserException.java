package pt.com.renan.javabechallenge.exception.user;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class InvalidUserException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUserException() {
		super("Invalid User");
	}
}
