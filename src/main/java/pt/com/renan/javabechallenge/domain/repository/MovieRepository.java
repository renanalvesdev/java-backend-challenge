package pt.com.renan.javabechallenge.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.com.renan.javabechallenge.domain.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, String>{
  
	List<Movie> findTop10ByOrderByStarsDesc();

}
