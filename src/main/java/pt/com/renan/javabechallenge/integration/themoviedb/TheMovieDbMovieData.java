package pt.com.renan.javabechallenge.integration.themoviedb;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;

@Getter
@Setter
public class TheMovieDbMovieData extends ExternalApiMovieData{
	
	private String original_title;

	@Override
	protected String getExtTitle() {
		return this.getOriginal_title();
	}
	
}
