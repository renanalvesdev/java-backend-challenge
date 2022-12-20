package pt.com.renan.javabechallenge.integration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;

@Service
public class PopulateExternalMovies {

	
	@Autowired
	private MovieRepository repository;
	
	@Transactional
	public void populate(ExternalApiMovieService externalApiMovieService) {
		
		List<String> newMovies = externalApiMovieService.allMovies();
		
		newMovies.removeAll(existingMoviesTitlesByTitles(newMovies));

		repository.saveAll(
				newMovies
				.stream()
				.map(nm -> new Movie(nm))
				.collect(Collectors.toList()));
		
	}	
	
	private List<String> existingMoviesTitlesByTitles(List<String> titles) {
		return repository
				.findByTitles(titles)
				.stream()
				.map(m -> m.getTitle())
				.collect(Collectors.toList());
	}
		
}
