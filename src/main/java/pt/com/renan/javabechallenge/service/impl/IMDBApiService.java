package pt.com.renan.javabechallenge.service.impl;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pt.com.renan.javabechallenge.integration.IMDBMovieData;
import pt.com.renan.javabechallenge.integration.IMDBMoviesData;
import reactor.core.publisher.Mono;

@Service
public class IMDBApiService {

	private static final String API_KEY = "k_f5xbs09y";
	private static final String BASE_URL = "https://imdb-api.com/en/API";
	
	public List<IMDBMovieData> IMDBAllMovies() {
		Mono<IMDBMoviesData> movies =  getWebClient().get()
				.uri("/Top250Movies/"+API_KEY)
				.retrieve()
				.bodyToMono(IMDBMoviesData.class);
		
		IMDBMoviesData moviesObject = movies.block(); 
		
		return moviesObject.getItems();
	}
	
	private WebClient getWebClient() {		
		return WebClient.builder()
				.baseUrl(BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
