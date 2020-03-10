/**
 * 
 */
package net.fluance.app.web.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ManagedException {

	public UnauthorizedException(String message) {
		super(message);
	}
	
}
