package pt.com.renan.javabechallenge.integration.imdb;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieData;

@Getter
@Setter
public class IMDBMovieData extends ExternalApiMovieData{

	private String Id ;
    private String Title ;
    private String Year ;
    private String Crew ;
	
    @Override
	protected String title() {
		return this.getTitle();
	}
    
   
 
}
