package pt.com.renan.javabechallenge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;
import pt.com.renan.javabechallenge.domain.repository.UserRepository;
import pt.com.renan.javabechallenge.integration.IMDBMovieData;
import pt.com.renan.javabechallenge.integration.IMDBMoviesData;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void populate() {
		
		List<IMDBMovieData> IMDBMovies = IMDBAllMovies().getItems();
		
		IMDBMovies
			.stream()
			.filter(movie -> !repository.existsById(movie.getId()))
			.forEach(movie -> repository.save(movie.toMovie()));
		
	}
	
	public List<Movie> getAll(){
		return repository.findAll();
	}
	
	@Transactional
	public void addToFavorites(String id, Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid User"));
		Movie movie = repository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Movie"));
		
		if(user.getFavoriteMovies().stream().anyMatch(m -> m.equals(movie))) {
			throw new RuntimeException("Movie already favorited");
		}
		
		movie.increaseStars();
		user.getFavoriteMovies().add(movie);
		
		repository.save(movie);
		userRepository.save(user);
	}
	
	@Transactional
	public void removeFromFavorites(String id, Integer userId) {
		
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid User"));
		Movie movie = repository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Movie"));
		
		if(user.getFavoriteMovies().stream().noneMatch(m -> m.equals(movie))) {
			throw new RuntimeException("Selected movie is not favorited.");
		}
		
		movie.decreaseStars();
		user.getFavoriteMovies().remove(movie);
		
		repository.save(movie);
		userRepository.save(user);
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
