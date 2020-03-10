/**
 * 
 */
package net.fluance.app.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;

public class GenericCORSFilter extends AbstractFilter {

	private static final Logger LOGGER = LogManager.getLogger(GenericCORSFilter.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
		httpServletResponse.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization");
		chain.doFilter(request, httpServletResponse);
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

}
