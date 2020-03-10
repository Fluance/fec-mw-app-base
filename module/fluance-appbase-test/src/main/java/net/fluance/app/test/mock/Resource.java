/**
 * 
 */
package net.fluance.app.test.mock;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {

	private String domain;
	@JsonProperty("scim_id")
	private String scimId;
	private String username;
	private Name name;
	private List<Email> emails;
	@JsonProperty("phoneNumbers")
	private List<Telephon> telephons;
	private List<Address> addresses;
	@JsonProperty("groups")
	private List<Role> roles;
	
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
	 * @return the scimId
	 */
	public String getScimId() {
		return scimId;
	}
	
	/**
	 * @param scimId the scimId to set
	 */
	public void setScimId(String scimId) {
		this.scimId = scimId;
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
	 * @return the name
	 */
	public Name getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(Name name) {
		this.name = name;
	}
	
	/**
	 * @return the emails
	 */
	public List<Email> getEmails() {
		return emails;
	}
	
	/**
	 * @param emails the emails to set
	 */
	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}
	
	/**
	 * @return the telephons
	 */
	public List<Telephon> getTelephons() {
		return telephons;
	}
	
	/**
	 * @param telephons the telephons to set
	 */
	public void setTelephons(List<Telephon> telephons) {
		this.telephons = telephons;
	}
	
	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}
	
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
