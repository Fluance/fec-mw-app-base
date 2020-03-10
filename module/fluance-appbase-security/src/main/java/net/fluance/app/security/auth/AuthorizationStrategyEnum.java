/**
 * 
 */
package net.fluance.app.security.auth;

public enum AuthorizationStrategyEnum {

	SINGLE_PATIENT("SINGLE_PATIENT"),
	AUTHENTICATION_TRUSTED_PARTNER("AUTHENTICATION_TRUSTED_PARTNER");
	
	private String name;

	/**
	 * @param name
	 */
	private AuthorizationStrategyEnum(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
