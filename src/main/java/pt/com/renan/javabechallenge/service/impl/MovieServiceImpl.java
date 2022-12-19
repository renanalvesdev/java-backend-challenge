package pt.com.renan.javabechallenge.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;
import pt.com.renan.javabechallenge.domain.repository.UserRepository;
import pt.com.renan.javabechallenge.exception.movie.InvalidMovieException;
import pt.com.renan.javabechallenge.exception.movie.MovieAlreadyFavoritedException;
import pt.com.renan.javabechallenge.exception.movie.MovieNotFavoritedException;
import pt.com.renan.javabechallenge.exception.user.InvalidUserException;
import pt.com.renan.javabechallenge.integration.ExternalApiMovieService;
import pt.com.renan.javabechallenge.integration.MovieDTO;
import pt.com.renan.javabechallenge.security.authentication.AuthenticationFacade;

@Service
@CacheConfig(cacheNames = "movies")
@RequiredArgsConstructor
public class MovieServiceImpl {

	private static final String TOP_MOVIES_KEY= "topMovies";
	
	@Autowired
	@Qualifier("iMDBApiService")
	private ExternalApiMovieService exApiMovieService;
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	private final HazelcastInstance hazelcastInstance  = Hazelcast.newHazelcastInstance();
	
	@Transactional
	public void populate() {
		
		List<MovieDTO> newMovies = exApiMovieService.allMovies();

		List<MovieDTO> savedMovies = repository
				.findAll()
				.stream()
				.map(movie -> movie.toMovieDto())
				.collect(Collectors.toList());
		
		newMovies.removeAll(savedMovies);
		
		newMovies.stream().forEach(movie -> repository.save(movie.toMovie()));
		
		
	}
	
	public List<Movie> getAll(){
		return repository.findAll();
	}
	
	public void operationFavorites(Integer id, BiConsumer<User, Movie> operation) {
		
		User user = findUser();
		Movie movie = findMovie(id);
		operation.accept(user, movie);
		
		repository.save(movie);
		userRepository.save(user);
	}
	
	//ADD to favorites
	@Transactional
	public void addToFavorites(Integer id) {		
		operationFavorites(id, (u,m) -> add(u,m));
	}
	
	@Transactional
	public void add(User user, Movie movie) {
		
		if(user.getFavoriteMovies().stream().anyMatch(m -> m.equals(movie))) {
			throw new MovieAlreadyFavoritedException();
		}
		
		movie.addToFavorites(user);
	}
	

	//remove from favorites
	@Transactional
	public void removeFromFavorites(Integer id) {		
		operationFavorites(id, (u,m) -> remove(u,m));
	}
	
	private void remove(User user, Movie movie) {
		if(user.getFavoriteMovies().stream().noneMatch(m -> m.equals(movie))) {
			throw new MovieNotFavoritedException();
		}
		
		movie.removeFromFavorites(user);
	}
	
	@RateLimiter(name = "rateLimiterApi", fallbackMethod = "topMoviesCached")
	public List<Movie> topMovies() {
		List<Movie> topMovies = repository.findTop10ByOrderByStarsDesc();
		retrieveMap().put(TOP_MOVIES_KEY, topMovies);
		return topMovies;
	}
	
	public List<Movie> topMoviesCached(RequestNotPermitted requestNotPermited) {
		System.out.println("getting top movies from cache");
		return retrieveMap().get(TOP_MOVIES_KEY);
	}
	
	public List<Movie> favoriteMovies() {
		User user = findUser();
		return user.getFavoriteMovies();
	}
	
	public Movie findMovie(Integer id) {
		return repository
				.findById(id)
				.orElseThrow(() -> new InvalidMovieException());
	}
	
	private User findUser() {
		return userRepository
				.findByLogin(authenticationFacade.getLoggerUser())
				.orElseThrow(() -> new InvalidUserException());
	}
	
	//CACHE

	private ConcurrentMap<String,List<Movie>> retrieveMap() {
        return hazelcastInstance.getMap("map");
    }

}
