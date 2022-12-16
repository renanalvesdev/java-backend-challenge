package pt.com.renan.javabechallenge.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pt.com.renan.javabechallenge.integration.MovieDTO;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "movie")
public class Movie {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Integer Id;
	
	@Column
	private String title;
	
	@Column
	@EqualsAndHashCode.Exclude
	private Integer stars;
	
	public Movie() {
		this.stars = 0;
	}
	
	public void decreaseStars() {
		stars--;
	}
	
	public void increaseStars() {
		stars++;
	}
	
	public void addToFavorites(User user) {
		this.increaseStars();
		user.addMovieToFavorites(this);
	}
	
	public void removeFromFavorites(User user) {
		this.decreaseStars();
		user.removeMovieToFavorites(this);
	}
	
	public MovieDTO toMovieDto() {
		return  MovieDTO
    			.builder()
    			.title(title)
    			.build();
	}
	
}
