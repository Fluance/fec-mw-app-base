/**
 * 
 */
package net.fluance.app.security.util;


public enum FullyQualifiedUsernameLayout {

	USERNAME_FIRST(Constants.FQUN_LAYOUT_USERNAME_FIRST),
	DOMAIN_FIRST(Constants.FQUN_LAYOUT_DOMAIN_FIRST);
	
	private String name;

	/**
	 * @param name
	 */
	private FullyQualifiedUsernameLayout(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Allows to get a layout from its name
	 * @param name
	 * @return
	 */
	public static FullyQualifiedUsernameLayout forName(String name) {
		switch (name) {
		case Constants.FQUN_LAYOUT_USERNAME_FIRST:
			return USERNAME_FIRST;
		case Constants.FQUN_LAYOUT_DOMAIN_FIRST:
			return FullyQualifiedUsernameLayout.DOMAIN_FIRST;
		default:
			return null;
		}
	}
	
}
