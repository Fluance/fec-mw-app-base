/**
 * 
 */
package net.fluance.app.test.mock;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Name implements Serializable {

	private String givenName;
	private String familyName;
	
	public Name() {}

	/**
	 * @param givenName
	 * @param familyName
	 */
	public Name(String givenName, String familyName) {
		super();
		this.givenName = givenName;
		this.familyName = familyName;
	}

	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
}
