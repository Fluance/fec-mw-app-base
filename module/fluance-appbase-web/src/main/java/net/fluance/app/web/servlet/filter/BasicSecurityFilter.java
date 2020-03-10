/**
 * 
 */
package net.fluance.app.web.servlet.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.security.service.entitlement.IEntitlementService;
import net.fluance.app.security.service.support.entitlement.EntitlementDecision;
import net.fluance.app.spring.util.Constants;
import net.fluance.commons.net.HttpUtils;

@Component
public class BasicSecurityFilter extends AbstractSecurityFilter {

	@Autowired
	private IEntitlementService entitlementService;
	@Value("${token.validate.url}")
	private String validateTokenUrl;
	@Value("${identity.domains.default}")
	private String defaultDomain;
	
	/**
	 * 
	 * @param authorizationHeaderValue
	 *            The value of the Authorization header
	 * @return
	 * @throws Exception
	 */
	@Override
	protected User authorizedUser(Object authorizationHeaderValue) throws Exception {
		StringTokenizer stringTokenizer = new StringTokenizer((String) authorizationHeaderValue, " ");
		String tokenType = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : null;
		String accessToken = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : null;
		User user = null;
		getLogger().debug("Validating the authorization token ... " + authorizationHeaderValue.toString());
		CloseableHttpResponse authorizationResponse = sendRequest(validateTokenUrl + "=" + accessToken);
		if (authorizationResponse.getStatusLine().getStatusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			user = mapper.readValue(EntityUtils.toString(authorizationResponse.getEntity()), User.class);
			getLogger().info("User " + user.getDomain() + "/" + user.getUsername() + "is authorized.");
			return user;
		} else {
			getLogger().warn("User not authorized.");
			return null;
		}
	}

	@Override
	protected boolean isAllowed(User authzUser, Object resource, Object action, Object... extraArgs) throws Exception {
		boolean isAllowed = false;

		if (authzUser == null || authzUser.getUsername().isEmpty()) {
			isAllowed = false;
		} else {

			isAllowed = isPermitted(authzUser, (String) resource, (String) action);

			getLogger().info(authzUser.getUsername() + "/" + authzUser.getDomain() + " is " + ((isAllowed) ? "" : "not") + " allowed to perform a " + action + " request at " + resource);
		}

		return isAllowed;
	}

	/**
	 * Checks against the PDP if the user bearing the access token is allowed to access the resource
	 * 
	 * @param request
	 * @return
	 * @throws KeyManagementException
	 * @throws NumberFormatException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws XPathExpressionException
	 * @throws HttpException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws URISyntaxException
	 */
	protected boolean isPermitted(User authzUser, String resource, String action) throws Exception {
		boolean isPermitted = false;
		if (authzUser == null || authzUser.getUsername().isEmpty()) {
			isPermitted = false;
		} else {
			getLogger().info("Attempt from " + authzUser.getUsername() + " to perform perform " + action + " request at " + resource);
			String domainKey = (authzUser.getDomain() != null) ? authzUser.getDomain() : defaultDomain;
			EntitlementDecision decision = entitlementService.getDecision(resource, authzUser.getUsername(), domainKey, action.toUpperCase());
			isPermitted = EntitlementDecision.PERMIT.equals(decision);
			getLogger().info(authzUser.getUsername() + " in domain " + authzUser.getDomain() + " is " + ((isPermitted) ? "" : " not") + " permitted to perform a " + action + " request at " + resource);
		}
		return isPermitted;
	}

	@Override
	public Logger getLogger() {
		return LogManager.getLogger();
	}

	/**
	 * Send a real http requestusing the complete URL and the access token
	 * 
	 * @param fullUri
	 * @return CloseableHttpResponse
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws HttpException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	protected CloseableHttpResponse sendRequest(String fullUri) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException {
		URI uri = HttpUtils.buildUri(fullUri);
		HttpGet get = HttpUtils.buildGet(uri, null);
		CloseableHttpResponse response = HttpUtils.sendGet(get, true);
		return response;
	}

}
