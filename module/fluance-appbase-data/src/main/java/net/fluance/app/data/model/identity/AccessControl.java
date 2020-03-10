/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class AccessControl implements Serializable {

	@JsonProperty("companies")
	private List<GrantedCompany> grantedCompanies;
	private List<String> roles;

	public AccessControl() {}
	
	/**
	 * @param grantedCompanies
	 * @param roles
	 */
	public AccessControl(List<GrantedCompany> grantedCompanies, List<String> roles) {
		super();
		this.grantedCompanies = grantedCompanies;
		this.roles = roles;
	}

	/**
	 * @return the grantedCompanies
	 */
	public List<GrantedCompany> getGrantedCompanies() {
		return grantedCompanies;
	}

	/**
	 * @param grantedCompanies the grantedCompanies to set
	 */
	public void setGrantedCompanies(List<GrantedCompany> grantedCompanies) {
		this.grantedCompanies = grantedCompanies;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean addGrantedCompany(GrantedCompany company) {
		return grantedCompanies.add(company);
	}
	
}
