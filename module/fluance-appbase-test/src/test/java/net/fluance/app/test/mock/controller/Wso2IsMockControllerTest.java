package net.fluance.app.test.mock.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.test.TestApplication;
import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.commons.io.StreamUtils;
import net.fluance.commons.net.ResponseUtils;
import net.fluance.commons.xml.XMLUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestApplication.class)
public class Wso2IsMockControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(Wso2IsMockControllerTest.class);
	private static final String OAUTH2_ISSUE_ACCESS_TOKEN_PATH = "/oauth2/token";
	@SuppressWarnings("unused")
	private static final String OAUTH2_REVOKE_ACCESS_TOKEN_PATH = "/oauth2/revoke";
	private static final String OAUTH2_VALIDATE_ACCESS_TOKEN_PATH = "/services/OAuth2TokenValidationService";
	private static final String IS_USER_EXISTING_PATH = "/services/RemoteUserStoreManagerService/isExistingUser";
	private static final String GET_USER_CLAIMS_PATH = "/services/RemoteUserStoreManagerService/getUserClaimValuesForClaims";
	private static final String USER_ADMIN_PATH = "/services/UserAdmin";
	private static final String SCIM_USERPROFILE_GET_PATH = "/wso2/scim/Users";

	public static final String WSO2_ISEXISTINGUSER_RESPONSE_XPATH_EXPRESSION = "//*[local-name()='return']";
	public static final String WSO2_CLAIMS_NODES_XPATH_EXPRESSION = "//*[local-name()='return']";
	public static final String USER_ROLES_RESPONSE_XPATH_EXPRESSION = "/Envelope/Body/getRolesOfUserResponse//return/itemName[../selected/text()=\"true\"]";

	private static final Integer DOMAIN1_ID = 1;
	private static final Integer DOMAIN2_ID = 2;
	private static final Integer DOMAIN3_ID = 3;
	private static final String DOMAIN1_NAME = "domain1.tld";
	private static final String DOMAIN2_NAME = "domain2.tld";
	private static final String DOMAIN3_NAME = "domain3.tld";
	private static final String USER1_NAME = "user1";
	private static final String USER2_NAME = "user2";
	private static final String USER3_NAME = "user3";
	private static final String ROLE1_NAME = "role1";
	private static final String ROLE2_NAME = "role2";
	@Autowired
	private Wso2IsMockController authServerMockController;

	@Before
	public void setUp() throws Exception {
		baseUrl = "http://localhost:" + serverPort;
		authServerMockController.getAuthorizationServerMock().addDomain(DOMAIN1_ID, DOMAIN1_NAME);
		authServerMockController.getAuthorizationServerMock().addDomain(DOMAIN2_ID, DOMAIN2_NAME);
		authServerMockController.getAuthorizationServerMock().addDomain(DOMAIN3_ID, DOMAIN3_NAME);
		authServerMockController.getAuthorizationServerMock().addRole(ROLE1_NAME);
		authServerMockController.getAuthorizationServerMock().addRole(ROLE2_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER1_NAME, UUID.randomUUID().toString(),
				DOMAIN1_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER1_NAME, UUID.randomUUID().toString(),
				DOMAIN3_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER2_NAME, UUID.randomUUID().toString(),
				DOMAIN2_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER3_NAME, UUID.randomUUID().toString(),
				DOMAIN1_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER3_NAME, UUID.randomUUID().toString(),
				DOMAIN2_NAME);
		authServerMockController.getAuthorizationServerMock().addUser(USER3_NAME, UUID.randomUUID().toString(),
				DOMAIN3_NAME);
		authServerMockController.getAuthorizationServerMock().assignRole(USER1_NAME, DOMAIN1_NAME, ROLE1_NAME);
		authServerMockController.getAuthorizationServerMock().assignRole(USER1_NAME, DOMAIN1_NAME, ROLE2_NAME);
	}

	@Test
	public void checkIssueAccessTokenGet() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", USER3_NAME));
		params.add(new BasicNameValuePair("domain", DOMAIN3_NAME));
		CloseableHttpResponse response = invokeResource(HttpMethod.GET.name(), OAUTH2_ISSUE_ACCESS_TOKEN_PATH, null,
				params, null, null, null);
		String token = EntityUtils.toString(response.getEntity());
		assertNotNull(token);
		String tokenFromObject = authServerMockController.getAuthorizationServerMock()
				.issueUserAccessToken(USER3_NAME, DOMAIN3_NAME).getAccessToken();
		assertEquals(tokenFromObject, token);
	}

	@Test
	public void checkValidateAccessToken() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ParserConfigurationException, SAXException, XPathExpressionException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", USER3_NAME));
		params.add(new BasicNameValuePair("domain", DOMAIN3_NAME));
		CloseableHttpResponse response = invokeResource(HttpMethod.GET.name(), OAUTH2_ISSUE_ACCESS_TOKEN_PATH, null,
				params, null, null, null);
		String accessToken = EntityUtils.toString(response.getEntity());
		String bodyContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://org.apache.axis2/xsd\" xmlns:xsd1=\"http://dto.oauth2.identity.carbon.wso2.org/xsd\">"
				+ "<soapenv:Header/>" + "<soapenv:Body>" + "<xsd:validate>" + "<xsd:validationReqDTO>"
				+ "<xsd1:accessToken>" + "<xsd1:identifier>" + accessToken + "</xsd1:identifier>" + "<xsd1:tokenType>"
				+ AuthorizationServerMock.DEFAULT_OAUTH2_USER_TOKEN_TYPE + "</xsd1:tokenType>" + "</xsd1:accessToken>"
				+ "</xsd:validationReqDTO>" + "</xsd:validate>" + "</soapenv:Body>" + "</soapenv:Envelope>";
		response = invokeResource(HttpMethod.POST.name(), OAUTH2_VALIDATE_ACCESS_TOKEN_PATH, null, null, bodyContent,
				null, null);

		HttpEntity responseEntity = response.getEntity();
		String responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		Node responseRoot = XMLUtils.xmlStringToDocument(responseXML).getDocumentElement();
		String xpathValidityExpression = "/Envelope/Body/validateResponse//valid";
		String validity = XMLUtils.queryString(responseRoot, xpathValidityExpression);
		boolean isValid = Boolean.parseBoolean(validity);
		assertTrue(isValid);

		String xpathUserExpression = "/Envelope/Body/validateResponse//authorizedUser";
		String fullyQualifiedUsername = XMLUtils.queryString(responseRoot, xpathUserExpression);
		assertNotNull(fullyQualifiedUsername);
		assertEquals(DOMAIN3_NAME + "/" + USER3_NAME, fullyQualifiedUsername);
	}

	@Test
	public void checkIsExistingUser() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ParserConfigurationException, SAXException, XPathExpressionException {
		// Check really existing user
		String bodyContent = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">"
				+ "	<soap:Header/>" + "	<soap:Body>" + "		<ser:isExistingUser>" + "			<ser:userName>"
				+ (DOMAIN1_NAME + "/" + USER1_NAME) + "</ser:userName>" + "		</ser:isExistingUser>"
				+ "	</soap:Body>" + "</soap:Envelope>";

		CloseableHttpResponse response = invokeResource(HttpMethod.POST.name(), IS_USER_EXISTING_PATH, null, null,
				bodyContent, ContentType.APPLICATION_XML, "utf-8");

		HttpEntity responseEntity = response.getEntity();
		String responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		Document responseDoc = XMLUtils.xmlStringToDocument(responseXML);
		Node reponseNode = XMLUtils.queryNode(responseDoc.getDocumentElement(),
				WSO2_ISEXISTINGUSER_RESPONSE_XPATH_EXPRESSION);
		boolean exists = Boolean.parseBoolean(reponseNode.getTextContent());

		assertTrue(exists);

		// Check non-existing user
		bodyContent = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">"
				+ "	<soap:Header/>" + "	<soap:Body>" + "		<ser:isExistingUser>"
				+ "			<ser:userName>FAKE_DOMAIN/fakeUsername</ser:userName>" + "		</ser:isExistingUser>"
				+ "	</soap:Body>" + "</soap:Envelope>";

		response = invokeResource(HttpMethod.POST.name(), IS_USER_EXISTING_PATH, null, null, bodyContent,
				ContentType.APPLICATION_XML, "utf-8");

		responseEntity = response.getEntity();
		responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		responseDoc = XMLUtils.xmlStringToDocument(responseXML);
		reponseNode = XMLUtils.queryNode(responseDoc.getDocumentElement(),
				WSO2_ISEXISTINGUSER_RESPONSE_XPATH_EXPRESSION);
		exists = Boolean.parseBoolean(reponseNode.getTextContent());

		assertFalse(exists);
	}

	@Test
	public void checkGetUserClaimValuesForClaims() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ParserConfigurationException, SAXException, XPathExpressionException {
		// Check really existing user
		String bodyContent = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">"
				+ "	<soap:Header/>" + "	<soap:Body>" + "		<ser:getUserClaimValuesForClaims>"
				+ "			<ser:userName>" + (DOMAIN1_NAME + "/" + USER1_NAME) + "</ser:userName>"
				+ "			<ser:claims>http://wso2.org/claims/givenname</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/lastname</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/telephone</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/mobile</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/emailaddress</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/streetaddress</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/postalcode</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/locality</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/stateorprovince</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/country</ser:claims>"
				+ "		</ser:getUserClaimValuesForClaims>" + "	</soap:Body>" + "</soap:Envelope>";

		CloseableHttpResponse response = invokeResource(HttpMethod.POST.name(), GET_USER_CLAIMS_PATH, null, null,
				bodyContent, ContentType.APPLICATION_XML, "utf-8");

		HttpEntity responseEntity = response.getEntity();
		String responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		if (HttpStatus.OK.value() == response.getStatusLine().getStatusCode()) {
			Document doc = XMLUtils.xmlStringToDocument(responseXML);
			NodeList claimNodes = XMLUtils.queryNodeSet(doc.getDocumentElement(), WSO2_CLAIMS_NODES_XPATH_EXPRESSION);
			assertEquals(10, claimNodes.getLength());
			
			for (int i = 0; i < claimNodes.getLength(); i++) {
				Node claimNode = claimNodes.item(i);

				String claimUri = XMLUtils.queryString(claimNode, "//claimURI");
				if (claimUri == null || claimUri.isEmpty()) {
					LOGGER.warn("Recieved claim with URI " + ((claimUri == null) ? "null" : "''") + " for user "
							+ USER1_NAME + " in domain " + DOMAIN1_NAME);
				}
				String claimValue = XMLUtils.queryString(claimNode, "//value");
				assertTrue(verifyClaimValue(USER1_NAME, claimUri, claimValue));
			}

		}

		// Check non-existing user
		bodyContent = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">"
				+ "	<soap:Header/>" + "	<soap:Body>" + "		<ser:getUserClaimValuesForClaims>"
				+ "			<ser:userName>" + DOMAIN1_NAME + "/fakeUsername</ser:userName>"
				+ "			<ser:claims>http://wso2.org/claims/givenname</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/lastname</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/telephone</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/mobile</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/emailaddress</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/streetaddress</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/postalcode</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/locality</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/stateorprovince</ser:claims>"
				+ "			<ser:claims>http://wso2.org/claims/country</ser:claims>"
				+ "		</ser:getUserClaimValuesForClaims>" + "	</soap:Body>" + "</soap:Envelope>";

		response = invokeResource(HttpMethod.POST.name(), GET_USER_CLAIMS_PATH, null, null, bodyContent,
				ContentType.APPLICATION_XML, "utf-8");

		responseEntity = response.getEntity();
		responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		assertEquals(500, response.getStatusLine().getStatusCode());
		assertEquals("<Envelope><Body>" + AbstractWso2IsMockController.WRONG_USER_FOR_CLAIMS_RESPONSE_BODY + "</Body></Envelope>", responseXML);
	}

	@Test
	public void checkFindRoles() throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException,
			KeyStoreException, IOException, HttpException, URISyntaxException, ParserConfigurationException,
			SAXException, XPathExpressionException {

		String bodyContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "	<soapenv:Body>" + "		<xsd:getRolesOfUser xmlns:xsd=\"http://org.apache.axis2/xsd\">"
				+ "			<xsd:userName>" + (DOMAIN1_NAME + "/" + USER1_NAME) + "</xsd:userName>"
				+ "			<xsd:limit>10</xsd:limit>" + "		</xsd:getRolesOfUser>" + "	</soapenv:Body>"
				+ "</soapenv:Envelope>";

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("SOAPAction", "getRolesOfUser"));
		CloseableHttpResponse response = invokeResource(HttpMethod.POST.name(), USER_ADMIN_PATH, headers, null,
				bodyContent, ContentType.APPLICATION_XML, "utf-8");

		HttpEntity responseEntity = response.getEntity();
		String responseXML = StreamUtils.inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);

		Document responseDoc = XMLUtils.xmlStringToDocument(responseXML);
		List<String> roles = XMLUtils.queryNodesTextContentList(responseDoc.getDocumentElement(),
				USER_ROLES_RESPONSE_XPATH_EXPRESSION);

		assertNotNull(roles);
		assertEquals(3, roles.size());
	}

	@Test
	public void checkUpdateRoles() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ParserConfigurationException, SAXException, XPathExpressionException {

		String bodyContent = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soapenv:Body>" + "<xsd:addRemoveRolesOfUser xmlns:xsd=\"http://org.apache.axis2/xsd\">"
				+ "<xsd:userName>" + (DOMAIN1_NAME + "/" + USER1_NAME) + "</xsd:userName>";
		bodyContent += "<xsd:newRoles>" + ROLE1_NAME + "</xsd:newRoles>";
		bodyContent += "<xsd:newRoles>" + ROLE2_NAME + "</xsd:newRoles>";
		bodyContent += "<xsd:deletedRoles>" + AuthorizationServerMock.DEFAULT_ROLE_NAME + "</xsd:deletedRoles>";
		bodyContent += "</xsd:addRemoveRolesOfUser>" + "</soapenv:Body>" + "</soapenv:Envelope>";

		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("SOAPAction", "addRemoveRolesOfUser"));
		CloseableHttpResponse response = invokeResource(HttpMethod.POST.name(), USER_ADMIN_PATH, headers, null,
				bodyContent, ContentType.APPLICATION_XML, "utf-8");

		assertEquals(200, response.getStatusLine().getStatusCode());
	}

	@Test
	public void checkGetScimProfile() throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException {
		List<NameValuePair> params = new ArrayList<>();
		String filterValue = "userNameEq" + DOMAIN1_NAME + "/" + USER1_NAME;
		;
		params.add(new BasicNameValuePair("filter", filterValue));

		CloseableHttpResponse response = invokeResource(HttpMethod.GET.name(), SCIM_USERPROFILE_GET_PATH, null, params,
				null, null, null);

		assertEquals(200, response.getStatusLine().getStatusCode());

		String scimProfile = ResponseUtils.stringEntity(response);
		JsonNode jsonProfile = new ObjectMapper().readTree(scimProfile);
		JsonNode jsonResources = jsonProfile.get("Resources");
		assertTrue(jsonResources.isArray());
		JsonNode jsonResource = jsonResources.get(0);
		assertTrue(jsonResource.has("scim_id"));
		assertTrue(jsonResource.get("scim_id").textValue() != null);
		assertTrue(jsonResource.has("domain"));
		assertTrue(jsonResource.get("domain").textValue() != null);
		assertTrue(jsonResource.has("username"));
		assertTrue(jsonResource.get("username").textValue() != null);
		assertTrue(jsonResource.has("addresses"));
		assertTrue(jsonResource.get("addresses").isArray());
		assertEquals(1, jsonResource.get("addresses").size());
		assertTrue(jsonResource.has("emails"));
		assertTrue(jsonResource.get("emails").isArray());
		assertEquals(1, jsonResource.get("emails").size());
		assertTrue(jsonResource.has("phoneNumbers"));
		assertTrue(jsonResource.get("phoneNumbers").isArray());
		assertEquals(1, jsonResource.get("phoneNumbers").size());
	}

	/**
	 * 
	 * @param username
	 * @param claimUri
	 * @param claimValue
	 * @return
	 */
	private boolean verifyClaimValue(String username, String claimUri, String claimValue) {
		switch (claimUri) {
		case "http://wso2.org/claims/givenname":
			return (username + " " + AbstractAuthorizationServerMockController.DEFAULT_FIRSTNAME_SUFFIX)
					.equals(claimValue);
		case "http://wso2.org/claims/lastname":
			return (username + " " + AbstractAuthorizationServerMockController.DEFAULT_LASTNAME_SUFFIX)
					.equals(claimValue);
		case "http://wso2.org/claims/email":
			return (username + "@" + AbstractAuthorizationServerMockController.DEFAULT_EMAIL_DOMAIN).equals(claimValue);
		case "http://wso2.org/claims/telephone":
			return AbstractAuthorizationServerMockController.DEFAULT_TELEPHONE.equals(claimValue);
		case "http://wso2.org/claims/mobile":
			return AbstractAuthorizationServerMockController.DEFAULT_MOBILE.equals(claimValue);
		default:
			return false;
		}
	}

	@Test
	public void checkRevokeAccessToken() {
	}

	@Override
	protected boolean checkOAuth2Authorization(Object... params) {
		return false;
	}

	@Override
	public void checkOk(Object... args) throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ProcessingException, NumberFormatException, ParseException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
	}

	@Override
	public void tearDown() {
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
