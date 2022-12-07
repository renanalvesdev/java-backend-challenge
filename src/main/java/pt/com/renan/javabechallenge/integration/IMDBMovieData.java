package pt.com.renan.javabechallenge.integration;

import lombok.Data;
import pt.com.renan.javabechallenge.domain.entity.Movie;

@Data
public class IMDBMovieData {

	private String Id ;
    private String Rank ;
    private String Title ;
    private String FullTitle ; 
    private String Year ;
    private String Image ;
    private String Crew ;
    private String IMDbRating; 
    private String IMDbRatingCount; 
    
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
