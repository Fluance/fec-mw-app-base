/**
 * 
 */
package net.fluance.app.test.mock;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Email implements Serializable {

	private String type;
	private String value;

	public Email() {}
	
	/**
	 * @param type
	 * @param value
	 */
	public Email(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
