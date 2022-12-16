package pt.com.renan.javabechallenge.integration.imdb;

import org.springframework.stereotype.Service;

import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;


public class IMDBApiService extends ExternalApiMovieService{

	@Override
	protected String baseUrl() {
		return "https://imdb-api.com/en/API";
	}

	@Override
	protected String apiKey() {
		return "k_f5xbs09y";
	}

	@Override
	protected String apiUrl() {
		return "/Top250Movies/";
	}

	@Override
	protected String uri() {
		return apiUrl() + apiKey();
	}
	@Override
	protected <T extends ExternalApiMoviesData> Class<T> clazz() {
		return (Class<T>) IMDBMoviesData.class;
	}


	
}
