/**
 * 
 */
package net.fluance.app.web.util.exceptions;

@SuppressWarnings("serial")
public class UnknownIssuerException extends BadRequestException {

	public UnknownIssuerException(String issuer) {
		super("Unknown issuer: " + issuer);
	}
	
}
