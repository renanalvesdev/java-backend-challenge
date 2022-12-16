package pt.com.renan.javabechallenge.integration;

import lombok.Builder;
import lombok.Data;
import pt.com.renan.javabechallenge.domain.entity.Movie;

@Data
@Builder
public class MovieDTO {
	
	private String title;
	
	public Movie toMovie() {
		return Movie.builder().title(title).stars(0).build();
	}
}
