package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends ManagedException {
	
	public ConflictException(){}
	
	public ConflictException(String message){
		super(message);
	}
}