package net.fluance.app.test.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;


public class AuthorizationServerMock {

	private static final Logger LOGGER = LogManager.getLogger(AuthorizationServerMock.class);
	
	private List<String> roles = new ArrayList<>();
	private Map<Domain, List<User>> users = new HashMap<>();
	// Access tokens by domain and user name
	private Map<Domain, Map<User, OAuth2Token>> userOauth2Tokens = new HashMap<>();
	private Map<User, List<String>> userRoles = new HashMap<>();
	private List<Domain> domains = new ArrayList<>();
	// Validity period (in seconds)
	public static final int DEFAULT_OAUTH2_EXPIRES_IN = 600;
	public static final String DEFAULT_OAUTH2_USER_TOKEN_TYPE = "Bearer";
	public static final String DEFAULT_ROLE_NAME = "everyone";

	public AuthorizationServerMock() {
		addRole(DEFAULT_ROLE_NAME);
	}
	
	/**
	 * 
	 * @param accessToken
	 * @return The fully qualified username of the access token owner, only if the access token is valid
	 */
	public OAuth2ValidationResponse validateOAuth2AccessToken(String accessToken) {
		OAuth2ValidationResponse response = new OAuth2ValidationResponse();
		boolean isTokenExpired = false;
		String fullyQualyfiedUsername = null;
		boolean found = false;
		Iterator<Domain> domainIterator = userOauth2Tokens.keySet().iterator();
		while (!found && domainIterator.hasNext()) {
			Domain domain = domainIterator.next();
			Iterator<User> userIterator = userOauth2Tokens.get(domain).keySet().iterator();
			while (!found && userIterator.hasNext()) {
				User user = userIterator.next();
				OAuth2Token currentToken = userOauth2Tokens.get(domain).get(user);
				LOGGER.info("User " + user.getUsername() + " has access token " + accessToken);
				isTokenExpired = isUserOAuth2TokenExpired(currentToken);
				if (currentToken.getAccessToken().equals(accessToken)) {
					fullyQualyfiedUsername = domain.getName() + "/" + user.getUsername();
					LOGGER.info("fullyQualyfiedUsername " + fullyQualyfiedUsername);
					found = true;
				}
				response.setValid(!isTokenExpired);
				response.setAuthzUser(fullyQualyfiedUsername);
			}
		}
		return response;
	}

	public OAuth2ValidationResponse validateOAuth2AccessToken(String accessToken, boolean sendDomainKey, Map<String, String> domainMappings) {
		OAuth2ValidationResponse response = new OAuth2ValidationResponse();
		boolean isTokenExpired = false;
		String fullyQualyfiedUsername = null;
		boolean found = false;
		Iterator<Domain> domainIterator = userOauth2Tokens.keySet().iterator();
		while (!found && domainIterator.hasNext()) {
			Domain domain = domainIterator.next();
			String domainKey = null;			
			Iterator<User> userIterator = userOauth2Tokens.get(domain).keySet().iterator();
			while (!found && userIterator.hasNext()) {
				User user = userIterator.next();
				OAuth2Token currentToken = userOauth2Tokens.get(domain).get(user);
				LOGGER.info("User " + user.getUsername() + " has access token " + accessToken);
				isTokenExpired = isUserOAuth2TokenExpired(currentToken);
				if (currentToken.getAccessToken().equals(accessToken)) {
					if(sendDomainKey) {
						for(String currentDomainKey : domainMappings.keySet()) {
							if(domainMappings.get(currentDomainKey).equals(domain.getName())) {
								domainKey = currentDomainKey;
							}
						}
					}
					fullyQualyfiedUsername = ((sendDomainKey) ? domainKey : domain.getName()) + "/" + user.getUsername();
					LOGGER.info("fullyQualyfiedUsername " + fullyQualyfiedUsername);
					found = true;
					response.setValid(!isTokenExpired);
					response.setAuthzUser(fullyQualyfiedUsername);
				}
			}
		}
		return response;
	}
	
	/**
	 * 
	 * @param username
	 * @param domainName
	 * @return
	 */
	public OAuth2Token getUserAccessToken(String username, String domainName) {
		Domain domain = getDomainByName(domainName);
		User user = getUserByUsernameAndDomain(username, domain);
		OAuth2Token token = null;
		Map<User, OAuth2Token> domainTokens = userOauth2Tokens.get(domain);
		if(domainTokens != null) {
			token = domainTokens.get(user);
		}
		return token;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public User getUserByUsernameAndDomain(String username, String domainName) {
		Domain domain = getDomainByName(domainName);
		return getUserByUsernameAndDomain(username, domain);
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public User getUserByUsernameAndDomain(String username, Domain domain) {
		if(users.get(domain) == null) {
			return null;
		}
		User user = null;
		for (User currentUser : users.get(domain)) {
			if (currentUser.getUsername().equals(username)) {
				user = currentUser;
			}
		}
		return user;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Domain getDomainByName(String name) {
		Domain domain = null;
		for (Domain currentDomain : domains) {
			if (currentDomain.getName().equals(name)) {
				domain = currentDomain;
			}
		}
		return domain;
	}

	public void addDomain(int id, String name) {
		if(getDomainByName(name) == null) {
			domains.add(new Domain(id, name));
			Domain domain = getDomainByName(name);
			userOauth2Tokens.put(domain, new HashMap<User, OAuth2Token>());
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param domainName
	 * @return
	 */
	public boolean addUser(String username, String password, String domainName) {
		Domain domain = getDomainByName(domainName);
		User user = new User(username, password);
		List<User> domainUsers = users.get(domain);
		if (domainUsers == null) {
			domainUsers = new ArrayList<User>();
			users.put(domain, domainUsers);
		}
		if (domainUsers.contains(user)) {
			throw new IllegalStateException("User " + username + " already exists in domain " + domainName);
		} else {
			boolean added = domainUsers.add(user);
			if(added) {
				assignRole(username, domainName, DEFAULT_ROLE_NAME);
			}
			return added;
		}
	}

	/**
	 * 
	 * @param username
	 * @param domainName
	 * @return
	 */
	public OAuth2Token issueUserAccessToken(String username, String domainName) {
		Domain domain = getDomainByName(domainName);
		User user = getUserByUsernameAndDomain(username, domain);
		if(user == null) {
			throw new IllegalArgumentException("User " + username + " doesn't exist in domain " + domain);
		}
		// If user already has an access token, then we return the existing one
		OAuth2Token token = null;
		Map<User, OAuth2Token> domainTokens = userOauth2Tokens.get(domain);
		if(domainTokens != null) {
			token = domainTokens.get(user);
		}
		if (token == null) { // else
			// We issue a new one
			domainTokens = userOauth2Tokens.get(domain);
			String accessToken = UUID.randomUUID().toString();
			String refreshToken = UUID.randomUUID().toString();
			String tokenType = DEFAULT_OAUTH2_USER_TOKEN_TYPE;
			int expiresIn = DEFAULT_OAUTH2_EXPIRES_IN;
			token = new OAuth2Token(new Date(), accessToken, refreshToken, tokenType, expiresIn);
			if(domainTokens == null) {
				domainTokens = new HashMap<User, OAuth2Token>();
			}
			domainTokens.put(user, token);
		}
		return token;
	}
	
	/**
	 * 
	 * @param roleName
	 */
	public void addRole(String roleName) {
		if(!roles.contains(roleName)) {
			roles.add(roleName);
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param domainName
	 * @param role
	 */
	public void assignRole(String username, String domainName, String role) {
		if(!roles.contains(role)) {
			throw new IllegalArgumentException("Role " + role + " doesn't exist");
		}
		Domain domain = getDomainByName(domainName);
		User user = getUserByUsernameAndDomain(username, domain);
		if(user == null) {
			throw new IllegalArgumentException("User " + username + " doesn't exist in domain " + domain);
		}
		if (userRoles.get(user) == null) {
			userRoles.put(user, new ArrayList<String>());
		}
		if(!userRoles.get(user).contains(role)) {
			userRoles.get(user).add(role);
		}
	}
	
	/**
	 * 
	 * @param username
	 * @param domainName
	 * @param role
	 */
	public void revokeRole(String username, String domainName, String role) {
		if(!roles.contains(role)) {
			throw new IllegalArgumentException("Role " + role + " doesn't exist");
		}
		Domain domain = getDomainByName(domainName);
		User user = getUserByUsernameAndDomain(username, domain);
		if(user == null) {
			throw new IllegalArgumentException("User " + username + " doesn't exist in domain " + domain);
		}
		if (userRoles.get(user) == null) {
			userRoles.put(user, new ArrayList<String>());
		}
		if(userRoles.get(user).contains(role)) {
			userRoles.get(user).remove(role);
		}
	}
	
	/**
	 * Get the list of roles assigned to a user, given his username and domain.
	 * @param username
	 * @param domainName
	 * @param role
	 * @return The list of roles if the user exists
	 */
	public List<String> userRoles(String username, String domainName) {
		Domain domain = getDomainByName(domainName);
		User user = getUserByUsernameAndDomain(username, domain);
		if(user == null) {
			throw new IllegalArgumentException("User " + username + " doesn't exist in domain " + domain);
		}
		return (userRoles.get(user) != null) ? userRoles.get(user) : new ArrayList<String>();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public boolean isUserOAuth2TokenExpired(OAuth2Token token) {
		long nowTime = new Date().getTime()/1000;
		long currentTokenIssuedTime = token.getDateIssued().getTime()/1000;
		boolean expired = (nowTime - currentTokenIssuedTime) > DEFAULT_OAUTH2_EXPIRES_IN;
		return expired;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @return the users
	 */
	public Map<Domain, List<User>> getUsers() {
		return users;
	}

	/**
	 * @return the userOauth2Tokens
	 */
	public Map<Domain, Map<User, OAuth2Token>> getUserOauth2Tokens() {
		return userOauth2Tokens;
	}

	/**
	 * @return the userRoles
	 */
	public Map<User, List<String>> getUserRoles() {
		return userRoles;
	}

	/**
	 * @return the domains
	 */
	public List<Domain> getDomains() {
		return domains;
	}
	
}
