/**
 * 
 */
package net.fluance.app.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.OncePerRequestFilter;

public class CORSFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LogManager.getLogger(CORSFilter.class);
	public static final String EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";

	@Value("${app.header.from-public-network}")
	private String fromPublicNetworkHeader;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOGGER.trace("Sending Header 'Access-Control-Allow-Origin' on path '" + request.getPathInfo() +  "' ...");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader(EXPOSE_HEADERS_HEADER, fromPublicNetworkHeader);

		if ("OPTIONS".equals(request.getMethod())) {
			LOGGER.trace("Sending Header....");
			// CORS "pre-flight" request
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			response.addHeader("Access-Control-Max-Age", "3600");
			response.addHeader(EXPOSE_HEADERS_HEADER, fromPublicNetworkHeader);
		} else {
			filterChain.doFilter(request, response);
		}
	}
	
	/**
	 * @param fromPublicNetworkHeader the fromPublicNetworkHeader to set
	 */
	public void setFromPublicNetworkHeader(String fromPublicNetworkHeader) {
		this.fromPublicNetworkHeader = fromPublicNetworkHeader;
	}

	/**
	 * @return the fromPublicNetworkHeader
	 */
	public String getFromPublicNetworkHeader() {
		return fromPublicNetworkHeader;
	}
	
}
