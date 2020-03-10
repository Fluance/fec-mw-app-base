/**
 * 
 */
package net.fluance.app.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.app.test.mock.Domain;
import net.fluance.app.test.mock.OAuth2Token;
import net.fluance.app.test.mock.OAuth2ValidationResponse;
import net.fluance.app.test.mock.User;

public class AuthorizationServerMockTest {

	private static final Integer DOMAIN1_ID = 1;
	private static final Integer DOMAIN2_ID = 2;
	private static final Integer DOMAIN3_ID = 3;
	private static final Integer DOMAIN4_ID = 4;
	private static final String DOMAIN1_NAME = "domain1.tld";
	private static final String DOMAIN2_NAME = "domain2.tld";
	private static final String DOMAIN3_NAME = "domain3.tld";
	private static final String DOMAIN4_NAME = "domain4.tld";
	private static final String USER1_NAME = "user1";
	private static final String USER2_NAME = "user2";
	private static final String USER3_NAME = "user3";
	private static final String USER4_NAME = "user4";
	private static final String ROLE1_NAME = "role1";
	private static final String ROLE2_NAME = "role2";
	private static final String ROLE3_NAME = "role3";
	private static final String ROLE4_NAME = "role4";
	private AuthorizationServerMock authServerMock;
	
	@Before
	public void setUp() {
		authServerMock = new AuthorizationServerMock();
		authServerMock.addDomain(DOMAIN1_ID, DOMAIN1_NAME);
		authServerMock.addDomain(DOMAIN2_ID, DOMAIN2_NAME);
		authServerMock.addDomain(DOMAIN3_ID, DOMAIN3_NAME);
		authServerMock.addRole(ROLE1_NAME);
		authServerMock.addRole(ROLE2_NAME);
		authServerMock.addRole(ROLE3_NAME);
		authServerMock.addUser(USER1_NAME, UUID.randomUUID().toString(), DOMAIN1_NAME);
		authServerMock.addUser(USER1_NAME, UUID.randomUUID().toString(), DOMAIN3_NAME);
		authServerMock.addUser(USER2_NAME, UUID.randomUUID().toString(), DOMAIN2_NAME);
		authServerMock.addUser(USER3_NAME, UUID.randomUUID().toString(), DOMAIN1_NAME);
		authServerMock.addUser(USER3_NAME, UUID.randomUUID().toString(), DOMAIN2_NAME);
		authServerMock.addUser(USER3_NAME, UUID.randomUUID().toString(), DOMAIN3_NAME);
	}
	
	@Test
	public void addDomainTest() {
		Domain domain4 = authServerMock.getDomainByName(DOMAIN4_NAME);
		assertNull(domain4);
		authServerMock.addDomain(DOMAIN4_ID, DOMAIN4_NAME);
		domain4 = authServerMock.getDomainByName(DOMAIN4_NAME);
		assertNotNull(domain4);
		assertEquals(DOMAIN4_ID, Integer.valueOf(domain4.getId()));
		assertEquals(DOMAIN4_NAME, domain4.getName());
	}
	
	@Test
	public void addUserTest() {
		User user4 = authServerMock.getUserByUsernameAndDomain(USER4_NAME, DOMAIN4_NAME);
		assertNull(user4);
		authServerMock.addUser(USER4_NAME, UUID.randomUUID().toString(), DOMAIN4_NAME);
		user4 = authServerMock.getUserByUsernameAndDomain(USER4_NAME, DOMAIN4_NAME);
		assertNotNull(user4);
		assertEquals(USER4_NAME, user4.getUsername());
	}
	
	@Test
	public void addRoleTest() {
		List<String> roles = authServerMock.getRoles();
		assertNotNull(roles);
		// The 'everyone' role is always added at server instantiation
		assertEquals(4, roles.size());
		authServerMock.addRole(ROLE4_NAME);
		roles = authServerMock.getRoles();
		assertNotNull(roles);
		assertEquals(5, roles.size());
		assertTrue(roles.contains(ROLE4_NAME));
	}
	
	@Test
	public void assignRoleTest() {
		List<String> user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(1, user2Roles.size());

		List<String> user3Roles = authServerMock.userRoles(USER3_NAME, DOMAIN2_NAME);
		assertNotNull(user3Roles);
		assertEquals(1, user3Roles.size());
		
		authServerMock.assignRole(USER2_NAME, DOMAIN2_NAME, ROLE1_NAME);
		authServerMock.assignRole(USER2_NAME, DOMAIN2_NAME, ROLE2_NAME);
		user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(3, user2Roles.size());
		assertTrue(user2Roles.contains(ROLE1_NAME));
		assertTrue(user2Roles.contains(ROLE2_NAME));
		
		authServerMock.assignRole(USER3_NAME, DOMAIN2_NAME, ROLE3_NAME);
		user3Roles = authServerMock.userRoles(USER3_NAME, DOMAIN2_NAME);
		assertNotNull(user3Roles);
		assertEquals(2, user3Roles.size());
		assertTrue(user3Roles.contains(ROLE3_NAME));
	}
	
	@Test
	public void revokeRoleTest() {
		List<String> user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(1, user2Roles.size());
		
		authServerMock.assignRole(USER2_NAME, DOMAIN2_NAME, ROLE1_NAME);
		authServerMock.assignRole(USER2_NAME, DOMAIN2_NAME, ROLE2_NAME);
		user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(3, user2Roles.size());
		assertTrue(user2Roles.contains(ROLE1_NAME));
		assertTrue(user2Roles.contains(ROLE2_NAME));
		
		authServerMock.revokeRole(USER2_NAME, DOMAIN2_NAME, ROLE1_NAME);
		user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(2, user2Roles.size());
		assertTrue(user2Roles.contains(ROLE2_NAME));
		assertFalse(user2Roles.contains(ROLE1_NAME));
		
		authServerMock.revokeRole(USER2_NAME, DOMAIN2_NAME, ROLE2_NAME);
		user2Roles = authServerMock.userRoles(USER2_NAME, DOMAIN2_NAME);
		assertNotNull(user2Roles);
		assertEquals(1, user2Roles.size());
		assertEquals(AuthorizationServerMock.DEFAULT_ROLE_NAME, user2Roles.get(0));
	}
	
	@After
	public void tearDown() {
		authServerMock = null;
	}

	@Test
	public void validateAccessTokenTest() throws ParseException{
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN3_NAME));
		assertNull(authServerMock.getUserAccessToken(USER2_NAME, DOMAIN2_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN2_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN3_NAME));
		OAuth2Token token = authServerMock.issueUserAccessToken(USER3_NAME, DOMAIN3_NAME);
		assertNotNull(token);
		assertNotNull(token.getAccessToken());
		assertNotNull(token.getRefreshToken());
		assertEquals(AuthorizationServerMock.DEFAULT_OAUTH2_EXPIRES_IN, token.getExpiresIn());
		assertEquals(AuthorizationServerMock.DEFAULT_OAUTH2_USER_TOKEN_TYPE, token.getTokenType());
		
		OAuth2ValidationResponse validationResponse = authServerMock.validateOAuth2AccessToken(token.getAccessToken());
		assertNotNull(validationResponse);
		assertEquals(DOMAIN3_NAME + "/" + USER3_NAME, validationResponse.getAuthzUser());
		assertTrue(validationResponse.isValid());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date issuedDate = sdf.parse("1900-01-01 00:00:00");
		token.setDateIssued(issuedDate);
		
		validationResponse = authServerMock.validateOAuth2AccessToken(token.getAccessToken());
		assertNotNull(validationResponse);
		assertEquals(DOMAIN3_NAME + "/" + USER3_NAME, validationResponse.getAuthzUser());
		assertFalse(validationResponse.isValid());
	}
	
	@Test
	public void issueUserTokenTest(){
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN3_NAME));
		assertNull(authServerMock.getUserAccessToken(USER2_NAME, DOMAIN2_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN2_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN3_NAME));
		OAuth2Token token = authServerMock.issueUserAccessToken(USER3_NAME, DOMAIN3_NAME);
		assertNotNull(token);
		assertNotNull(token.getAccessToken());
		assertNotNull(token.getRefreshToken());
		assertEquals(AuthorizationServerMock.DEFAULT_OAUTH2_EXPIRES_IN, token.getExpiresIn());
		assertEquals(AuthorizationServerMock.DEFAULT_OAUTH2_USER_TOKEN_TYPE, token.getTokenType());
		//Token has been issued before now
		// Wait 1s before obtaining the current date to avoid it being equal to the token dateIssued 
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date currentDate = new Date();
		assertTrue(currentDate.compareTo(token.getDateIssued()) > 0);
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER1_NAME, DOMAIN3_NAME));
		assertNull(authServerMock.getUserAccessToken(USER2_NAME, DOMAIN2_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN1_NAME));
		assertNull(authServerMock.getUserAccessToken(USER3_NAME, DOMAIN2_NAME));
	}
	
}
