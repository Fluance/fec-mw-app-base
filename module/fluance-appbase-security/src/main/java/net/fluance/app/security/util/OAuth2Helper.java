/**
 * 
 */
package net.fluance.app.security.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.app.security.auth.OAuth2AccessToken;
import net.fluance.commons.codec.Base64Utils;
import net.fluance.commons.io.StreamUtils;
import net.fluance.commons.net.HttpUtils;

@Component
public class OAuth2Helper {

	//private static final String SAML2_GRANT_TYPE = "saml2";
	private static final String PASSWORD_GRANT_TYPE = "password";
	
	@Value("${oauth2.service.token.url}")
	private String oAuth2TokenUrl;
	@Value("${oauth2.service.client.authorization-type}")
	private String oAuth2ClientAuthorizationType;
	@Value("${oauth2.service.client.id}")
	private String oAuth2ClientId;
	@Value("${oauth2.service.client.secret}")
	private String oAuth2ClientSecret;
	
	@Value("${authorization.header}")
	private String authorizationHeader;
	
	private static final String GRANT_TYPE = "grant_type";
	@Value("${oauth2.service.url.AuthorizationHeader}")
	private String oAuth2ServerAuthorizationHeader;
	
	public OAuth2AccessToken accessToken(String tokenEndpoint, String spAuth, String grantType, String... args) throws Exception{
		switch (grantType) {
		case PASSWORD_GRANT_TYPE:
			return passwordAccessToken(tokenEndpoint, args);
		default:
			throw new IllegalArgumentException("Invalid grant type: " + grantType);
		}
	}

	public String getAuth2SrvToken(String url, String username, String password) throws UnsupportedEncodingException, HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair(GRANT_TYPE, "password"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		HttpPost postRequest = HttpUtils.buildPost(new URI(url), null, entity);
		postRequest.setHeader(new BasicHeader("Authorization", oAuth2ServerAuthorizationHeader));
		CloseableHttpResponse response = HttpUtils.send(postRequest, true);
		return EntityUtils.toString(response.getEntity());
	}

	/**
	 * 
	 * @param tokenEndpoint
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public OAuth2AccessToken passwordAccessToken(String tokenEndpoint, String... args) throws Exception{
		String payload = null;
		String clientAuth = Base64Utils.encode(oAuth2ClientId + ":" + oAuth2ClientSecret);
		switch (args.length) {
		case 2:
			payload = "grant_type=password&username=" + args[0] + "&password=" + args[1];
			break;
		case 3:
			clientAuth = Base64Utils.encode(args[0].getBytes());
			payload = "grant_type=password&username=" + args[1] + "&password=" + args[2];
			break;
		case 4:
			clientAuth = Base64Utils.encode((args[0]+":"+args[1]).getBytes());
			payload = "grant_type=password&username=" + args[2] + "&password=" + args[3];
			break;
		default:
			throw new IllegalArgumentException(args + " is not a valid argument. Valid are [\"username\", \"password\"], [\"Base64-encoded clientAuthorization\", \"username\", \"password\"] and [\"clientId\", \"clientSecret\", \"username\", \"password\"]");
		}
		
		OAuth2AccessToken accessToken = requestAccessToken(tokenEndpoint, clientAuth, payload);
		
		return accessToken;
	}
	
	/**
	 * 
	 * @param uri
	 * @param payload
	 * @return
	 * @throws URISyntaxException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws IOException
	 */
	public OAuth2AccessToken requestAccessToken(String uri, String clientAuth, String payload) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException {
		OAuth2AccessToken accessToken = null;
		URI uRI = HttpUtils.buildUri(uri);
		List<Header> headers = new ArrayList<>();
		
//		String clientAuth = Base64Utils.encode(oAuth2ClientId + ":" + oAuth2ClientSecret);
		
		Header authHeader = new BasicHeader(authorizationHeader, oAuth2ClientAuthorizationType + " " + clientAuth);
		headers.add(authHeader);
		StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_FORM_URLENCODED.withCharset(Charset.forName("utf-8")));
		HttpPost request = HttpUtils.buildPost(uRI, headers, entity);
		CloseableHttpResponse response = HttpUtils.send(request, true);
		
		String accessTokenStr = stringEntity(response);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode accessTokenNode = objectMapper.readTree(accessTokenStr);
			
			accessToken = new OAuth2AccessToken();
			accessToken.setTokenType(accessTokenNode.get("token_type").textValue());
			accessToken.setRefreshToken(accessTokenNode.get("refresh_token").textValue());
			accessToken.setExpirationDate(accessTokenNode.get("expires_in").longValue());
			accessToken.setAccessToken(accessTokenNode.get("access_token").textValue());
		} catch (Exception e) {
			throw e;
		}
		
		return accessToken;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public String stringEntity(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		HttpEntity responseEntity = response.getEntity();
		String entity = StreamUtils.inputStreamToString(responseEntity
				.getContent());
		EntityUtils.consume(responseEntity);
		return entity;
	}
}
