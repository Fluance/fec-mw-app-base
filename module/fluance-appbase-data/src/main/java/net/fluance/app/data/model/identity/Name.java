/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Name implements Serializable {

	private String firstName;
	private String lastName;
	
	public Name() {}
	
	/**
	 * @param firstName
	 * @param lastName
	 */
	public Name(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
