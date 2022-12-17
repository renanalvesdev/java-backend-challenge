package pt.com.renan.javabechallenge.exception.movie;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class InvalidMovieException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMovieException() {
		super("Invalid Movie");
	}
}
