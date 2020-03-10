/**
 * 
 */
package net.fluance.app.web.servlet.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javassist.NotFoundException;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.log.ResourceType;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.app.web.util.exceptions.ManagedException;



public abstract class AbstractRestController extends AbstractController {

	@Autowired(required = false)
	private LogService systemAccessLogService;

	/**
	 * 
	 * @param exc The exception that was raised
	 * @return
	 * @throws Exception 
	 */
	public ResponseEntity<?> handleException(Exception exc) {
		if(exc instanceof DataException) {
			return (ResponseEntity<?>) handleDataException((DataException) exc);
		} else if (exc instanceof IllegalArgumentException){
			getLogger().info("", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			String msg = exc.getMessage();
			HttpStatus status = HttpStatus.BAD_REQUEST;
			grp.setMessage(msg);
			return new ResponseEntity<>(grp, status);
		} else if (exc instanceof NotFoundException){
			getLogger().info("", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			grp.setMessage(exc.getMessage());
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(grp, status);
		} else if (exc instanceof EmptyResultDataAccessException){
			getLogger().info("", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			HttpStatus status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(grp, status);
		} else if (exc instanceof BadSqlGrammarException){
			getLogger().error("", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			String msg = DATABASE_EXCEPTION;
			if(exc!=null && exc.getCause() instanceof SQLException){
				switch(((SQLException)exc.getCause()).getSQLState()){
				case "42P01": 
					msg = UNDEFINED_TABLE_MESSAGE;
					break;
				case "42703":
					msg = UNKNOWN_COLUMN_MESSAGE;
					break;
				default: break;
				}
			}
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			grp.setMessage(msg);
			return new ResponseEntity<>(grp, status);
		} else if (exc instanceof SQLException){
			getLogger().warn("", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			grp.setMessage(exc.getMessage());
			HttpStatus status = HttpStatus.CONFLICT;
			return new ResponseEntity<>(grp, status);
		}else if (exc instanceof ManagedException){
			getLogger().warn("ManagedException : ", exc);
			GenericResponsePayload grp = new GenericResponsePayload();
			grp.setMessage(exc.getMessage());
			HttpStatus status = null;
			ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(exc.getClass(), ResponseStatus.class);
			if(annotation != null) {
				status = annotation.value();
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}			
			return new ResponseEntity<>(grp, status);
		} else {
			getLogger().error("", exc);
			exc.printStackTrace();
			GenericResponsePayload grp = new GenericResponsePayload();
			String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			grp.setMessage(msg);
			return new ResponseEntity<>(grp, status);
		}
	}

	public void systemLog(HttpServletRequest request){
		systemLog(request, null, null);
	}

	public void systemLog(HttpServletRequest request, ResourceType resourceType){
		systemLog(request, resourceType, null);
	}

	public void systemLog(HttpServletRequest request, String resourceId){
		systemLog(request, null, resourceId);
	}

	protected void systemLog(HttpServletRequest request, ResourceType resourceType, String resourceId){
		systemAccessLogService.log(resourceType, null, request.getParameterMap(), (User)request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), resourceId);
	}
}
