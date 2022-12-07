package pt.com.renan.javabechallenge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;
import pt.com.renan.javabechallenge.integration.IMDBMovieData;
import pt.com.renan.javabechallenge.integration.IMDBMoviesData;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl {

	@Autowired
	private MovieRepository repository;
	
	public void populate() {
		
		List<IMDBMovieData> IMDBMovies = IMDBAllMovies().getItems();
		
		IMDBMovies
			.stream()
			.filter(movie -> !repository.existsById(movie.getId()))
			.forEach(movie -> repository.save(movie.toMovie()));
		
	}

	private IMDBMoviesData IMDBAllMovies() {
		Mono<IMDBMoviesData> movies =  getWebClient().get()
				.uri("/Top250Movies/k_f5xbs09y")
				.retrieve()
				.bodyToMono(IMDBMoviesData.class);
		
		IMDBMoviesData moviesObject = movies.block(); 
		
		return moviesObject;
	}
	
	private WebClient getWebClient() {		
		return WebClient.builder()
				.baseUrl("https://imdb-api.com/en/API")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
