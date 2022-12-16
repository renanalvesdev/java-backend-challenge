package pt.com.renan.javabechallenge.integration;

public abstract class ExternalApiMovieData {
	
	protected abstract String title();
	
    public MovieDTO toMovieDto() {
    	return  MovieDTO
    			.builder()
    			.title(title())
    			.build();
    }
    
}
