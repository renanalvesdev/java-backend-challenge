package pt.com.renan.javabechallenge.rest.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pt.com.renan.javabechallenge.domain.entity.enums.Roles;

@Getter @Setter
public class UpdatePermissionsDTO {
	
	private List<Roles> permissions;
	
}
