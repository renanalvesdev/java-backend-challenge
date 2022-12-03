package pt.com.renan.javabechallenge.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.entity.enums.Roles;
import pt.com.renan.javabechallenge.exception.InvalidPasswordException;
import pt.com.renan.javabechallenge.rest.dto.CredentialsDTO;
import pt.com.renan.javabechallenge.rest.dto.TokenDTO;
import pt.com.renan.javabechallenge.rest.dto.UpdatePermissionsDTO;
import pt.com.renan.javabechallenge.security.jwt.JwtService;
import pt.com.renan.javabechallenge.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	
	private final JwtService jwtService;
	private final UserServiceImpl userService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User save(@RequestBody @Valid User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userService.save(user);
	}
	
	@PostMapping("/auth")
	public TokenDTO authenticate(@RequestBody CredentialsDTO credentials) {
	
		try {
			User user = User.builder()
					.login(credentials.getLogin())
					.password(credentials.getPassword()).build();
			
			UserDetails authenticatedUser = userService.authenticate(user);
			String token = jwtService.generateToken(user);
			
			return new TokenDTO(user.getLogin(), token);
		} catch (UsernameNotFoundException  | InvalidPasswordException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PatchMapping("/update-permissions/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePermission(@PathVariable Integer id,
							@RequestBody UpdatePermissionsDTO dto) {
		
		List<Roles> permissions = dto.getPermissions();
		userService.updatePermissions(id, permissions);
	}
	
}
