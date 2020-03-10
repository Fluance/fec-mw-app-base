/**
 * 
 */
package net.fluance.app.security.util;

public class Constants {
	public static final String UNSECURED_HTTP_URISCHEME = "http";
	public static final String SECURED_HTTP_URISCHEME = "https";
	public static final String DEFAULT_SECURITY_URISCHEME = SECURED_HTTP_URISCHEME;
	
	public static final String BASIC_AUTHENTICATION_TYPE = "Basic";
	public static final String DIGEST_AUTHENTICATION_TYPE = "Digest";
	public static final String BEARER_AUTHENTICATION_TYPE = "Bearer";
	public static final String DEFAULT_AUTHENTICATION_TYPE = BASIC_AUTHENTICATION_TYPE;
	
	public static final String DEFAULT_AUTHORIZATION_HEADER = "Authorization";
	
	/**
	 * Layout name for fully qualified username
	 */
	public static final String FQUN_LAYOUT_USERNAME_FIRST = "username-first";
	/**
	 * Layout name for fully qualified username
	 */
	public static final String FQUN_LAYOUT_DOMAIN_FIRST = "domain-first";
	public static final String DEFAULT_FQUN_LAYOUT = FQUN_LAYOUT_DOMAIN_FIRST;
	
	public static final String DEFAULT_FULLYQUALIFIED_USERNAME_SEPARATOR = "/";
	
	public static final String EXTERNAL_CLIENT_JWT_PARAM = "externalClientJwt";
	public static final String FORWARD_PARAM = "fw";
	public static final String TOKEN_ATTRIBUTE_NAME = "token";
}
