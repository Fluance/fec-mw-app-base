package net.fluance.app.web.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
	
	public static final String EMPTY_URLPATH = "/";
	
	/**
	 * Checks if a request has a given header
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static boolean hasHeader(HttpServletRequest request, String headerName) {
		if(headerName == null || headerName.isEmpty()) {
			return false;
		}
		
		boolean hasHeader = false;
		
		Enumeration<String> headerNamesEnum = request.getHeaderNames();
		
		while(!hasHeader && headerNamesEnum.hasMoreElements()) {
			hasHeader = headerNamesEnum.nextElement().equalsIgnoreCase(headerName);
		}
		
		return hasHeader;
	}

	/**
	 * Checks if a response has a given header
	 * @param response
	 * @param headerName
	 * @return
	 */
	public static boolean hasHeader(HttpServletResponse response, String headerName) {
		if(headerName == null || headerName.isEmpty()) {
			return false;
		}
		
		boolean hasHeader = false;
		
		Collection<String> headerNames = response.getHeaderNames();
		
		for(String currentHeaderName : headerNames) {
			if(currentHeaderName.equalsIgnoreCase(headerName)) {
				hasHeader = true;
				break;
			}
		}
		
		return hasHeader;
	}

	/**
	 * Checks if a response has a given header
	 * @param response
	 * @param headerName
	 * @return
	 */
	public static boolean matchesUrl(HttpServletRequest request, String url) {
		if(url == null) {
			return (request.getRequestURL().toString() == null || request.getRequestURL().toString().isEmpty());
		}
		
		return url.equalsIgnoreCase(request.getRequestURL().toString());
	}

	/**
	 * 
	 * @param request
	 * @param path
	 * @return
	 * @throws MalformedURLException
	 */
	public static boolean matchesPath(HttpServletRequest request, String path) throws MalformedURLException {
		String urlPath = new URL(request.getRequestURL().toString()).getPath();
	
		if(path == null) {
			return (urlPath == null || urlPath.isEmpty() || EMPTY_URLPATH.equals(urlPath));
		}
		
		return path.equalsIgnoreCase(urlPath);
	}
}

