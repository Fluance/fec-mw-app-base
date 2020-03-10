/**
 * 
 */
package net.fluance.app.security.util;

import java.util.StringTokenizer;

public class UserManagementUtils {

	/**
	 * 
	 * @param fullyQualifiedUsername
	 * @param separator
	 * @param layout
	 * @return
	 */
	public static final String userNameFromFQUN(String fullyQualifiedUsername, String separator, FullyQualifiedUsernameLayout layout) {
		if (fullyQualifiedUsername == null || !fullyQualifiedUsername.contains(separator)) {
			return fullyQualifiedUsername;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(fullyQualifiedUsername, separator);
		if (stringTokenizer.countTokens() != 2) {
			throw new IllegalArgumentException(fullyQualifiedUsername + " is not a valid (fully qualified) username");
		}
		
		String firstPart = stringTokenizer.nextToken();
		String secondPart = stringTokenizer.nextToken();

		switch(layout) {
		case DOMAIN_FIRST:
			return secondPart;
		case USERNAME_FIRST:
			return firstPart;
		default:
			throw new IllegalArgumentException(layout + " is not a valid layout");
		}
	}

	/**
	 * 
	 * @param fullyQualifiedUsername
	 * @param separator
	 * @param layout
	 * @return
	 */
	public static final String domainFromFQUN(String fullyQualifiedUsername, String separator, FullyQualifiedUsernameLayout layout) {
		if (fullyQualifiedUsername == null || !fullyQualifiedUsername.contains(separator)) {
			return null;
		}
		StringTokenizer stringTokenizer = new StringTokenizer(fullyQualifiedUsername, separator);
		if (stringTokenizer.countTokens() != 2) {
			throw new IllegalArgumentException(fullyQualifiedUsername + " is not a valid (fully qualified) username");
		}
		
		String firstPart = stringTokenizer.nextToken();
		String secondPart = stringTokenizer.nextToken();

		switch(layout) {
		case DOMAIN_FIRST:
			return firstPart;
		case USERNAME_FIRST:
			return secondPart;
		default:
			throw new IllegalArgumentException(layout + " is not a valid layout");
		}
	}
	
	/**
	 * Generates a fully qualified username from the username and domain name
	 * @param username
	 * @param domain
	 * @param separator
	 * @param layout
	 * @return
	 */
	public static final String fullyQualifiedUsername(String username, String domain, String separator, FullyQualifiedUsernameLayout layout) {
		if (layout == null || separator == null || username == null) {
			throw new IllegalArgumentException("username, separator and layout are required to generate a fully qualified username");
		}
		if (domain == null || domain.isEmpty()) {
			return username;
		}

		switch(layout) {
		case DOMAIN_FIRST:
			return domain + separator + username;
		case USERNAME_FIRST:
			return username + separator + domain;
		default:
			throw new IllegalArgumentException(layout + " is not a valid layout");
		}
	}
	
}
