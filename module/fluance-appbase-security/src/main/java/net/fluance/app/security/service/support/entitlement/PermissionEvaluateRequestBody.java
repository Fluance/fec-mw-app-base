/**
 * 
 */
package net.fluance.app.security.service.support.entitlement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PermissionEvaluateRequestBody {

	protected String resource;
	protected String username;
	protected String domain;
	protected String action;
	
	public PermissionEvaluateRequestBody() {}
	
	/**
	 * @param resource
	 * @param username
	 * @param action
	 * @param userRoles
	 */
	public PermissionEvaluateRequestBody(String resource, String username, String domain, String action/*, List<String> userRoles*/) {
		super();
		this.resource = resource;
		this.username = username;
		this.domain = domain;
		this.action = action;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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
