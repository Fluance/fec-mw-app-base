/**
 * 
 */
package net.fluance.app.test.mock;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable {

	private String addressLine;
	private String postalCode;
	private String locality;
	private String state;
	private String country;
	
	public Address() {}
	
	/**
	 * @param addressLine
	 * @param postalCode
	 * @param locality
	 * @param country
	 */
	public Address(String addressLine, String postalCode, String locality, String state, String country) {
		super();
		this.addressLine = addressLine;
		this.postalCode = postalCode;
		this.locality = locality;
		this.state = state;
		this.country = country;
	}
	
	/**
	 * @return the addressLine
	 */
	public String getAddressLine() {
		return addressLine;
	}
	
	/**
	 * @param addressLine the addressLine to set
	 */
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
	
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
}
