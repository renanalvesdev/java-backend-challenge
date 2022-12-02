package pt.com.renan.javabechallenge.exception;

public class RoleNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleNotFoundException() {
		super("Role not found");
	}
}
