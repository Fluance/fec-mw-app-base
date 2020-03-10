/**
 * 
 */
package net.fluance.app.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.xml.sax.SAXException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.commons.json.JsonValidationUtils;
import net.fluance.commons.net.HttpUtils;

@WebIntegrationTest
@PropertySource({"classpath:test.properties","classpath:webapps/conf/security.properties"})
public abstract class AbstractWebIntegrationTest extends AbstractIntegrationTest {

	protected static final Map<String, String> MEDIA_TYPES = new HashMap<>(); 
	public static final String AUTHORIZED_LABEL = "authorized";
	public static final String UNAUTHORIZED_LABEL = "unauthorized";
	
	@Value("${server.port}")
	protected int serverPort;
	
	@Value("${oauth2.service.token.url}")
	private String oAuth2TokenServiceUrl;
	@Value("${oauth2.service.client.authorization-type}")
	private String oAuth2ClientAuthorizationType;
	@Value("${oauth2.service.client.id}")
	private String oAuth2ClientId;
	@Value("${oauth2.service.client.secret}")
	private String oAuth2ClientSecret;
	@Value("${oauth2.service.token-validate.url}")
	protected String oauth2TokenValidateUrl;
	
	protected String specLocation;
	protected String baseUrl;
	protected String authBaseUrl;
	protected MockMvc mockMvc;
	
	protected static String DECISION_PERMIT = "<Response><Result><Decision>Permit</Decision><Status><StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/></Status></Result></Response>";
	
	static {
		MEDIA_TYPES.put("xml", "application/xml");
		MEDIA_TYPES.put("json", "application/json");
	}
	
	@Before
	public abstract void setUp() throws Exception;
	
	@After
	public abstract void tearDown();
	
	protected boolean isSchemaCompliant(CloseableHttpResponse response, String schema) throws UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, URISyntaxException, ProcessingException {
		// Process the response
		String responsePayload = HttpUtils.stringEntity(response);
		String responseContentType = response.getEntity().getContentType().getValue();
		boolean isSchemaCompliant = isSchemaCompliant(schema, responsePayload, responseContentType);
		return isSchemaCompliant;
	}
	
	/**
	 * 
	 * @param httpMethod
	 * @param url
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws URISyntaxException
	 */
	protected CloseableHttpResponse invokeResource(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, URISyntaxException {
		if(!resourcePath.startsWith("/")) {
			resourcePath = "/" + resourcePath;
		}
		if(!baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length());
		}
		CloseableHttpResponse response = null;
		String resourceUri = baseUrl + resourcePath;
		
		if(params == null || params.isEmpty()) {
			response = HttpUtils.send(httpMethod, resourceUri, headers, payload, contentType, charset);
		} else {
			URI uri = HttpUtils.buildUri(resourceUri);
			uri = new URIBuilder(uri).addParameters(params).build();
			HttpRequestBase request = HttpUtils.buildRequest(httpMethod, uri, headers, null);
			response = HttpUtils.send(request, true);
		}

		return response;
	}
	
	/**
	 * 
	 * @param baseUrl
	 * @param httpMethod
	 * @param resourcePath
	 * @param headers
	 * @param params
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws URISyntaxException
	 */
	protected CloseableHttpResponse invokeResource(String baseUrl, String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, URISyntaxException {
		CloseableHttpResponse response = null;
		String resourceUri = baseUrl + resourcePath;
		
		if(params == null || params.isEmpty()) {
			response = HttpUtils.send(httpMethod, resourceUri, headers, payload, contentType, charset);
		} else {
			URI uri = HttpUtils.buildUri(resourceUri);
			uri = new URIBuilder(uri).addParameters(params).build();
			HttpRequestBase request = HttpUtils.buildRequest(httpMethod, uri, headers, null);
			response = HttpUtils.send(request, true);
		}

		return response;
	}
	
	/**
	 * 
	 * @param url
	 * @param httpMethod
	 * @param headers
	 * @param params
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws URISyntaxException
	 */
	protected CloseableHttpResponse invokeResource(URL url, String httpMethod, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, URISyntaxException {
		CloseableHttpResponse response = null;

		if(params == null || params.isEmpty()) {
			response = HttpUtils.send(httpMethod, url.toString(), headers, payload, contentType, charset);
		} else {
			URI uri = HttpUtils.buildUri(url.toString());
			uri = new URIBuilder(uri).addParameters(params).build();
			HttpRequestBase request = HttpUtils.buildRequest(httpMethod, uri, headers, null);
			response = HttpUtils.send(request, true);
		}

		return response;
	}
	
	/**
	 * 
	 * @param expectedHeaders
	 * @param actualHeaders
	 * @return
	 */
	public boolean checkHeadersPresence(Collection<String> expectedHeaders, Collection<String> actualHeaders) {
		
		// If no header expected, then
		if((expectedHeaders == null) || (expectedHeaders.isEmpty())) {
			// No need to go further
			return true;
		}
		
		// If we reach here, there are expected headers, so actual must exist
		if((actualHeaders == null) || (actualHeaders.isEmpty())) {
			// No need to go further
			return false;
		}
		
		return actualHeaders.containsAll(expectedHeaders);
		
	}
	
	/**
	 * 
	 * @param expectedHeaders
	 * @param actualHeaders
	 * @return Whether 
	 */
	public boolean checkHeadersValues(Map<String, String> expectedHeaders, Map<String, String> actualHeaders) {
		
		// If no header expected, then
		if((expectedHeaders == null) || (expectedHeaders.isEmpty())) {
			// No need to go further
			return true;
		}
		
		Set<String> expectedHeadersNames =expectedHeaders.keySet();
		Set<String> actualHeadersNames =actualHeaders.keySet();
		
		boolean asExpected = (actualHeaders!=null) ? checkHeadersPresence(expectedHeadersNames, actualHeadersNames) : false;
		if(!asExpected) {
			return false;
		}
		
		Iterator<String> expectedIter = expectedHeadersNames.iterator();
		while(asExpected && expectedIter.hasNext()) {
			String headerName = expectedIter.next();
			asExpected = asExpected && expectedHeaders.get(headerName).equals(actualHeaders.get(headerName));
		}
		
		return asExpected;
		
	}
	
	/**
	 * Checks if the 
	 * @param payload
	 * @param payload
	 * @return
	 * @throws IOException 
	 * @throws ProcessingException 
	 */
	protected boolean isSchemaCompliant(String schema, String payload, ContentType contentType) throws ProcessingException, IOException {
		if(ContentType.APPLICATION_JSON.getMimeType().equals(contentType)) {
			return isSchemaCompliant(schema, payload, contentType);
		}
		return false;
	}
	
	/**
	 * 
	 * @param schema
	 * @param payload
	 * @param contentType
	 * @return
	 * @throws ProcessingException
	 * @throws IOException
	 */
	protected boolean isSchemaCompliant(String schema, String payload, String contentType) throws ProcessingException, IOException {
		if(ContentType.APPLICATION_JSON.getMimeType().equals(contentType)) {
			return isJsonSchemaCompliant(schema, payload);
		}
		return false;
	}
	
	/**
	 * 
	 * @param schema
	 * @param payload
	 * @return
	 * @throws IOException 
	 * @throws ProcessingException 
	 */
	protected boolean isJsonSchemaCompliant(String schema, String payload) throws ProcessingException, IOException {
		boolean ok = JsonValidationUtils.isJsonValid(schema, payload);
		return ok;
	}
	
	/**
	 * Builds the authorization header based on the provided information
	 * @param data
	 * @return
	 */
	protected Header authorizationHeader(String type, String authorizationToken) {
		return new BasicHeader("Authorization", type + " " + authorizationToken);
	}
	
	/**
	 * Send a real http requestusing the complete URL and the access token
	 * @param fullUri
	 * @param token accessToken
	 * @return CloseableHttpResponse
	 */
	protected CloseableHttpResponse sendRequest(String fullUri, String token) throws URISyntaxException, HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		URI uri = HttpUtils.buildUri(fullUri);
		HttpGet get = HttpUtils.buildGet(uri, null);
		get.setHeader("Authorization", "Bearer " + token);
		getLogger().info("Sending HTTP GET...");
		CloseableHttpResponse response = HttpUtils.sendGet(get, true);
		getLogger().info("Response.Status = " + response.getStatusLine().getStatusCode());
		return response;
	}
	
	/**
	 * Send a real http request using the complete URL, the access token a body and a contentType
	 * @param fullUri
	 * @param token
	 * @param body
	 * @param contentType
	 * @return CloseableHttpResponse
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	protected CloseableHttpResponse sendPostRequest(String fullUri, String token, String body, String contentType) throws URISyntaxException, HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		URI uri = HttpUtils.buildUri(fullUri);
		HttpPost post = HttpUtils.buildPost(uri, null);
		HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
        post.setEntity(entity);
		post.setHeader("Authorization", "Bearer " + token);
		post.setHeader("Content-Type", contentType);
		getLogger().info("Sending HTTP POST...");
		CloseableHttpResponse response = HttpUtils.sendPost(post, true);
		getLogger().info("Response.Status = " + response.getStatusLine().getStatusCode());
		return response;
	}
	
	protected abstract boolean checkOAuth2Authorization(Object... params);

	public abstract void checkOk(Object... params) throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException, ProcessingException, NumberFormatException, ParseException, XPathExpressionException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException;
	
	/**
	 * Checks that the return of the resource request has the Bad Request status when expected
	 * @param httpMethod The Http method
	 * @param resourcePath The resource path
	 * @param headers The headers, Typically authorization
	 * @param payload The request body payload, if applicable
	 * @param contentType The request body content type, if applicable
	 * @param charset The request body charset, if applicable
	 * @throws Exception
	 */
	protected boolean checkHttpStatusBadRequest(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws Exception {
		CloseableHttpResponse response = invokeResource(httpMethod, resourcePath, headers, params, payload, contentType, charset);
		getLogger().info("Call to " + baseUrl + resourcePath + " returned status code :  " + response.getStatusLine().getStatusCode());
		// test status code
		return HttpStatus.SC_BAD_REQUEST == response.getStatusLine().getStatusCode();
	}
	
	/**
	 * 
	 * @param httpMethod
	 * @param resourcePath
	 * @param headers
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	protected boolean checkHttpStatusUnAuthorized(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws Exception {
		CloseableHttpResponse response = invokeResource(httpMethod, resourcePath, headers, params, payload, contentType, charset);;
		getLogger().info("Call to " + baseUrl + resourcePath + " returned status code :  " + response.getStatusLine().getStatusCode());
		return HttpStatus.SC_UNAUTHORIZED == response.getStatusLine().getStatusCode();
	}
	
	/**
	 * 
	 * @param httpMethod
	 * @param resourcePath
	 * @param headers
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	protected boolean checkHttpStatusForbidden(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws Exception{
		CloseableHttpResponse response = invokeResource(httpMethod, resourcePath, headers, params, payload, contentType, charset);;
		getLogger().info("Call to " + baseUrl + resourcePath + " returned status code :  " + response.getStatusLine().getStatusCode());
		return HttpStatus.SC_FORBIDDEN == response.getStatusLine().getStatusCode();
	}
	
	/**
	 * 
	 * @param httpMethod
	 * @param resourcePath
	 * @param headers
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	protected boolean checkHttpStatusConflict(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws Exception{
		CloseableHttpResponse response = invokeResource(httpMethod, resourcePath, headers, params, payload, contentType, charset);;
		getLogger().info("Call to " + baseUrl + resourcePath + " returned status code :  " + response.getStatusLine().getStatusCode());
		return HttpStatus.SC_CONFLICT == response.getStatusLine().getStatusCode();
	}
	
	protected boolean checkHttpStatusInternalServerError(String httpMethod, String resourcePath, List<Header> headers, List<NameValuePair> params, String payload, ContentType contentType, String charset) throws Exception{
		CloseableHttpResponse response = invokeResource(httpMethod, resourcePath, headers, params, payload, contentType, charset);;
		getLogger().info("Call to " + baseUrl + resourcePath + " returned status code :  " + response.getStatusLine().getStatusCode());
		return HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode();
	}
	
	/**
	 * Requests a user access token in test context
	 * @param userName
	 * @param domain
	 * @return
	 * @throws KeyManagementException
	 * @throws UnsupportedOperationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws HttpException
	 * @throws URISyntaxException
	 */
	public String getAccessToken(String username, String domain) throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException{
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("domain", domain));
		CloseableHttpResponse response = invokeResource(new URL(oAuth2TokenServiceUrl), HttpMethod.GET.name(), null, params, null, ContentType.APPLICATION_XML, null);
		String token = EntityUtils.toString(response.getEntity());
		return token;
	}
	
	/**
	 * Get the class's logger
	 * @return
	 */
	public abstract Logger getLogger();
}
