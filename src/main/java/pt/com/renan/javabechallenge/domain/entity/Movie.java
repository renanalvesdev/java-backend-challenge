package pt.com.renan.javabechallenge.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "movie")
public class Movie {
	
	@Id
	private String Id;
	
	@Column
	private String Title;
	
	@Column
	private String Year;
	
	@Column
	private String Crew;
	
	@Column
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
	
	public boolean equals(Movie movie) {
		return movie.getId() == Id;
	}
	
}
