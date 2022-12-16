package pt.com.renan.javabechallenge.integration.themoviedb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;

@Getter
@Setter
public class TheMovieDbMoviesData extends ExternalApiMoviesData{

	private List<TheMovieDbMovieData> results ;

	@Override
	protected List<? extends ExternalApiMovieData> getExternalResults() {
		return results;
	}
	
}
