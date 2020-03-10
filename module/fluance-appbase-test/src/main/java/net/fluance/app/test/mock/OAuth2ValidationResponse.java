/**
 * 
 */
package net.fluance.app.test.mock;

public class OAuth2ValidationResponse {

	private boolean valid;
	private String authzUser;
	
	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}
	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	/**
	 * @return the authzUser
	 */
	public String getAuthzUser() {
		return authzUser;
	}
	/**
	 * @param authzUser the authzUser to set
	 */
	public void setAuthzUser(String authzUser) {
		this.authzUser = authzUser;
	}
	
}
