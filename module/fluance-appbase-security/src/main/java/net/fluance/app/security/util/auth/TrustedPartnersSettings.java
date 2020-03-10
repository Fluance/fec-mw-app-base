/**
 * 
 */
package net.fluance.app.security.util.auth;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TrustedPartnersSettings {

	public static final String CONF_PARTNERS_FIELD = "partners";
	
	@JsonProperty("partners")
	private Map<String, TrustedPartner> trustedPartners ;

	public TrustedPartnersSettings() {
		this.trustedPartners = new HashMap<>();
	}

	public TrustedPartnersSettings(Map<String, TrustedPartner> trustedPartners) {
		this.trustedPartners = trustedPartners;
	}

	public TrustedPartnersSettings(ObjectNode jsonConfigNode) throws JsonProcessingException {
		this.trustedPartners = new HashMap<>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		if(!isJsonConfigNodeValid(jsonConfigNode)) {
			throw new IllegalArgumentException("Invalid configuration: " + objectMapper.writeValueAsString(jsonConfigNode));
		}
		
		ArrayNode partnersConfNode = (ArrayNode) jsonConfigNode.get(CONF_PARTNERS_FIELD);
		for(JsonNode partnerNode : partnersConfNode) {
			TrustedPartner partner = new TrustedPartner((ObjectNode) partnerNode);
			this.trustedPartners.put(partner.getName(), partner);
		}
	}
	
	public Map<String, TrustedPartner> getTrustedPartners() {
		return trustedPartners;
	}

	public void setTrustedPartners(Map<String, TrustedPartner> trustedPartners) {
		this.trustedPartners = trustedPartners;
	}

	public static boolean isJsonConfigNodeValid(ObjectNode jsonNode) {
		return jsonNode != null && jsonNode.has(CONF_PARTNERS_FIELD) && jsonNode.get(CONF_PARTNERS_FIELD).isArray();
	}
}
