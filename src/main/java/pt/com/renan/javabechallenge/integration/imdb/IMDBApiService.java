package pt.com.renan.javabechallenge.integration.imdb;

import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;


public class IMDBApiService extends ExternalApiMovieService{

	@Override
	protected String baseUrl() {
		return "https://imdb-api.com/en/API";
	}

	@Override
	protected String apiUrl() {
		return "/Top250Movies/";
	}

	@Override
	protected String uri() {
		return apiUrl() + getApiKey();
	}
	@Override
	protected <T extends ExternalApiMoviesData> Class<T> clazz() {
		return (Class<T>) IMDBMoviesData.class;
	}
	
}
