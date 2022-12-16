package pt.com.renan.javabechallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pt.com.renan.javabechallenge.domain.entity.Movie;
import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.repository.MovieRepository;
import pt.com.renan.javabechallenge.domain.repository.UserRepository;
import pt.com.renan.javabechallenge.service.impl.MovieServiceImpl;

@ExtendWith(SpringExtension.class)
public class MovieServiceImplTest {

	@InjectMocks
	private MovieServiceImpl service;
	
	@Mock
	private MovieRepository repository;
	
	@Mock
	private UserRepository userRepository;
	
	private Movie movie1,movie2,movie3,movie4;
	
	private List<Movie> movies ;
	
	private User user;
	
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		mockMovie();
	}
	
	@Test
	void shouldReturnAnMovieInstanceWhenMovieIsFound() {
		Mockito.when(repository.findById(Mockito.eq(1))).thenReturn(Optional.of(movie1));
		Movie response = service.findMovie(1);
		assertEquals(Movie.class, response.getClass());
	}
	
	@Test
	void shouldReturnAnExceptionWhenMovieIsNotFound() {
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class, () -> service.findMovie(1));
		assertEquals("Invalid Movie", exception.getMessage());
	}
	
	@Test 
	void shouldReturnAnListOfMoviesWhenGetAllMovies() {
		when(repository.findAll()).thenReturn(movies);
		List<Movie> responseMovies = service.getAll();
		assertNotNull(responseMovies);
		assertEquals(2, responseMovies.size());
	}
	
	@Test
	void shouldAddMovieToFavorites() {
		Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(movie3));
		service.addToFavorites(3);
		final ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);
		Mockito.verify(repository).save(captor.capture());
		Movie movie = captor.getValue();
		assertEquals(11, movie.getStars());
	}
	
	private void mockMovie() {
		movie1 = new Movie(1, "Rumo ao hexa", 10);
		movie2 = new Movie(2, "A volta dos que nao foram", 2);
		movie3 = new Movie(3, "El chanfro ", 10);
		movie4 = new Movie(4, "Deu a louca na chapeuzinho", 10);

		movies = new ArrayList<Movie>();
		movies.add(movie1);
		movies.add(movie2);
		
		user = new User(1, null, null, null, movies);
	}
	
}
