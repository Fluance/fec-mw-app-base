/**
 * 
 */
package net.fluance.app.test.mock;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {

	@JsonProperty("display")
	private String name;

	public Role() {}
	
	/**
	 * @param name
	 */
	public Role(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
