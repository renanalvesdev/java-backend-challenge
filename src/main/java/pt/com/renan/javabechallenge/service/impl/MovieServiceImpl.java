package pt.com.renan.javabechallenge.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
import pt.com.renan.javabechallenge.integration.PopulateExternalMovies;
import pt.com.renan.javabechallenge.security.authentication.AuthenticationFacade;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl {

	private static final String TOP_MOVIES_KEY= "topMovies";
	
	@Autowired
	@Qualifier("iMDBApiService")
	private ExternalApiMovieService imdbService;
	
	@Autowired
	@Qualifier("theMovieDbApiService")
	private ExternalApiMovieService theMovieDbService;
	
	@Autowired
	private PopulateExternalMovies populateExternalMovies;
	
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	private final HazelcastInstance hazelcastInstance  = Hazelcast.newHazelcastInstance();
	
	//CIRCUIT BREAK
	@CircuitBreaker(name = "circuitBreakerPopulateMovies", fallbackMethod = "populateFallback")
	public void populate() {
		populateExternalMovies(imdbService);
	}
	
	public void populateFallback(Throwable t) {
		populateExternalMovies(theMovieDbService);
	}
	
	private void populateExternalMovies(ExternalApiMovieService externalApiMovieService) {
		populateExternalMovies.populate(externalApiMovieService); 
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
	
	public void favoriteSuggestedMovie() {
		
		Integer userId = findUser().getId();
		
		Movie suggestedMovie = repository
				.findSuggestedMovieForUser(userId)
				.orElse(
					repository.
					findRandomMovieForUser(userId)
					.orElseThrow(() -> new NoResultException())
				);
		
		addToFavorites(suggestedMovie.getId());
	}
	
	//RATE LIMITER
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
