package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends ManagedException {
	
	private static final long serialVersionUID = 1L;
	
	public static String NO_USER_PROFILE_FOUND = "NO_USER_PROFILE_FOUND";
	
	public InternalServerErrorException(){}
	
	public InternalServerErrorException(String message){
		super(message);
	}
}