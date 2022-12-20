package pt.com.renan.javabechallenge.integration;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public abstract class ExternalApiMovieService {
	
	private String apiKey;
	
	public List<String> allMovies() {
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
	
	protected void setApiKey(String apiKey) { 
		this.apiKey = apiKey;
	}
	
	protected String getApiKey() {
		return this.apiKey;
	}
	
	protected abstract <T extends ExternalApiMoviesData> Class<T> clazz();
	protected abstract String baseUrl();
	protected abstract String apiUrl();
	protected abstract String uri();
	
}
