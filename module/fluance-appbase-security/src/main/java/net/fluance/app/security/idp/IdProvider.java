/**
 * 
 */
package net.fluance.app.security.idp;

public enum IdProvider {

	AD("Microsoft Active Directory", "AD"),
	FACEBOOK("Facebook", "fb"),
	LDAP("LDAP", "ldap"),
	LOCAL("Local", "local"),;
	
	private String name;
	private String code;
	
	private IdProvider(String name, String code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * Allows getting a provider knowing its name
	 * @param name
	 * @return
	 */
	public static IdProvider forName(String name) {
		IdProvider provider = null; 
		for(IdProvider prov : values()) {
			if(prov.getName().equals(name)) {
				provider = prov;
				break;
			}
		}
		return provider;
	}
	
	/**
	 * Allows getting a provider knowing its code
	 * @param name
	 * @return
	 */
	public static IdProvider forCode(String code) {
		IdProvider provider = null; 
		for(IdProvider prov : values()) {
			if(prov.getCode().equals(code)) {
				provider = prov;
				break;
			}
		}
		return provider;
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
