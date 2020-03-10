/**
 * 
 */
package net.fluance.app.test.mock.controller;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

public abstract class AbstractAuthorizationServerMockController {
	
	/* Some prefixes and suffixes that can be used in tests*/
	public static final String DEFAULT_FIRSTNAME_PREFIX = "First Name";
	public static final String DEFAULT_LASTNAME_PREFIX = "Last Name";
	public static final String DEFAULT_GENDER_PREFIX = "Gender";
	public static final String DEFAULT_EMAIL_PREFIX = "Email";
	
	public static final String DEFAULT_ADDRESSLINE_PREFIX = "Address Street Number";
	public static final String DEFAULT_POSTALCODE_PREFIX = "Postal Code";
	public static final String DEFAULT_LOCALITY_PREFIX = "Locality";
	public static final String DEFAULT_STATEORPROVINCE_PREFIX = "State";
	public static final String DEFAULT_COUNTRY_PREFIX = "Country";
	
	public static final String DEFAULT_LASTNAME_SUFFIX = "Last Name";
	public static final String DEFAULT_GENDER_SUFFIX = "Gender";
	public static final String DEFAULT_EMAIL_DOMAIN = "domain.tld";
	public static final String DEFAULT_FIRSTNAME_SUFFIX = "First Name";

	public static final String DEFAULT_ADDRESSLINE_SUFFIX = "Address Street Number";
	public static final String DEFAULT_POSTALCODE_SUFFIX = "Postal Code";
	public static final String DEFAULT_LOCALITY_SUFFIX = "Locality";
	public static final String DEFAULT_STATEORPROVINCE_SUFFIX = "State";
	public static final String DEFAULT_COUNTRY_SUFFIX = "Country";
	
	public static final String DEFAULT_TELEPHONE = "+0 12 345 67 89";
	public static final String DEFAULT_MOBILE = "+9 87 654 32 10";
	
	public abstract String issueAccessToken(@RequestParam(required=false) String username, @
			RequestParam(required=false) String domainName);
	
	public abstract String issueAccessToken(@RequestBody(required=false) String bodyContent);
	
	public abstract String validateAccessToken(@RequestBody String bodyContent) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException;

	public abstract String revokeAccessToken(@RequestBody String bodyContent);
	
	public abstract String isExistingUser(@RequestBody String bodyContent) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException;

	public abstract String getUserClaimValuesForClaims(@RequestBody String bodyContent, HttpServletResponse response) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException;
	
	public abstract ResponseEntity<?> scimUserProfile(@RequestParam String filter, HttpServletRequest request);
	
	public abstract String userAdmin(@RequestBody String bodyContent, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException;
}