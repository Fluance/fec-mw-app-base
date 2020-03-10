/**
 * 
 */
package net.fluance.app.web.support.payload.response;

public class GenericResponsePayload {

	String message;
	

	/**
	 * @param message
	 */
	public GenericResponsePayload() {}
	
	/**
	 * @param message
	 */
	public GenericResponsePayload(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
