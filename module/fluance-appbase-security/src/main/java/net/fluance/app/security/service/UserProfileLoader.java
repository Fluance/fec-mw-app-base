package net.fluance.app.security.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.commons.json.JsonUtils;
import net.fluance.commons.net.HttpUtils;

@Component
public class UserProfileLoader {

	private static final Logger LOGGER = LogManager.getLogger(UserProfileLoader.class);

	@Value("${user-profile.service.url}")
	private String userProfileUrl;
	@Value("${oauth2.service.url.getUserInfos}")
	private String urlUserInfoRequest;
	@Value("${oauth2.service.url.AuthorizationHeader}")
	private String oAuth2ServerAuthorizationHeader;
	@Value("${ehprofile.getProfileByStaffIds}")
	private String urlGetProfileByStaffIds;

	/**
	 * Load User profile using username domain and oauth2 access token. If it cannot get the {@link UserProfile} returns a <b>null</b> value and print a message in the log with the cause
	 * 
	 * @param userName
	 * @param domain
	 * @param accessToken
	 * @return
	 */
	public UserProfile loadProfile(String userName, String domain, String accessToken) {
		UserProfile userProfile = new UserProfile();
		try {
			CloseableHttpResponse userProfileResponse = sendRequest(userProfileUrl, accessToken);
			if (userProfileResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ObjectMapper mapper = new ObjectMapper();
				String responseEntity = EntityUtils.toString(userProfileResponse.getEntity());
				userProfile = mapper.readValue(responseEntity, UserProfile.class);
			}else if (userProfileResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				LOGGER.warn("The Request for the User Profile has returned the message: " + userProfileResponse.getStatusLine());
				return null;
			}
			else {
				LOGGER.warn("The Request for the User Profile of "+ domain + "/" + userName + "has returned the message: " + userProfileResponse.getStatusLine());
				return null;
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | URISyntaxException
				| HttpException | IOException | RuntimeException e) {
			LOGGER.warn("Unable to Load USER PROFILE of " + domain + "/" + userName, e);
			return null;
		}
		return userProfile;
	}

	/**
	 * Send a real http requestusing the complete URL and the access token
	 * 
	 * @param fullUri
	 * @param token
	 *            accessToken
	 * @return CloseableHttpResponse
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws HttpException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	protected CloseableHttpResponse sendRequest(String fullUri, String token) throws URISyntaxException,
			KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException {
		//TODO : change to full URL
		URI uri = HttpUtils.buildUri(fullUri);
		HttpGet get = HttpUtils.buildGet(uri, null);
		get.setHeader("Authorization", "Bearer " + token);
		CloseableHttpResponse response = HttpUtils.sendGet(get, true);
		return response;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public User getUserWithInfos(User user) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException, URISyntaxException {
		UserInfo userInfos = requestUserInfos(user);
		if (userInfos != null && JsonUtils.checkJsonCompatibility(userInfos.getUserInfo(), User.class)){
			User userWithInfo = new ObjectMapper().readValue(userInfos.getUserInfo(), User.class);
			userWithInfo.setUserIdentityFromUser(user);
			return userWithInfo;
		}
		else{
			LOGGER.info("Unable to initialize the user infos of : " + user.getDomain() + "/" + user.getUsername());
			return user;
		}
	}
	/**
	 * 
	 * @param user
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private UserInfo requestUserInfos(User user) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, IOException, URISyntaxException {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("username", user.getUsername()));
		params.add(new BasicNameValuePair("domain", user.getDomain()));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		entity.setContentType("application/json");
		Header authorizationHeader = new BasicHeader("Authorization", oAuth2ServerAuthorizationHeader);
		HttpGet getRequest = HttpUtils.buildGet(new URI(urlUserInfoRequest+"?username="+user.getUsername()+"&domain="+user.getDomain()), Arrays.asList(authorizationHeader));
		CloseableHttpResponse response = HttpUtils.send(getRequest, true);

		if (response.getStatusLine().getStatusCode() == 200) {
			String test = EntityUtils.toString(response.getEntity(), "UTF-8");
			return new ObjectMapper().readValue(test, UserInfo.class);
		} else {
			return null;
		}
	}
	
	public Person buildFullName(Person editor) {
		if(editor != null && editor.getUsername() != null){
			String[] editorDomainUsername = editor.getUsername().split("/");
			if(editorDomainUsername.length == 2){
				String domain = editorDomainUsername[0];
				String username = editorDomainUsername[1];
				try {
					User user = this.getUserWithInfos(new User(username, domain, "", null));
					if(user != null){
						return new Person(editor.getUsername(), user.getFirstName(), user.getLastName());
					} else {
						return editor;
					}
				} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | HttpException | IOException | URISyntaxException e) {
					LOGGER.warn("Not Able to get FirstName and Last name of " + editor.getUsername());
					return editor;
				}
			} else {
				return editor;
			}
		} else {
			return editor;
		}
	}
	
	public EhProfile getProfileByStaffIds(String staffId, Integer cliniqueId, Integer providerId, String token) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, URISyntaxException, HttpException, IOException {
		EhProfile ehProfile = new EhProfile();
		try {
			CloseableHttpResponse userProfileResponse = sendRequest(urlGetProfileByStaffIds+"?staffId="+staffId+"&cliniqueId="+cliniqueId+"&providerId="+providerId, token);
			if (userProfileResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ObjectMapper mapper = new ObjectMapper();
				String responseEntity = EntityUtils.toString(userProfileResponse.getEntity());
				ehProfile = mapper.readValue(responseEntity, EhProfile.class);
			} 
			else if (userProfileResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				LOGGER.warn("The Request for the User Profile has returned the message: " + userProfileResponse.getStatusLine());
				return null;
			}
			else {
				LOGGER.warn("The Request for the Eh Profile of "+ staffId + "/" + cliniqueId + "/" + providerId + " has returned the message: " + userProfileResponse.getStatusLine());
				return null;
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | URISyntaxException
				| HttpException | IOException | RuntimeException e) {
			LOGGER.warn("Unable to Load USER PROFILE of " + "domain" + "/" + "userName", e);
			return null;
		}
		return ehProfile;
	}
}
