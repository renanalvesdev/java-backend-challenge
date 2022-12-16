package pt.com.renan.javabechallenge.integration;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pt.com.renan.javabechallenge.domain.entity.Movie;

@Service
public abstract class ExternalApiMovieService {
	
	public List<Movie> allMovies() {
		return retreiveData().getMovieResults();
	}

	private ExternalApiMoviesData retreiveData() {

		ExternalApiMoviesData res =  
				(ExternalApiMoviesData) getWebClient().get()
				.uri(uri())
				.retrieve()
				.bodyToMono(clazz()).block();
		
		return res;

	}
	
	private WebClient getWebClient() {		
		return WebClient.builder()
				.baseUrl(baseUrl())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	protected abstract <T extends ExternalApiMoviesData> Class<T> clazz();
	protected abstract String baseUrl();
	protected abstract String apiKey();
	protected abstract String apiUrl();
	protected abstract String uri();
	
}
