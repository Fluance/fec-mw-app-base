/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PatientUnit implements Serializable {

	private String code;
	
	public PatientUnit() {}
	
	/**
	 * @param code
	 * @param description
	 */
	public PatientUnit(String code) {
		super();
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
}
