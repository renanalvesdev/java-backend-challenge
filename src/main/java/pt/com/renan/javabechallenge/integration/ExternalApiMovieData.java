package pt.com.renan.javabechallenge.integration;

import pt.com.renan.javabechallenge.domain.entity.Movie;

public abstract class ExternalApiMovieData {
	
	protected abstract Movie toMovie();
}
