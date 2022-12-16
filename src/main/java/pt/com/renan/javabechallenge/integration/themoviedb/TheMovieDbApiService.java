package pt.com.renan.javabechallenge.integration.themoviedb;

import org.springframework.stereotype.Service;

import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.ExternalApiMoviesData;

@Service
public class TheMovieDbApiService extends ExternalApiMovieService{

	@Override
	protected String baseUrl() {
		return "https://api.themoviedb.org";
	}

	@Override
	protected String apiKey() {
		return "04c2d3599d2649192a1f61505d5d148c";
	}

	@Override
	protected String apiUrl() {
		return "/movie/top_rated";
	}

	@Override
	protected String uri() {
		return "/3" + apiUrl() + "?"+"api_key="+apiKey();
	}
	@Override
	protected <T extends ExternalApiMoviesData> Class<T> clazz() {
		return (Class<T>) TheMovieDbMoviesData.class;
	}


	
}
