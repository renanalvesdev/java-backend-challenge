package pt.com.renan.javabechallenge.exception.movie;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class MovieAlreadyFavoritedException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MovieAlreadyFavoritedException() {
		super("Movie already favorited");
	}
}
