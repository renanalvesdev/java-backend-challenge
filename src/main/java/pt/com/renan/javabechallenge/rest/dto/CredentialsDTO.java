package pt.com.renan.javabechallenge.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsDTO {

	private String login;
	private String password;
	
}
