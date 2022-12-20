package pt.com.renan.javabechallenge.integration.imdb;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;

@Getter
@Setter
public class IMDBMoviesData extends ExternalApiMoviesData{

	private List<IMDBMovieData> Items;
	private String errorMessage;

	@Override
	protected List<? extends ExternalApiMovieData> getExternalResults() {
		return Items;
	}

	@Override
	protected Boolean getAvailable() {
		return this.errorMessage == null || this.errorMessage.isEmpty();
	}
	
}
