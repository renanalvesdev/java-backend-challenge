package pt.com.renan.javabechallenge.exception.user;

import pt.com.renan.javabechallenge.exception.BusinessException;

public class RoleNotFoundException extends BusinessException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleNotFoundException() {
		super("Role not found");
	}
}
