/**
 * 
 */
package net.fluance.app.data.exception;


@SuppressWarnings("serial")
public class DataException extends Exception {

	private Exception exception;

	/**
	 * @param exception
	 */
	public DataException(String message) {
		super(message);
	}
	
	/**
	 * @param exception
	 */
	public DataException(Exception exception) {
		super();
		this.exception = exception;
	}
	
	/**
	 * @param exception
	 */
	public DataException(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
