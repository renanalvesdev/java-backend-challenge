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
	private IMDBApiService imdbApiService;
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void populate() {
		
		List<IMDBMovieData> IMDBMovies = imdbApiService.IMDBAllMovies();
		
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
		
		User user = findUser(userId);
		Movie movie = findMovie(id);
		
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
		
		User user = findUser(userId);
		Movie movie = findMovie(id);
		
		if(user.getFavoriteMovies().stream().noneMatch(m -> m.equals(movie))) {
			throw new RuntimeException("Selected movie is not favorited.");
		}
		
		movie.decreaseStars();
		user.getFavoriteMovies().remove(movie);
		
		repository.save(movie);
		userRepository.save(user);
	}
	
	public List<Movie> topMovies() {
		return repository.findTop10ByOrderByStarsDesc();
	}
	
	public List<Movie> favoriteMovies(Integer userId) {
		User user = findUser(userId);
		return user.getFavoriteMovies();
	}
	
	private Movie findMovie(String id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Movie"));
	}
	
	private User findUser(Integer userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid User"));
	}
}
