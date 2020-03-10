package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ManagedException {
	
	public BadRequestException(){}
	
	public BadRequestException(String message){
		super(message);
	}
}