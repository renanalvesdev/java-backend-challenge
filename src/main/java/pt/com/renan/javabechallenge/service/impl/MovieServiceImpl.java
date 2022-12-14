package pt.com.renan.javabechallenge.service.impl;

import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;
import pt.com.renan.javabechallenge.domain.repository.UserRepository;
import pt.com.renan.javabechallenge.security.authentication.AuthenticationFacade;

@Service
@CacheConfig(cacheNames = "movies")
@RequiredArgsConstructor
public class MovieServiceImpl {

	@Autowired
	private IMDBApiService imdbApiService;
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	public void populate() {
		
		imdbApiService.IMDBAllMovies()
			.stream()
			.filter(movie -> !repository.existsById(movie.getId()))
			.forEach(movie -> repository.save(movie.toMovie()));
		
	}
	
	public List<Movie> getAll(){
		return repository.findAll();
	}
	
	@Transactional
	public void addToFavorites(String id) {
		
		User user = findUser();
		Movie movie = findMovie(id);
		
		if(user.getFavoriteMovies().stream().anyMatch(m -> m.equals(movie))) {
			throw new RuntimeException("Movie already favorited");
		}
		
		movie.increaseStars();
		user.getFavoriteMovies().add(movie);
		
		repository.save(movie);
		userRepository.save(user);
	}
	

	public void operationFavorites(String id, BiConsumer<User, Movie> operation) {
		
		User user = findUser();
		Movie movie = findMovie(id);
		operation.accept(user, movie);
		
		repository.save(movie);
		userRepository.save(user);
	}

	@Transactional
	public void removeFromFavorites(String id) {		
		operationFavorites(id, (u,m) -> remove(u,m));
	}

	private void remove(User user, Movie movie) {
		if(user.getFavoriteMovies().stream().noneMatch(m -> m.equals(movie))) {
			throw new RuntimeException("Selected movie is not favorited.");
		}
		
		movie.decreaseStars();
		user.getFavoriteMovies().remove(movie);
	}
	
	@Cacheable
	public List<Movie> topMovies() {
		return repository.findTop10ByOrderByStarsDesc();
	}
	
	public List<Movie> favoriteMovies() {
		User user = findUser();
		return user.getFavoriteMovies();
	}
	
	public Movie findMovie(String id) {
		return repository
				.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid Movie"));
	}
	
	private User findUser() {
		return userRepository
				.findByLogin(authenticationFacade.getLoggerUser())
				.orElseThrow(() -> new RuntimeException("Invalid User"));
	}
	

}
