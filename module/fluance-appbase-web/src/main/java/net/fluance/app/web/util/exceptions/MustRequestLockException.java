package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class MustRequestLockException extends ManagedException {

	public static String RESOURCE_NOT_LOCKED = "Resource must be locked before any modifications";

	public MustRequestLockException(){}

	public MustRequestLockException(String message){
		super(message);
	}
}
