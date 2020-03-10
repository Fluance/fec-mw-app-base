/**
 * 
 */
package net.fluance.app.web.servlet.filter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.spring.util.Constants;
import net.fluance.app.web.util.exceptions.UnknownIssuerException;
import net.fluance.app.web.wrapper.RequestWrapper;
import net.fluance.app.web.wrapper.ResponseWrapper;

public abstract class AbstractSecurityFilter extends AbstractFilter {

	@Value("${authorization.header}")
	protected String authorizationHeaderName;
	@Value("${identity.user.fully-qualified-name.separator}")
	protected String fullyQualifiedUsernameSeparator;
	@Value("${identity.user.fully-qualified-name.layout}")
	protected String fullyQualifiedUsernameLayout;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		RequestWrapper wrappedRequest = null;
		getLogger().info("Requested Url is " + request.getRequestURL().toString());
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			wrappedRequest = new RequestWrapper(request);
		} catch (Exception e1) {
			sendInternalServerError(response, "{\"message\":\"Internal server error: Could not process request\"}");
			return;
		}

		// Http OPTIONS must always be authorized
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			filterChain.doFilter(wrappedRequest, response);
			return;
		}

		ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
		responseWrapper.setContentType("application/json");

		// If a different authoriation header is defined, it is used. If not, then default ('Authorization') is used
		String authHeader = request.getHeader((authorizationHeaderName == null) ? Constants.DEFAULT_AUTHORIZATION_HEADER : authorizationHeaderName);
		getLogger().info("Received request for " + request.getRequestURL() + " with Authorization header: " + authHeader);
		if (authHeader == null) {
			addCORSHeaders(response);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(authHeader, " ");
			if (stringTokenizer.countTokens() < 2) {
				getLogger().warn("Invalid authorisation header " + authHeader);
			}

			User authorizedUser = (User) authorizedUser(authHeader);
			getLogger().info("Request for " + request.getRequestURL() + " - User: " + ((authorizedUser != null) ? (authorizedUser.getDomain() + "/" + authorizedUser.getUsername()) : ""));
			request.setAttribute(User.USER_KEY, authorizedUser);
			if (authorizedUser == null || authorizedUser.getUsername() == null) {
				addCORSHeaders(response);
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			String resourceUrl = request.getRequestURL().toString() + "/";
			while (resourceUrl.endsWith("/")) {
				resourceUrl = resourceUrl.substring(0, resourceUrl.length() - 1);
			}
			boolean allowed = isAllowed(authorizedUser, resourceUrl, request.getMethod().toUpperCase(), request);
			if (!allowed) {
				addCORSHeaders(responseWrapper);
				responseWrapper.setStatus(HttpServletResponse.SC_FORBIDDEN);
				responseWrapper.getWriter().write("{\"message\":\"This operation is not allowed\"}");
			} else {
				filterChain.doFilter(wrappedRequest, response);
				return;
			}

		} catch (UnknownIssuerException uie) {
			getLogger().warn("", uie);
			addCORSHeaders(responseWrapper);
			responseWrapper.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			responseWrapper.getWriter().write("{\"message\":\"" + uie.getMessage() + "\"}");
			return;
		} catch (Exception e) {
			getLogger().error("", e);
			sendInternalServerError(response, "{\"message\":\"Internal server error\"}");
			return;
		}
	}

	/**
	 * Validates the Authorization header and Returns the user information.
	 * 
	 * @param authorizationHeader
	 * @return
	 * @throws URISyntaxException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws HttpException
	 * @throws XPathExpressionException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws NumberFormatException
	 * @throws KeyManagementException
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	protected abstract User authorizedUser(Object authorizationHeader) throws Exception;

	/**
	 * Checks against the PDP if the user bearing the access token is allowed to access the resource
	 * 
	 * @param authzUSer
	 * @param resource
	 * @param action
	 * @param extraArgs
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean isAllowed(User authzUSer, Object resource, Object action, Object... extraArgs) throws Exception;

}
