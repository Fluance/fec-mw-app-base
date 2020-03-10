/**
 * 
 */
package net.fluance.app.web.servlet.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

import net.fluance.app.spring.util.Constants;

public abstract class AbstractFilter extends GenericFilterBean {

	/**
	 * 
	 * @param response
	 * @param status
	 * @param message
	 * @throws IOException
	 */
	protected void sendInternalServerError(HttpServletResponse response, String message) throws IOException {
		addCORSHeaders(response);
		String sentMessage = (message !=null && !message.isEmpty()) ? message : Constants.DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sentMessage);
	}
	
	/**
	 * 
	 * @param response
	 */
	protected void addCORSHeaders(HttpServletResponse response) {
		if(!response.containsHeader("Access-Control-Allow-Origin")) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
	}
	
	/**
	 * Get the class's logger
	 * @return
	 */
	public abstract Logger getLogger();
	
}
