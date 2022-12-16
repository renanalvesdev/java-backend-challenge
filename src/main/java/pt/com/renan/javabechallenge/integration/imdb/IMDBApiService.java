package pt.com.renan.javabechallenge.integration.imdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;

@Service("iMDBApiService")
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
	@Value("${imdb.key}")
	protected void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}
	
	@Override
	protected <T extends ExternalApiMoviesData> Class<T> clazz() {
		return (Class<T>) IMDBMoviesData.class;
	}
	
}
