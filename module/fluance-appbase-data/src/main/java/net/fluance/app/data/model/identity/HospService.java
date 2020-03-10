/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HospService implements Serializable {

	private String code;
	
	public HospService() {}
	
	/**
	 * @param code
	 * @param description
	 */
	public HospService(String code) {
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
