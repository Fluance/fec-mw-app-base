package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends ManagedException {
	
	public static String NO_STAFF_ID = "NO_STAFF_ID_FOUND";
	
	public ForbiddenException(){}
	
	public ForbiddenException(String message){
		super(message);
	}
}