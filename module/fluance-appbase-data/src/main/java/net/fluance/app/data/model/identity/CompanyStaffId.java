/**
 * 
 */
package net.fluance.app.data.model.identity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyStaffId {

	@JsonProperty("providerid")
	private Integer providerId;
	@JsonProperty("staffid")
	private String staffId;
	
	/**
	 * @return the providerId
	 */
	public Integer getProviderId() {
		return providerId;
	}
	
	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * @return the staffId
	 */
	public String getStaffId() {
		return staffId;
	}
	
	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
}
