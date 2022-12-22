package pt.com.renan.javabechallenge.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.service.impl.MovieServiceImpl;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
	
	@Autowired
	private MovieServiceImpl service;
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/populate")
	@ResponseStatus(HttpStatus.CREATED)
	public void populate() {
		service.populate();
	}
	
	@GetMapping("/all")
	public List<Movie> getAll(){
		return service.getAll();
	}
	
	@PostMapping("/addToFavorites/{id}")
	public void addToFavorites(@PathVariable Integer id){
		service.addToFavorites(id);
	}
	
	@PostMapping("/removeFromFavorites/{id}")
	public void removeFromFavorites(@PathVariable Integer id){
		service.removeFromFavorites(id);
	}
	
	@PostMapping("/favoriteSuggesttedMovie")
	public void favoriteSugestedMovie(){
		service.favoriteSuggestedMovie();
	}
	
	
	@GetMapping("/topMovies")
	public List<Movie> topMovies() {
		return service.topMovies();
	}
	
	@GetMapping("/favoriteMovies")
	public List<Movie> favoriteMovies() {
		return service.favoriteMovies();
	}
	
}
