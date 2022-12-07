package pt.com.renan.javabechallenge.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.com.renan.javabechallenge.service.impl.MovieServiceImpl;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {
	
	@Autowired
	private MovieServiceImpl service;
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/populate")
	@ResponseStatus(HttpStatus.CREATED)
	public void populate() {
		service.populate();
	}
	
}
