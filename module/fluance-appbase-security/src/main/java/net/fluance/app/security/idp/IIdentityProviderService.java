/**
 * 
 */
package net.fluance.app.security.idp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.xml.sax.SAXException;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserClaims;
import net.fluance.app.data.model.identity.UserReference;

public interface IIdentityProviderService {

	// -------------------------- CHECK
	// -------------------------------------------------
	/**
	 * Checks if a user exists. This method is not aimed at being directly
	 * exposed to clients, since the domain is calculated based on the related
	 * information received from the client. Example: client send 'local' as
	 * domain, but we don't request an IS with 'local'
	 * 
	 * @param username
	 * @param domain
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	boolean isExistingUser(String username, String domain) throws Exception;
	
	/**
	 * Tells if this identity provider supports SCIM protocol
	 * @return
	 */
	boolean isScimProtocolSupported();
	
	String scimProfile(String username, String domainName) throws Exception;

	/**
	 * Get user claims
	 * @param username
	 * @param domain
	 * @param claims
	 * @return
	 * @throws Exception
	 */
	Map<UserClaims, Object> userClaims(String username, String domain, Map<UserClaims, String> claims) throws Exception;
	
	/**
	 * Get user claims whith the requested encoding
	 * @param username
	 * @param domain
	 * @param claims
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	Map<UserClaims, Object> userClaims(String username, String domain, Map<UserClaims, String> claims, String encoding) throws Exception;
	
	User getUserInfos(User user);
	
	UserReference getUserReference(String managerUserName) throws Exception;
}
