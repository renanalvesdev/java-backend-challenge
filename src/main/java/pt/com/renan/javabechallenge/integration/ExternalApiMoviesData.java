package pt.com.renan.javabechallenge.integration;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public abstract class ExternalApiMoviesData {
	
	protected abstract List<? extends ExternalApiMovieData> getExternalResults();
	
	public List<String> getMovieResults() {
		return getExternalResults().stream().map(movie -> movie.getExtTitle()).collect(Collectors.toList());
	}
}
