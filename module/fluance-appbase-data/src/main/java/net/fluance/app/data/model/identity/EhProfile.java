/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class EhProfile implements Serializable {

	@JsonProperty("scim_id")
	private String scimId;
	private String domain;
	private String username;
	private String language;
	private UserType usertype;
	private AccessControl grants;
	
	public EhProfile() {}
	
	public EhProfile(String scimId, String domain, String username, String language, UserType usertype, AccessControl grants) {
		super();
		this.scimId = scimId;
		this.domain = domain;
		this.username = username;
		this.language = language;
		this.usertype = usertype;
		this.grants = grants;
	}

	public String getScimId() {
		return scimId;
	}

	public void setScimId(String scimId) {
		this.scimId = scimId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUsertype() {
		return usertype.getValue();
	}

	public void setUsertype(String usertype) {
		this.usertype = UserType.permissiveValueOf(usertype);
	}

	public AccessControl getGrants() {
		return grants;
	}

	public void setGrants(AccessControl grants) {
		this.grants = grants;
	}
	
	/**
	 * This method returns the staffId's for a given company.
	 * 
	 * @param companyId, providerId
	 */
	public String getStaffId(Integer companyId, Integer providerId) {
		if (companyId != null && providerId != null) {
			AccessControl grants = this.getGrants();
			if (grants != null && grants.getGrantedCompanies() != null) {
				for (GrantedCompany grantedCompany : grants.getGrantedCompanies()){
					if (grantedCompany.getId() == companyId) {
						List<CompanyStaffId> staffids = grantedCompany.getStaffIds();
						if (staffids != null && !staffids.isEmpty()) {
							for (CompanyStaffId companyStaffId : staffids) {
								if (companyStaffId.getProviderId() == providerId) {
									return companyStaffId.getStaffId();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * This method returns the staffId's from the user profile for a given providerId.
	 * 
	 * @param providerId
	 */
	public Map<Integer,String> getCompanyStaffIds(Integer providerId){
		Map<Integer,String> companystaffIds = new HashMap<Integer, String>();
		if(providerId != null){
			AccessControl grants = this.getGrants();
			if (grants != null && grants.getGrantedCompanies() != null){
				for (GrantedCompany grantedCompany : grants.getGrantedCompanies()){
					List<CompanyStaffId> staffids=grantedCompany.getStaffIds();	
					if(staffids != null && !staffids.isEmpty()){
						for (CompanyStaffId companyStaffId : staffids) {
							if(companyStaffId.getProviderId()==providerId){
								companystaffIds.put(grantedCompany.getId(), companyStaffId.getStaffId());
							}
						}
					}
				}
			}
		}
		return companystaffIds;
	}
	
	/**
	 * This method returns the companies of a user from the user 
	 * profile for a given providerId.
	 * 
	 * @param providerId
	 */
	public List<Integer> getGrantedCompanies(Integer providerId){
		List<Integer> companies = new ArrayList<>();
		if(providerId!=null){
			AccessControl grants = this.getGrants();
			if (grants != null && grants.getGrantedCompanies() != null){
				for (GrantedCompany grantedCompany : grants.getGrantedCompanies()){
					List<CompanyStaffId> staffids=grantedCompany.getStaffIds();	
					if(staffids != null && !staffids.isEmpty()){
						for (CompanyStaffId companyStaffId : staffids) {
							if(companyStaffId.getProviderId()==providerId){
								companies.add(grantedCompany.getId());
							}
						}
					}	
				}
			}
		}
		return companies;
	}
	
	/**
	 * This method returns the staffIds of a user from the user 
	 * profile for a given providerId.
	 * 
	 * @param providerId
	 */
	public List<String> getStaffIds(Integer providerId){
		List<String> staffIds = new ArrayList<>();
		if(providerId != null){
			AccessControl grants = this.getGrants();
			if (grants != null && grants.getGrantedCompanies() != null){
				for (GrantedCompany grantedCompany : grants.getGrantedCompanies()){
					List<CompanyStaffId> staffids=grantedCompany.getStaffIds();	
					if(staffids != null && !staffids.isEmpty()){
						for (CompanyStaffId companyStaffId : staffids) {
							if(companyStaffId.getProviderId()==providerId){
								staffIds.add(companyStaffId.getStaffId());
							}
						}
					}
				}
			}
		}
		return staffIds;
	}
}
