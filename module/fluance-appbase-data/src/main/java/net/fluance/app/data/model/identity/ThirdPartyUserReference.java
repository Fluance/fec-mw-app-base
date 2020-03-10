package net.fluance.app.data.model.identity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class represent the info for the third party actual user.
 * It is use on the payload of the JWT token and on {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThirdPartyUserReference {
	private String actualUserName;
	private String actualFirstName;
	private String actualLastName;
	private String actualEmail;
	
	public String getActualUserName() {
		return actualUserName;
	}
	
	public void setActualUserName(String actualUserName) {
		this.actualUserName = actualUserName;
	}
	
	public String getActualFirstName() {
		return actualFirstName;
	}
	
	public void setActualFirstName(String actualFirstName) {
		this.actualFirstName = actualFirstName;
	}
	
	public String getActualLastName() {
		return actualLastName;
	}
	
	public void setActualLastName(String actualLastName) {
		this.actualLastName = actualLastName;
	}
	
	public String getActualEmail() {
		return actualEmail;
	}
	
	public void setActualEmail(String actualEmail) {
		this.actualEmail = actualEmail;
	}
	
	
}
