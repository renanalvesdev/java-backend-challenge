package pt.com.renan.javabechallenge.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pt.com.renan.javabechallenge.exception.BusinessException;
import pt.com.renan.javabechallenge.rest.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBusinessException (BusinessException ex) {
		String errorMessage = ex.getMessage();
		return new ApiErrors(errorMessage);
	}
	
	
	
}
