/**
 * 
 */
package net.fluance.app.security.service.support.entitlement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PreparedPermissionEvaluateRequestBody extends PermissionEvaluateRequestBody {

	@JsonProperty("user_roles")
	protected List<String> userRoles;
	
	public PreparedPermissionEvaluateRequestBody() {}
	
	/**
	 * @param resource
	 * @param username
	 * @param action
	 * @param userRoles
	 */
	public PreparedPermissionEvaluateRequestBody(String resource, String username, String domain, String action, List<String> userRoles) {
		super(resource, username, domain, action);
		this.userRoles = userRoles;
	}

	/**
	 * @return the userRoles
	 */
	public List<String> getUserRoles() {
		return userRoles;
	}
	
	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return super.toString();
		}
	}
}
