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

	public List<IMDBMovieData> IMDBAllMovies() {
		Mono<IMDBMoviesData> movies =  getWebClient().get()
				.uri("/Top250Movies/k_f5xbs09y")
				.retrieve()
				.bodyToMono(IMDBMoviesData.class);
		
		IMDBMoviesData moviesObject = movies.block(); 
		
		return moviesObject.getItems();
	}
	
	private WebClient getWebClient() {		
		return WebClient.builder()
				.baseUrl("https://imdb-api.com/en/API")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
