package pt.com.renan.javabechallenge.exception.movie;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class MovieNotFavoritedException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MovieNotFavoritedException() {
		super("Selected movie is not favorited.");
	}
}
