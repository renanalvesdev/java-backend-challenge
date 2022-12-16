package pt.com.renan.javabechallenge.integration.themoviedb;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;

@Getter
@Setter
public class TheMovieDbMovieData extends ExternalApiMovieData{
	
	private String original_title;

	@Override
	protected Movie toMovie() {
		return  Movie
    			.builder()
    			.Title(original_title)
    			.build();
	}
	
}
