package net.fluance.app.web.util.exceptions;

public class ManagedException  extends RuntimeException {
	
	public ManagedException(){}
	
	public ManagedException(String message){
		super(message);
	}
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
