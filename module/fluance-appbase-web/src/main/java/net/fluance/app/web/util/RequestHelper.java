/**
 * 
 */
package net.fluance.app.web.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.fluance.app.security.auth.OAuth2AccessToken;
import net.fluance.app.security.util.JwtHelper;
import net.fluance.app.spring.util.Constants;

public class RequestHelper {

	private static Logger LOGGER = LogManager.getLogger(RequestHelper.class);
	public static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

	/**
	 * Checks if a request has a given header
	 * 
	 * @param request
	 * @param headerName
	 * @return
	 */
	public boolean hasHeader(HttpServletRequest request, String headerName) {
		if (headerName == null || headerName.isEmpty()) {
			return false;
		}

		boolean hasHeader = false;

		Enumeration<String> headerNamesEnum = request.getHeaderNames();

		while (!hasHeader && headerNamesEnum.hasMoreElements()) {
			hasHeader = headerNamesEnum.nextElement().equalsIgnoreCase(headerName);
		}

		return hasHeader;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public String accessToken(HttpServletRequest request) {

		String authHeader = request.getHeader(Constants.DEFAULT_AUTHORIZATION_HEADER);
		String authz = null;

		if (authHeader != null) {
			StringTokenizer stringTokenizer = new StringTokenizer(authHeader, " ");
			if (stringTokenizer.countTokens() < 2) {
				LOGGER.warn("Invalid authorisation header");
			} else {
				String tokenType = stringTokenizer.nextToken();
				authz = stringTokenizer.nextToken();
			}
		}

		return authz;
	}
}
