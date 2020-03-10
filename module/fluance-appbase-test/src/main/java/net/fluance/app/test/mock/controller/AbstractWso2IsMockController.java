/**
 * 
 */
package net.fluance.app.test.mock.controller;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import net.fluance.app.test.mock.Address;
import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.app.test.mock.Email;
import net.fluance.app.test.mock.Name;
import net.fluance.app.test.mock.OAuth2Token;
import net.fluance.app.test.mock.OAuth2ValidationResponse;
import net.fluance.app.test.mock.Resource;
import net.fluance.app.test.mock.ScimProfile;
import net.fluance.app.test.mock.Telephon;
import net.fluance.app.test.mock.User;
import net.fluance.commons.lang.CollectionUtils;
import net.fluance.commons.xml.XMLUtils;

public abstract class AbstractWso2IsMockController extends AbstractAuthorizationServerMockController {

	protected static final Map<String, String> DOMAIN_MAPPINGS = new HashMap<String, String>();
	protected static final Logger LOGGER = LogManager.getLogger(AbstractWso2IsMockController.class);
	@Autowired
	protected AuthorizationServerMock authorizationServerMock;
	protected static final String ACCESS_TOKEN_VALIDATION_REQUEST_XPATH = "/Envelope/Body/validate/validationReqDTO/accessToken/identifier";
	protected static final String USER_EXISTENCE_REQUEST_XPATH = "/Envelope/Body/isExistingUser/userName";
	protected static final String USER_CLAIMS_USERNAME_REQUEST_XPATH = "/Envelope/Body/getUserClaimValuesForClaims/userName";
	protected static final String USER_CLAIMS_CLAIMS_REQUEST_XPATH = "/Envelope/Body/getUserClaimValuesForClaims//claims";
	protected static final String USER_ROLES_REQUEST_XPATH = "/Envelope/Body/getRolesOfUser/userName";
	protected static final String UPDATE_ROLES_USER_REQUEST_XPATH = "/Envelope/Body/addRemoveRolesOfUser/userName";
	protected static final String UPDATE_ROLES_DELETED_REQUEST_XPATH = "/Envelope/Body/addRemoveRolesOfUser/deletedRoles";
	protected static final String UPDATE_ROLES_NEW_REQUEST_XPATH = "/Envelope/Body/addRemoveRolesOfUser/newRoles";
	public static final String FILTER_PARAM_NAME = "filter";
	protected static final Map<String, String> FILTERS = new HashMap<>();
	
	protected static final String PASSWORD_GRANT_TYPE = "password";
	protected static final String DEFAULT_DOMAIN = "PRIMARY";
	
	public static String WRONG_USER_FOR_CLAIMS_RESPONSE_BODY = "";
	
	static {
		DOMAIN_MAPPINGS.put("local", "PRIMARY");
		DOMAIN_MAPPINGS.put("fluance", "PRIMARY");
		
		FILTERS.put("username.equals", "userNameEq");
		
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "<Fault>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "	<faultcode>soapenv:Server</faultcode>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "	<faultstring>Unprocessed Continuation Reference(s)</faultstring>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "	<detail>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "		<RemoteUserStoreManagerServiceUserStoreException>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "			<UserStoreException/>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "		</RemoteUserStoreManagerServiceUserStoreException>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "	</detail>";
		WRONG_USER_FOR_CLAIMS_RESPONSE_BODY += "</Fault>";
	}
	
	@Override
	@RequestMapping(value = "/wso2/scim/Users", method = RequestMethod.GET)
	public ResponseEntity<?> scimUserProfile(@RequestParam String filter, HttpServletRequest request) {
		String filterParam = request.getParameter(FILTER_PARAM_NAME);
		String filterParamKey = filterParamKey(filterParam);
		String filterParamValue = filterValue(filterParamKey, filterParam);
		ScimProfile scimProfile = null;
		
		if(FILTERS.get("username.equals").equals(filterParamKey)) {
			String domainName = domainFromFQN(filterParamValue);
			LOGGER.info("Domain name: " + domainName);
			String username = usernameFromFQN(filterParamValue);
			LOGGER.info("Username: " + username);
			User user = authorizationServerMock.getUserByUsernameAndDomain(username, domainName);
			
			if(user != null) {
				scimProfile = buildScimProfile(username, domainName);
			}
		}
		
		return new ResponseEntity<ScimProfile>(scimProfile, HttpStatus.OK);
	}

	@RequestMapping(value = "/services/OAuth2TokenValidationService", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = MediaType.ALL_VALUE)
	public String validateAccessToken(@RequestBody String bodyContent)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, javax.security.cert.CertificateException {
		LOGGER.info("Validation request received: " + bodyContent);

		OAuth2ValidationResponse response = null;

		Node contentDoc = XMLUtils.xmlStringToDocument(bodyContent).getDocumentElement();
		String accessToken = XMLUtils.queryString(contentDoc, ACCESS_TOKEN_VALIDATION_REQUEST_XPATH);

		LOGGER.info("Access token: " + accessToken);

		if (accessToken != null) {
			response = authorizationServerMock.validateOAuth2AccessToken(accessToken);
			LOGGER.info("Validation result for access token " + accessToken + ": " + response);
		}

		String validationXmlResponse = "<Envelope>";
		validationXmlResponse += "<Body>";
		validationXmlResponse += "<validateResponse>";
		validationXmlResponse += "<valid>" + Boolean.toString(response.isValid()) + "</valid>";
		validationXmlResponse += "<authorizedUser>" + response.getAuthzUser() + "</authorizedUser>";
		validationXmlResponse += "</validateResponse>";
		validationXmlResponse += "</Body>";
		validationXmlResponse += "</Envelope>";

		return validationXmlResponse;
	}

	@Override
	@RequestMapping(value = "/oauth2/token", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String issueAccessToken(@RequestBody(required = false) String bodyContent) {
		StringTokenizer stringTokenizer = new StringTokenizer(bodyContent, "&");
		String username = null;
		String password = null;
		String grantType = null;
		
		while (stringTokenizer.hasMoreTokens()) {
			String nextToken = stringTokenizer.nextToken();
			if(nextToken.startsWith("grant_type") && nextToken.split("=").length == 2) {
				grantType = nextToken.split("=")[1];
				switch (grantType) {
				case PASSWORD_GRANT_TYPE:
					nextToken = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
					if(nextToken.startsWith("username") && nextToken.split("=").length == 2) {
						username = nextToken.split("=")[1];
					} else if(nextToken.startsWith("password") && nextToken.split("=").length == 2) {
						password = nextToken.split("=")[1];
					}
					nextToken = (stringTokenizer.hasMoreTokens()) ? stringTokenizer.nextToken() : "";
					if(nextToken.startsWith("username") && nextToken.split("=").length == 2) {
						username = nextToken.split("=")[1];
					} else if(nextToken.startsWith("password") && nextToken.split("=").length == 2) {
						password = nextToken.split("=")[1];
					}
					break;

				default:
					break;
				}
			}
		}
		
		if (username != null) {
			return authorizationServerMock.issueUserAccessToken(username, DEFAULT_DOMAIN).toString();
		}
		
		return null;
	}

	@Override
	@RequestMapping(value = "/oauth2/token", method = RequestMethod.GET)
	public String issueAccessToken(@RequestParam String username, @RequestParam String domain) {
		if (username != null && domain != null) {
			return authorizationServerMock.issueUserAccessToken(username, domain).getAccessToken();
		}
		return null;
	}

	public OAuth2Token obtainToken(String username, String domain) {
		return authorizationServerMock.issueUserAccessToken(username, domain);
	}

	@Override
	@RequestMapping(value = "/oauth2/revoke", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String revokeAccessToken(String bodyContent) {
		return null;
	}

	/**
	 * @return the authorizationServerMock
	 */
	public AuthorizationServerMock getAuthorizationServerMock() {
		return authorizationServerMock;
	}

	@Override
	@RequestMapping(value = "/services/RemoteUserStoreManagerService/isExistingUser", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	public String isExistingUser(@RequestBody String bodyContent)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		LOGGER.info("User existence check request received: " + bodyContent);

		User user = null;

		Node contentDoc = XMLUtils.xmlStringToDocument(bodyContent).getDocumentElement();
		String fullyQualifiedUsername = XMLUtils.queryString(contentDoc, USER_EXISTENCE_REQUEST_XPATH);

		LOGGER.info("Requested username : " + fullyQualifiedUsername);

		if (fullyQualifiedUsername != null) {
			String username = null;
			String domainName = null;

			if (fullyQualifiedUsername.contains("/")) {
				domainName = domainFromFQN(fullyQualifiedUsername);
				LOGGER.info("Domain name: " + domainName);
				username = usernameFromFQN(fullyQualifiedUsername);
				LOGGER.info("Username: " + username);
			}

			user = authorizationServerMock.getUserByUsernameAndDomain(username, domainName);
			LOGGER.info("Found user: " + ((user != null) ? "Yes" : "No"));
		}

		String response = "<Envelope>";
		response += "<Body>";
		response += "<isUserExisting>";
		response += "<return>" + ((user != null) ? "true" : "false") + "</return>";
		response += "</isUserExisting>";
		response += "</Body>";
		response += "</Envelope>";

		LOGGER.info("Returning Is Existing User Response: " + response);

		return response;
	}
	
	@Override
	@RequestMapping(value = "/services/RemoteUserStoreManagerService/getUserClaimValuesForClaims", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	public String getUserClaimValuesForClaims(@RequestBody String bodyContent, HttpServletResponse response)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		LOGGER.info("User claims request received: " + bodyContent);

		User user = null;

		Node contentDoc = XMLUtils.xmlStringToDocument(bodyContent).getDocumentElement();
		String fullyQualifiedUsername = XMLUtils.queryString(contentDoc, USER_CLAIMS_USERNAME_REQUEST_XPATH);

		LOGGER.info("Requested username : " + fullyQualifiedUsername);

		if (fullyQualifiedUsername != null) {
			String username = null;
			String domainName = null;

			if (fullyQualifiedUsername.contains("/")) {
				domainName = domainFromFQN(fullyQualifiedUsername);
				LOGGER.info("Domain name: " + domainName);
				username = usernameFromFQN(fullyQualifiedUsername);
				LOGGER.info("Username: " + username);
			}

			user = authorizationServerMock.getUserByUsernameAndDomain(username, domainName);
			LOGGER.info("Found user: " + ((user != null) ? "Yes" : "No"));
		}

		
		String responseContent = "<Envelope>";
		responseContent += "<Body>";
		
		if(user == null) {
			responseContent += WRONG_USER_FOR_CLAIMS_RESPONSE_BODY;
			response.setStatus(500);
		} else {
			responseContent += "<getUserClaimValuesForClaimsResponse>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/telephone</claimURI>";
			responseContent += "		<value>" + DEFAULT_TELEPHONE + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/mobile</claimURI>";
			responseContent += "		<value>" + DEFAULT_MOBILE + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/lastname</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_LASTNAME_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/streetaddress</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_ADDRESSLINE_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/postalcode</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_POSTALCODE_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/locality</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_LOCALITY_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/stateorprovince</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_STATEORPROVINCE_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/country</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_COUNTRY_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/emailaddress</claimURI>";
			responseContent += "		<value>" + user.getUsername() + "@" + DEFAULT_EMAIL_DOMAIN + "</value>";
			responseContent += "	</return>";
			responseContent += "	<return>";
			responseContent += "		<claimURI>http://wso2.org/claims/givenname</claimURI>";
			responseContent += "		<value>" + user.getUsername() + " " + DEFAULT_FIRSTNAME_SUFFIX + "</value>";
			responseContent += "	</return>";
			responseContent += "</getUserClaimValuesForClaimsResponse>";
		}
		responseContent += "</Body>";
		responseContent += "</Envelope>";

		LOGGER.info("Returning User claims Response: " + responseContent);

		return responseContent;
	}

	@Override
	@RequestMapping(value = "/services/UserAdmin", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = MediaType.ALL_VALUE)
	public String userAdmin(@RequestBody String bodyContent, HttpServletRequest request)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		LOGGER.info("User admin request received: " + bodyContent);

		String soapAction = request.getHeader("SOAPAction");
		LOGGER.info("SOAP action : " + soapAction);

		String method = request.getMethod();
		LOGGER.info("Request method : " + method);

		String response = "Unknown action: " + soapAction;

		switch (soapAction) {
		case "getRolesOfUser":
			response = userRoles(bodyContent);
			break;
		case "addRemoveRolesOfUser":
			boolean ok = addRemoveUserRoles(bodyContent);
			response = ok ? "Successfully updated roles" : "Could not update roles";
			break;
		case "getUserRealmInfo":
			response = availableUserStoresStr();
			break;
		default:
			break;
		}

		LOGGER.info("Returning user roles update Response: " + response);

		return response;
	}
	
	private String availableUserStoresStr() {
		String availableUserStoresStr = "";
		availableUserStoresStr += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		availableUserStoresStr += "		<soapenv:Body>";
		availableUserStoresStr += "      <ns:getUserRealmInfoResponse xmlns:ns=\"http://org.apache.axis2/xsd\">";
		availableUserStoresStr += "         <ns:return xsi:type=\"ax2638:UserRealmInfo\" xmlns:ax2638=\"http://common.mgt.user.carbon.wso2.org/xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
		availableUserStoresStr += "            <ax2638:adminRole>admin</ax2638:adminRole>";
		availableUserStoresStr += "            <ax2638:adminUser>admin</ax2638:adminUser>";
		availableUserStoresStr += "            <ax2638:bulkImportSupported>false</ax2638:bulkImportSupported>";
		availableUserStoresStr += "            <ax2638:domainNames>PRIMARY</ax2638:domainNames>";
		availableUserStoresStr += "            <ax2638:domainNames>FLUANCE</ax2638:domainNames>";
		availableUserStoresStr += "            <ax2638:enableUIPageCache>true</ax2638:enableUIPageCache>";
		availableUserStoresStr += "            <ax2638:everyOneRole>Internal/everyone</ax2638:everyOneRole>";
		availableUserStoresStr += "            <ax2638:maxItemsPerUIPage>15</ax2638:maxItemsPerUIPage>";
		availableUserStoresStr += "            <ax2638:maxUIPagesInCache>6</ax2638:maxUIPagesInCache>";
		availableUserStoresStr += "            <ax2638:multipleUserStore>true</ax2638:multipleUserStore>";
		availableUserStoresStr += "            <ax2638:primaryUserStoreInfo xsi:type=\"ax2638:UserStoreInfo\">";
		availableUserStoresStr += "					   <ax2638:bulkImportSupported>true</ax2638:bulkImportSupported>";
		availableUserStoresStr += "		               <ax2638:domainName>PRIMARY</ax2638:domainName>";
		availableUserStoresStr += "		               <ax2638:externalIdP xsi:nil=\"true\"/>";
		availableUserStoresStr += "		               <ax2638:maxRoleLimit>0</ax2638:maxRoleLimit>";
		availableUserStoresStr += "		               <ax2638:maxUserLimit>0</ax2638:maxUserLimit>";
		availableUserStoresStr += "		               <ax2638:passwordRegEx>^[\\S]{5,30}$</ax2638:passwordRegEx>";
		availableUserStoresStr += "		               <ax2638:passwordsExternallyManaged>false</ax2638:passwordsExternallyManaged>";
		availableUserStoresStr += "		               <ax2638:readOnly>false</ax2638:readOnly>";
		availableUserStoresStr += "		               <ax2638:roleNameRegEx>^[\\S]{3,60}$</ax2638:roleNameRegEx>";
		availableUserStoresStr += "		               <ax2638:userNameRegEx>^[\\S]{3,100}$</ax2638:userNameRegEx>";
		availableUserStoresStr += "            </ax2638:primaryUserStoreInfo>";
		availableUserStoresStr += "            <ax2638:requiredUserClaims>http://wso2.org/claims/givenname</ax2638:requiredUserClaims>";
		availableUserStoresStr += "            <ax2638:requiredUserClaims>http://wso2.org/claims/emailaddress</ax2638:requiredUserClaims>";
		availableUserStoresStr += "            <ax2638:requiredUserClaims>http://wso2.org/claims/lastname</ax2638:requiredUserClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/otherphone</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/dob</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/primaryChallengeQuestion</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/role</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/challengeQuestion1</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/telephone</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/mobile</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/country</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/challengeQuestionUris</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/postalcode</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/challengeQuestion2</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/identity/accountLocked</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/nickname</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/streetaddress</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/url</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/givenname</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/emailaddress</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/oneTimePassword</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/region</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/gender</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/fullname</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/passwordTimestamp</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/title</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/locality</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/stateorprovince</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/im</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/organization</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userClaims>http://wso2.org/claims/lastname</ax2638:userClaims>";
		availableUserStoresStr += "            <ax2638:userStoresInfo xsi:type=\"ax2638:UserStoreInfo\">";
		availableUserStoresStr += "		               <ax2638:bulkImportSupported>true</ax2638:bulkImportSupported>";
		availableUserStoresStr += "		               <ax2638:domainName>PRIMARY</ax2638:domainName>";
		availableUserStoresStr += "		               <ax2638:externalIdP xsi:nil=\"true\"/>";
		availableUserStoresStr += "		               <ax2638:maxRoleLimit>0</ax2638:maxRoleLimit>";
		availableUserStoresStr += "		               <ax2638:maxUserLimit>0</ax2638:maxUserLimit>";
		availableUserStoresStr += "		               <ax2638:passwordRegEx>^[\\S]{5,30}$</ax2638:passwordRegEx>";
		availableUserStoresStr += "		               <ax2638:passwordsExternallyManaged>false</ax2638:passwordsExternallyManaged>";
		availableUserStoresStr += "		               <ax2638:readOnly>false</ax2638:readOnly>";
		availableUserStoresStr += "		               <ax2638:roleNameRegEx>^[\\S]{3,60}$</ax2638:roleNameRegEx>";
		availableUserStoresStr += "		               <ax2638:userNameRegEx>^[\\S]{3,100}$</ax2638:userNameRegEx>";
		availableUserStoresStr += "            </ax2638:userStoresInfo>";
		availableUserStoresStr += "            <ax2638:userStoresInfo xsi:type=\"ax2638:UserStoreInfo\">";
		availableUserStoresStr += "		               <ax2638:bulkImportSupported>true</ax2638:bulkImportSupported>";
		availableUserStoresStr += "		               <ax2638:domainName>FLUANCE</ax2638:domainName>";
		availableUserStoresStr += "		               <ax2638:externalIdP xsi:nil=\"true\"/>";
		availableUserStoresStr += "		               <ax2638:maxRoleLimit>0</ax2638:maxRoleLimit>";
		availableUserStoresStr += "		               <ax2638:maxUserLimit>0</ax2638:maxUserLimit>";
		availableUserStoresStr += "		               <ax2638:passwordRegEx xsi:nil=\"true\"/>";
		availableUserStoresStr += "		               <ax2638:passwordsExternallyManaged>false</ax2638:passwordsExternallyManaged>";
		availableUserStoresStr += "		               <ax2638:readOnly>true</ax2638:readOnly>";
		availableUserStoresStr += "		               <ax2638:roleNameRegEx xsi:nil=\"true\"/>";
		availableUserStoresStr += "		               <ax2638:userNameRegEx xsi:nil=\"true\"/>";
		availableUserStoresStr += "            </ax2638:userStoresInfo>";
		availableUserStoresStr += "			</ns:return>";
		availableUserStoresStr += "</ns:getUserRealmInfoResponse>";
		availableUserStoresStr += "	</soapenv:Body>";
		availableUserStoresStr += "</soapenv:Envelope>";
		return availableUserStoresStr;
	}

	/**
	 * 
	 * @param bodyContent
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	private String userRoles(String bodyContent)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		List<String> roles = new ArrayList<>();

		Node contentDoc = XMLUtils.xmlStringToDocument(bodyContent).getDocumentElement();
		String fullyQualifiedUsername = XMLUtils.queryString(contentDoc, USER_ROLES_REQUEST_XPATH);

		LOGGER.info("Requested username : " + fullyQualifiedUsername);

		if (fullyQualifiedUsername != null) {
			String username = null;
			String domainName = null;

			if (fullyQualifiedUsername.contains("/")) {
				domainName = domainFromFQN(fullyQualifiedUsername);
				LOGGER.info("Domain name: " + domainName);
				username = usernameFromFQN(fullyQualifiedUsername);
				LOGGER.info("Username: " + username);
			}

			roles = authorizationServerMock.userRoles(username, domainName);
			LOGGER.info(fullyQualifiedUsername + " roles : " + roles);
		}

		String response = "<Envelope>";
		response += "<Body>";
		response += "<getRolesOfUserResponse>";
		for (String role : roles) {
			response += "<return>";
			response += "<itemName>" + role + "</itemName>";
			response += "<selected>true</selected>";
			response += "</return>";
		}
		response += "</getRolesOfUserResponse>";
		response += "</Body>";
		response += "</Envelope>";

		return response;
	}

	/**
	 * 
	 * @param bodyContent
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	private boolean addRemoveUserRoles(String bodyContent)
			throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		
		boolean ok = false;
		User user = null;

		Node contentDoc = XMLUtils.xmlStringToDocument(bodyContent).getDocumentElement();
		String fullyQualifiedUsername = XMLUtils.queryString(contentDoc, UPDATE_ROLES_USER_REQUEST_XPATH);

		LOGGER.info("Requested username : " + fullyQualifiedUsername);

		String username = null;
		String domainName = null;
		if (fullyQualifiedUsername != null) {

			if (fullyQualifiedUsername.contains("/")) {
				domainName = domainFromFQN(fullyQualifiedUsername);
				LOGGER.info("Domain name: " + domainName);
				username = usernameFromFQN(fullyQualifiedUsername);
				LOGGER.info("Username: " + username);
			}

			user = authorizationServerMock.getUserByUsernameAndDomain(username, domainName);
			LOGGER.info("Found user: " + ((user != null) ? "Yes" : "No"));
		}

		List<String> currentRoles = authorizationServerMock.userRoles(username, domainName);

		List<String> deletedRoles = XMLUtils.queryNodesTextContentList(contentDoc, UPDATE_ROLES_NEW_REQUEST_XPATH);
		if(deletedRoles != null) {
			for(String role : deletedRoles) {
				authorizationServerMock.revokeRole(username, domainName, role);
			}
		}
		List<String> newRoles = XMLUtils.queryNodesTextContentList(contentDoc, UPDATE_ROLES_NEW_REQUEST_XPATH);
		if(newRoles != null) {
			for(String role : newRoles) {
				authorizationServerMock.assignRole(username, domainName, role);
			}
		}
		
		currentRoles = authorizationServerMock.userRoles(username, domainName);
		
		ok = CollectionUtils.containsNoneOf(currentRoles, deletedRoles) && currentRoles.containsAll(newRoles);
		
		return ok;
	}

	protected String usernameFromFQN(String fullyQualifiedUsername) {
		if (fullyQualifiedUsername == null || !fullyQualifiedUsername.contains("/")) {
			return fullyQualifiedUsername;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(fullyQualifiedUsername, "/");
		if (stringTokenizer.countTokens() != 2) {
			throw new IllegalArgumentException(fullyQualifiedUsername + " is not a valid (fully qualified) username");
		}
		@SuppressWarnings("unused")
		String domain = stringTokenizer.nextToken();
		String username = stringTokenizer.nextToken();
		return username;
	}

	/**
	 * 
	 * @param fullyQualifiedUsername
	 * @return
	 */
	protected String domainFromFQN(String fullyQualifiedUsername) {
		if (fullyQualifiedUsername == null || !fullyQualifiedUsername.contains("/")) {
			return null;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(fullyQualifiedUsername, "/");
		if (stringTokenizer.countTokens() != 2) {
			throw new IllegalArgumentException(fullyQualifiedUsername + " is not a valid (fully qualified) username");
		}
		String domain = stringTokenizer.nextToken();
		return domain;
	}
	
	protected String filterParamKey(String filterParam) {
		String filterParamKey = null;
		
		Iterator<String> filterKeyIterator = FILTERS.keySet().iterator();
		
		while (filterParamKey==null && filterKeyIterator.hasNext()) {
			String currentKey = FILTERS.get(filterKeyIterator.next());
			if(filterParam.startsWith(currentKey)) {
				filterParamKey = currentKey;
			}
		}
		
		return filterParamKey;
	}
	
	/**
	 * 
	 * @param filterParam
	 * @return
	 */
	protected String filterValue(String filterParamKey, String filterParam) {
		String value = filterParam;
		
		value = filterParam.substring((filterParam.lastIndexOf(filterParamKey) + filterParamKey.length()));
		
		return value;
	}
	
	/**
	 * 
	 * @param username
	 * @param domainName
	 * @return
	 */
	protected ScimProfile buildScimProfile(String username, String domainName) {
		ScimProfile scimProfile = new ScimProfile();
		Resource resource = new Resource();
		resource.setDomain(domainName);
		resource.setScimId("xxxxxxxxxx-" + username + "-xxxxxxxxxx");
		resource.setUsername(username);
		
		Name name = new Name();
		name.setGivenName(username + "FirstName");
		name.setFamilyName(username + "LastName");
		resource.setName(name);
		
		Address address = new Address("Street 000", "9999999", "Locality", "State", "Country");
		List<Address> addresses = new ArrayList<>();
		addresses.add(address);
		resource.setAddresses(addresses);
		
		Email email = new Email("work", "address@work.tld");
		List<Email> emails = new ArrayList<>();
		emails.add(email);
		resource.setEmails(emails);
		
		Telephon telephon = new Telephon("work", "+00 11 222 33 44");
		List<Telephon> telephons = new ArrayList<>();
		telephons.add(telephon);
		resource.setTelephons(telephons);
		
		scimProfile.getResources().add(resource);
		
		return scimProfile;
	}
}
