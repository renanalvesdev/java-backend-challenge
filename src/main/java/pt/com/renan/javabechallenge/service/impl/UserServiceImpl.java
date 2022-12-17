package pt.com.renan.javabechallenge.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.com.renan.javabechallenge.domain.entity.User;
import pt.com.renan.javabechallenge.domain.entity.enums.Roles;
import pt.com.renan.javabechallenge.domain.repository.UserRepository;
import pt.com.renan.javabechallenge.exception.user.InvalidPasswordException;
import pt.com.renan.javabechallenge.exception.user.RoleNotFoundException;


@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository repository;
	
	
	@Transactional
	public User save(User user) {
		return repository.save(user);
	}
	
	public UserDetails authenticate(User user) {
		UserDetails userDetails = loadUserByUsername(user.getLogin());
		boolean passwordMatches = encoder.matches(user.getPassword(), userDetails.getPassword());
		
		if(passwordMatches)
			return userDetails;
		
		throw new InvalidPasswordException();
		
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User not found in database"));
		
		return org.springframework.security.core.userdetails.User
				.builder()
				.username(user.getLogin())
				.password(user.getPassword())
				.roles(user.getPrivilegesToArray())
				.build();
	} 
	

	@Transactional
	public void updatePermissions(Integer id, List<Roles> permissions) {
		
		Optional<User> opUser  = repository.findById(id);
		
		if(!opUser.isPresent()) {
			throw new RoleNotFoundException();
		}
		
		User user = opUser.get();
		user.setPrivileges(permissions);
		repository.save(user);
				
	}

}
