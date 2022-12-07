package pt.com.renan.javabechallenge.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.enums.Roles;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	@NotEmpty(message = "{field.login.required}")
	private String login;
	
	@Column
	@NotEmpty(message = "{field.password.required}")
	private String password;
	
	@ElementCollection
	@CollectionTable(name = "tb_privileges", joinColumns = @JoinColumn(name = "id"))
	@Enumerated(EnumType.STRING)
	private List<Roles> privileges ; 
	
	public String[] getPrivilegesToArray() {
		
		return getPrivileges()
				.stream()
				.map(p -> p.toString())
				.toArray(String[]::new);

	}
	
	@ManyToMany
	@JoinTable(
		name = "tb_favorite_movies", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "movie_id")
	)
	private List<Movie> favoriteMovies;

	
}
