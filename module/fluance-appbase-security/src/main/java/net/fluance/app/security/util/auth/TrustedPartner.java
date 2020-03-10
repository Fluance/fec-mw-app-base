/**
 * 
 */
package net.fluance.app.security.util.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TrustedPartner {

	public static final String CONF_NAME_FIELD = "name";
	public static final String CONF_SSL_KEY_ALIAS_FIELD = "ssl-key-alias";
	public static final String CONF_JWT_SPEC_FIELD = "jwt-spec";

	private String name;
	@JsonProperty(CONF_SSL_KEY_ALIAS_FIELD)
	private String sslKeyAlias;
	@JsonProperty(CONF_JWT_SPEC_FIELD)
	private TrustedPartnerJwtSpec jwtSpec;

	public TrustedPartner() {}

	public TrustedPartner(String name, String sslKeyAlias, TrustedPartnerJwtSpec jwtSpec) {
		super();
		this.name = name;
		this.sslKeyAlias = sslKeyAlias;
		this.jwtSpec = jwtSpec;
	}

	public TrustedPartner(ObjectNode jsonConfigNode) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if(!isJsonConfigNodeValid(jsonConfigNode)) {
			throw new IllegalArgumentException("Invalid configuration: " + objectMapper.writeValueAsString(jsonConfigNode));
		}
		
		this.name = jsonConfigNode.get(CONF_NAME_FIELD).textValue();
		this.sslKeyAlias = jsonConfigNode.get(CONF_SSL_KEY_ALIAS_FIELD).textValue();
		
		JsonNode jwtSpecNode = jsonConfigNode.get(CONF_JWT_SPEC_FIELD);
		
		this.jwtSpec = new TrustedPartnerJwtSpec((ObjectNode) jwtSpecNode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSslKeyAlias() {
		return sslKeyAlias;
	}

	public void setSslKeyAlias(String sslKeyAlias) {
		this.sslKeyAlias = sslKeyAlias;
	}

	public TrustedPartnerJwtSpec getJwtSpec() {
		return jwtSpec;
	}

	public void setJwtSpec(TrustedPartnerJwtSpec jwtSpec) {
		this.jwtSpec = jwtSpec;
	}

	public static boolean isJsonConfigNodeValid(ObjectNode jsonNode) {
		return jsonNode != null && jsonNode.has(CONF_NAME_FIELD) && jsonNode.get(CONF_NAME_FIELD).isTextual()
				&& jsonNode.has(CONF_SSL_KEY_ALIAS_FIELD) && jsonNode.get(CONF_SSL_KEY_ALIAS_FIELD).isTextual()
				&& jsonNode.has(CONF_JWT_SPEC_FIELD) && jsonNode.get(CONF_JWT_SPEC_FIELD).isObject(); 
	}

}
