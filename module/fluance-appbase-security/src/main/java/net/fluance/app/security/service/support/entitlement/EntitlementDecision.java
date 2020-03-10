/**
 * 
 */
package net.fluance.app.security.service.support.entitlement;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntitlementDecision {

	INDETERMINATE("Indeterminate", ""),
	NOT_APPLICABLE("NotApplicable", ""),
	DENY("Deny", ""),
	PERMIT("Permit", "");
	
	private String decision;
	private String description;

	/**
	 * @param decision
	 * @param description
	 */
	private EntitlementDecision(String decision, String description) {
		this.decision = decision;
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	@JsonValue
	public String getDecision() {
		return decision;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EntitlementDecision fromDecision(String decisionStr) {
		EntitlementDecision decision = null;
		EntitlementDecision[] decisions = EntitlementDecision.values();
		for(EntitlementDecision currentDecision : decisions) {
			if(currentDecision.getDecision().equals(decisionStr)) {
				decision = currentDecision;
				break;
			}
		}
		return decision;
	}
}
