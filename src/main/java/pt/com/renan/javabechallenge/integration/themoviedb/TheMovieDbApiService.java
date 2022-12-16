package pt.com.renan.javabechallenge.integration.themoviedb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;

@Service("theMovieDbApiService")
public class TheMovieDbApiService extends ExternalApiMovieService{

	@Override
	protected String baseUrl() {
		return "https://api.themoviedb.org";
	}

	@Override
	protected String apiUrl() {
		return "/movie/top_rated";
	}

	@Override
	protected String uri() {
		return "/3" + apiUrl() + "?"+"api_key="+getApiKey();
	}

	@Override
	@Value("${themoviedb.key}")
	protected void setApiKey(String apiKey) {
		super.setApiKey(apiKey);
	}
	
	@Override
	protected <T extends ExternalApiMoviesData> Class<T> clazz() {
		return (Class<T>) TheMovieDbMoviesData.class;
	}




	
}
