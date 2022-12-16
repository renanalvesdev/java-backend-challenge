package pt.com.renan.javabechallenge.integration;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import pt.com.renan.javabechallenge.domain.entity.Movie;

@Data
public abstract class ExternalApiMoviesData {
	
	protected abstract List<? extends ExternalApiMovieData> getExternalResults();
	
	public List<Movie> getMovieResults() {
		return getExternalResults().stream().map(imdbMovie -> imdbMovie.toMovie()).collect(Collectors.toList());
	}
}
