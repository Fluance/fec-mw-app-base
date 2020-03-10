/**
 * 
 */
package net.fluance.app.spring.util;

public class Constants {

	// ---------------------------- Preferences ---------------------------------------------------------
	public static final String DEFAULT_LANGUAGE = "en";
	
	// ---------------------------- WSO2 ----------------------------------------------------------------
	
	// The namespace URI for WSO2 IS user management service
	public static final String WSO2_IS_USERMGT_SERVICE_NAMESPACE_URI = "http://service.ws.um.carbon.wso2.org";
	
	// The namespace prefix for WSO2 IS user management service
	public static final String WSO2_IS_USERMGT_SERVICE_NAMESPACE_PREFIX = "ser";
	
	public static final String WSO2_ISEXISTINGUSER_RESPONSE_XPATH_EXPRESSION = "//*[local-name()='return']";
	
	// ---------------------------- Custom headers --------------------------------------------------------
	
	public static final String CLIENT_AUTHORISATION_HEADER = "Client-Authorization";
	public static final String DEFAULT_AUTHORIZATION_HEADER = "Authorization";
	
	
	//-------------------------------------- Messages -----------------------------------------------------
	public static final String DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
}
