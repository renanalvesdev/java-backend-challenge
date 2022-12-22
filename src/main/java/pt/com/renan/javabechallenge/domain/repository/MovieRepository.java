package pt.com.renan.javabechallenge.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.com.renan.javabechallenge.domain.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{
  
	List<Movie> findTop10ByOrderByStarsDesc();
	
	@Query(value = "SELECT m FROM Movie m where m.title IN (:titles)")  
	 List<Movie> findByTitles(@Param("titles") List<String> titles);
	
	@Query(value = MovieRepositorySQL.SUGESTED_MOVIES_QUERY, nativeQuery = true)
	 Optional<Movie> findSuggestedMovieForUser(@Param("userId") Integer user);
	
	@Query(value = MovieRepositorySQL.RANDOM_MOVIE_QUERY, nativeQuery = true)
	 Optional<Movie> findRandomMovieForUser(@Param("userId") Integer user);

}