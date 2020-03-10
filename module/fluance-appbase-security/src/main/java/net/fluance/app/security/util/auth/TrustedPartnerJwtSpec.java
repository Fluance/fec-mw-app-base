/**
 * 
 */
package net.fluance.app.security.util.auth;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.fluance.commons.json.JacksonUtils;

public class TrustedPartnerJwtSpec {

	public static final String CONF_HEADER_FIELD = "header";
	public static final String CONF_PAYLOAD_FIELD = "payload";
	public static final String CONF_SIGNING_ALGORITHM_FIELD = "signing-algorithm";
	public static final String CONF_TYPE_FIELD = "type";
	public static final String CONF_VALIDITY_PERIOD_FIELD = "validity-period";
	public static final String CONF_DYNAMIC_CLAIMS_FIELD = "dynamic-claims";

	public static class JwtHeader {
		@JsonProperty(CONF_SIGNING_ALGORITHM_FIELD)
		private String signingAlgorithm;
		private String type;
		@JsonProperty(CONF_DYNAMIC_CLAIMS_FIELD)
		private Set<String> dynamicClaims;

		public JwtHeader() {
			dynamicClaims = new HashSet<>();
		}

		public JwtHeader(String signingAlgorithm, String type, Set<String> dynamicClaims) {
			super();
			this.signingAlgorithm = signingAlgorithm;
			this.type = type;
			this.dynamicClaims = dynamicClaims;
		}

		public String getSigningAlgorithm() {
			return signingAlgorithm;
		}

		public void setSigningAlgorithm(String signingAlgorithm) {
			this.signingAlgorithm = signingAlgorithm;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Set<String> getDynamicClaims() {
			return dynamicClaims;
		}

		public void setDynamicClaims(Set<String> dynamicClaims) {
			this.dynamicClaims = dynamicClaims;
		}
	}

	public static class JwtPayload {
		@JsonProperty(CONF_VALIDITY_PERIOD_FIELD)
		private int validityPeriod;
		@JsonProperty(CONF_DYNAMIC_CLAIMS_FIELD)
		private Set<String> dynamicClaims;

		public JwtPayload() {
			dynamicClaims = new HashSet<>();
		}

		public JwtPayload(int validityPeriod, Set<String> dynamicClaims) {
			this.validityPeriod = validityPeriod;
			this.dynamicClaims = dynamicClaims;
		}

		public int getValidityPeriod() {
			return validityPeriod;
		}

		public void setValidityPeriod(int validityPeriod) {
			this.validityPeriod = validityPeriod;
		}

		public Set<String> getDynamicClaims() {
			return dynamicClaims;
		}

		public void setDynamicClaims(Set<String> dynamicClaims) {
			this.dynamicClaims = dynamicClaims;
		}
	}

	private JwtHeader header;
	private JwtPayload payload;

	public TrustedPartnerJwtSpec() {
	}

	public TrustedPartnerJwtSpec(ObjectNode jsonConfigNode) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		if (!isJsonConfigNodeValid(jsonConfigNode)) {
			throw new IllegalArgumentException(
					"Invalid configuration: " + objectMapper.writeValueAsString(jsonConfigNode));
		}

		// Header conf
		JwtHeader header = new JwtHeader();
		String signingAlgorithm = jsonConfigNode.get(CONF_HEADER_FIELD).get(CONF_SIGNING_ALGORITHM_FIELD).textValue();
		String type = jsonConfigNode.get(CONF_HEADER_FIELD).get(CONF_TYPE_FIELD).textValue();
		header.setSigningAlgorithm(signingAlgorithm);
		header.setType(type);
		if (jsonConfigNode.get(CONF_HEADER_FIELD).has(CONF_DYNAMIC_CLAIMS_FIELD)
				&& jsonConfigNode.get(CONF_HEADER_FIELD).get(CONF_DYNAMIC_CLAIMS_FIELD).isArray()) {
			Iterator<JsonNode> headerDynamicClaimsIterator = jsonConfigNode.get(CONF_HEADER_FIELD)
					.get(CONF_DYNAMIC_CLAIMS_FIELD).elements();
			@SuppressWarnings("unchecked")
			List<String> headerDynamicClaimsArg = JacksonUtils
					.textNodesToStringsList(IteratorUtils.toList(headerDynamicClaimsIterator));
			Set<String> headerDynamicClaims = new HashSet<>(headerDynamicClaimsArg);
			header.setDynamicClaims(headerDynamicClaims);
		}

		// Payload conf
		JwtPayload payload = new JwtPayload();
		int validityPeriod = jsonConfigNode.get(CONF_PAYLOAD_FIELD).get(CONF_VALIDITY_PERIOD_FIELD).intValue();
		payload.setValidityPeriod(validityPeriod);
		if (jsonConfigNode.get(CONF_PAYLOAD_FIELD).has(CONF_DYNAMIC_CLAIMS_FIELD)
				&& jsonConfigNode.get(CONF_PAYLOAD_FIELD).get(CONF_DYNAMIC_CLAIMS_FIELD).isArray()) {
			Iterator<JsonNode> payloadDynamicClaimsIterator = jsonConfigNode.get(CONF_PAYLOAD_FIELD)
					.get(CONF_DYNAMIC_CLAIMS_FIELD).elements();
			@SuppressWarnings("unchecked")
			List<String> payloadDynamicClaimsArg = JacksonUtils
					.textNodesToStringsList(IteratorUtils.toList(payloadDynamicClaimsIterator));
			Set<String> payloadDynamicClaims = new HashSet<>(payloadDynamicClaimsArg);
			payload.setDynamicClaims(payloadDynamicClaims);
		}
		this.header = header;
		this.payload = payload;
	}

	public TrustedPartnerJwtSpec(JwtHeader header, JwtPayload payload) {
		super();
		this.header = header;
		this.payload = payload;
	}

	public JwtHeader getHeader() {
		return header;
	}

	public void setHeader(JwtHeader header) {
		this.header = header;
	}

	public JwtPayload getPayload() {
		return payload;
	}

	public void setPayload(JwtPayload payload) {
		this.payload = payload;
	}

	private static boolean isJsonConfigNodeValid(ObjectNode jsonNode) {
		return jsonNode != null && jsonNode.has(CONF_HEADER_FIELD) && jsonNode.get(CONF_HEADER_FIELD).isObject()
				&& jsonNode.has(CONF_PAYLOAD_FIELD) && jsonNode.get(CONF_PAYLOAD_FIELD).isObject()
				&& jsonNode.get(CONF_HEADER_FIELD).has(CONF_SIGNING_ALGORITHM_FIELD)
				&& jsonNode.get(CONF_HEADER_FIELD).get(CONF_SIGNING_ALGORITHM_FIELD).isTextual()
				&& jsonNode.get(CONF_HEADER_FIELD).has(CONF_TYPE_FIELD)
				&& jsonNode.get(CONF_HEADER_FIELD).get(CONF_TYPE_FIELD).isTextual()
				&& jsonNode.get(CONF_PAYLOAD_FIELD).has(CONF_DYNAMIC_CLAIMS_FIELD)
				&& jsonNode.get(CONF_PAYLOAD_FIELD).get(CONF_DYNAMIC_CLAIMS_FIELD).isArray();
	}
}
