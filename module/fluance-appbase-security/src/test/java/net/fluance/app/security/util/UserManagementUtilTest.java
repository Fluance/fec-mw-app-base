/**
 * 
 */
package net.fluance.app.security.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserManagementUtilTest {

	private static final String FQUN_USERNAMEFIRST_SEPARATOR = "@";
	private static final String FQUN_DOMAINFIRST_SEPARATOR = "/";
	
	private static final String USERNAME = "user";
	private static final String DOMAIN = "domain.tld";

	private static final String FQUN_DOMAIN_FIRST = DOMAIN + FQUN_DOMAINFIRST_SEPARATOR + USERNAME;
	private static final String FQUN_USERNAME_FIRST = USERNAME + FQUN_USERNAMEFIRST_SEPARATOR + DOMAIN;
	
	
	@Test
	public void testUserAndDomainFromFQUN() {
		assertEquals(USERNAME, UserManagementUtils.userNameFromFQUN(FQUN_DOMAIN_FIRST, FQUN_DOMAINFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_DOMAIN_FIRST)));
		assertEquals(DOMAIN, UserManagementUtils.domainFromFQUN(FQUN_DOMAIN_FIRST, FQUN_DOMAINFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_DOMAIN_FIRST)));
		assertEquals(USERNAME, UserManagementUtils.userNameFromFQUN(FQUN_USERNAME_FIRST, FQUN_USERNAMEFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_USERNAME_FIRST)));
		assertEquals(DOMAIN, UserManagementUtils.domainFromFQUN(FQUN_USERNAME_FIRST, FQUN_USERNAMEFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_USERNAME_FIRST)));
	}
	
	@Test
	public void testGenerateFQUN() {
		assertEquals(FQUN_DOMAIN_FIRST, UserManagementUtils.fullyQualifiedUsername(USERNAME, DOMAIN, FQUN_DOMAINFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_DOMAIN_FIRST)));
		assertEquals(FQUN_USERNAME_FIRST, UserManagementUtils.fullyQualifiedUsername(USERNAME, DOMAIN, FQUN_USERNAMEFIRST_SEPARATOR, FullyQualifiedUsernameLayout.forName(Constants.FQUN_LAYOUT_USERNAME_FIRST)));
	}

	
}
