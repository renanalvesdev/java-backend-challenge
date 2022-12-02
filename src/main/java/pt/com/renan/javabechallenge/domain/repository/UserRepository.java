package pt.com.renan.javabechallenge.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.com.renan.javabechallenge.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByLogin(String login);
	
}
