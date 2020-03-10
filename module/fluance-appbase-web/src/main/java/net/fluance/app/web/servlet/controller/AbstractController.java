/**
 * 
 */
package net.fluance.app.web.servlet.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.app.web.util.exceptions.ManagedException;

@ControllerAdvice
public abstract class AbstractController {

	protected static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = "Could not perform the requested operation, due to an internal error";
	protected static final String UNDEFINED_TABLE_MESSAGE = "Undefined data structure";
	protected static final String UNKNOWN_COLUMN_MESSAGE = "Undefined column found when querying database";
	protected static final String DATABASE_EXCEPTION = "Database Server Exception has occured";
	protected static final String BAD_REQUEST_MESSAGE = "Bad request";
	
	public abstract Logger getLogger();
	public abstract Object handleException(Exception exc);

	/**
	 * 
	 * @param exc The exception that was raised
	 * @return
	 */
	public abstract Object handleDataException(DataException exc);
	
//    @ExceptionHandler(value = Exception.class)  
    public Object globalHandleException(HttpServletResponse response, Exception exc) throws Exception{
    	getLogger().error(ExceptionUtils.getStackTrace(exc));
    	if (exc instanceof MissingServletRequestParameterException){
    		GenericResponsePayload grp = new GenericResponsePayload();
    		String msg = exc.getMessage();
    		HttpStatus status = HttpStatus.BAD_REQUEST;
    		grp.setMessage(msg);
    		return new ResponseEntity<>(grp, status);
    	} else if (exc instanceof ManagedException || exc instanceof HttpMessageNotReadableException){
    		throw exc;
    	} else 
    		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	return null;
    }	
}
