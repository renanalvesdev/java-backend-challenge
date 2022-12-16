package pt.com.renan.javabechallenge.integration.imdb;

import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;

public class IMDBMovieData extends ExternalApiMovieData{

	private String Id ;
    private String Title ;
    private String Year ;
    private String Crew ;
    
    @Override
    public Movie toMovie() {
    	
    	return  Movie
    			.builder()
    			.Id(Id)
    			.Title(Title)
    			.Year(Year)
    			.Crew(Crew)
    			.build();
    	    	
    }
 
}
