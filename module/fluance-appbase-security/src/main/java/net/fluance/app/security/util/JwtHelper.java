/**
 * 
 */
package net.fluance.app.security.util;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.fluance.commons.json.jwt.JWTUtils;
import net.fluance.commons.json.jwt.JWTUtils.JwtPart;
import net.fluance.commons.net.MediaType;
import net.fluance.commons.net.MimeUtils;
import net.fluance.app.security.auth.AuthorizationStrategyEnum;
import net.fluance.commons.codec.PKIUtils;

public class JwtHelper {

	private static Logger LOGGER = LogManager.getLogger(JwtHelper.class);

	public static final String USERPROFILE_KEY = "profile";
	public static final String USERNAME_KEY = "username";
	public static final String DOMAIN_KEY = "domain";
	
	@Value("${server.ssl.key-alias}")
	private String sslKeyAlias;
	@Value("${server.ssl.key-password}")
	private String sslKeyPassword;

	@Value("${app.jwt.issuer}")
	private String jwtIssuer;
	@Value("${jwt.default.type}")
	private String defaultJwtType;
	@Value("${jwt.default.signing-algorithm}")
	private String defaultJwtSigningAlgorithm;
	
	/**
	 * 
	 * @param type
	 * @param signingAlgorithm
	 * @param signed
	 * @param extraHeaderFields
	 * @param payload
	 * @return
	 * @throws javax.security.cert.CertificateException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	public String build(String type, String signingAlgorithm, boolean signed, Map<String, Object> extraHeaderFields,
			Map<String, Object> payload) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, javax.security.cert.CertificateException {

		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode extraHeaderFieldsJson = (extraHeaderFields != null) ? objectMapper.valueToTree(extraHeaderFields) : objectMapper.createObjectNode();

		JsonNode payloadJson = objectMapper.valueToTree(payload);

		return build(type, signingAlgorithm, signed, extraHeaderFieldsJson, payloadJson);

	}
	
	/**
	 * @deprecated Use build(JsonNode, JsonNode) instead. Kept for backward compatibility and as utility.
	 * @param type
	 * @param signingAlgorithm
	 * @param signed
	 * @param extraHeaderFields
	 * @param payload
	 * @return
	 * @throws javax.security.cert.CertificateException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	public String build(String type, String signingAlgorithm, boolean signed, JsonNode extraHeaderFields,
			JsonNode payload) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, javax.security.cert.CertificateException {

		PrivateKey signingKey = null;

		if (signed) {
			signingKey = PKIUtils.readPrivateKey(sslKeyAlias, sslKeyPassword);
		}
		// Prepare and/or fix the header
		JsonNode headerJson = buildHeader(type, signingAlgorithm, signed, extraHeaderFields);

		String jwt = JWTUtils.buildToken(signingKey, headerJson, payload);
		
		LOGGER.info("Built JWT: " + jwt);
		
		return jwt;
	}

	/**
	 * 
	 * @param header
	 * @param payload
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws javax.security.cert.CertificateException
	 */
	public String build(JsonNode header,
			JsonNode payload) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, javax.security.cert.CertificateException {
		
		PrivateKey signingKey = null;
		
		if (header.has(JWTUtils.SIGNING_ALGORITHM_KEY) && header.get(JWTUtils.SIGNING_ALGORITHM_KEY).isTextual() && !header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue().equalsIgnoreCase(JWTUtils.NO_SIGNATURE_ALGORITHM_VALUE)) {
			signingKey = PKIUtils.readPrivateKey(sslKeyAlias, sslKeyPassword);
		}
		
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		LOGGER.info("[build(JsonNode,JsonNode)] Built JWT: " + jwt);
		
		return jwt;
	}
	
	/**
	 * 
	 * @param header
	 * @param payload
	 * @param keyStoreFile
	 * @param keyStorePassword
	 * @param keyStoreType
	 * @param keyAlias
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws javax.security.cert.CertificateException
	 */
	public String build(JsonNode header,
			JsonNode payload, File keyStoreFile, String keyStorePassword, String keyStoreType, String keyAlias) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, javax.security.cert.CertificateException {
		
		PrivateKey signingKey = null;
		
		
		if (header.has(JWTUtils.SIGNING_ALGORITHM_KEY) && header.get(JWTUtils.SIGNING_ALGORITHM_KEY).isTextual() && !header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue().equalsIgnoreCase(JWTUtils.NO_SIGNATURE_ALGORITHM_VALUE)) {
			signingKey = PKIUtils.readPrivateKey(keyStoreFile, keyStorePassword, keyStoreType, keyAlias, sslKeyPassword);			
		}
		
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		LOGGER.info("[build(JsonNode,JsonNode)] Built JWT: " + jwt);
		
		return jwt;
	}
	
	/**
	 * 
	 * @deprecated Use build(JsonNode, JsonNode) instead. Kept for backward compatibility and as utility.
	 * @param jwtTokenRequestPayload
	 * @param user
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws javax.security.cert.CertificateException
	 */
	public String build(String type, String signingAlgorithm, boolean signed, long expirationTime, ObjectNode extraHeaderFields, ObjectNode originalPayload, String username, String domain) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, javax.security.cert.CertificateException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode payload = (originalPayload != null) ? originalPayload : objectMapper.createObjectNode();
		
		String issuer = (payload.has(JWTUtils.ISSUER_KEY) && payload.get(JWTUtils.ISSUER_KEY) != null && payload.get(JWTUtils.ISSUER_KEY).isTextual() && !payload.get(JWTUtils.ISSUER_KEY).textValue().isEmpty()) ? payload.get(JWTUtils.ISSUER_KEY).textValue() : jwtIssuer;
		
		payload.put(JWTUtils.ISSUER_KEY, issuer);
		payload.put(USERNAME_KEY, username);
		payload.put(DOMAIN_KEY, domain);
		payload.put(JWTUtils.EXPIRATION_TIME_KEY, expirationTime);
		
		String jwt = build(type, signingAlgorithm, signed, extraHeaderFields, payload);

		return jwt;
		
	}

	/**
	 * Prepares the JWT header
	 * 
	 * @param originalHeader
	 * @return
	 */
	public JsonNode buildHeader(String type, String signingAlgo, boolean signed, JsonNode extraHeaderFields) {

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode header = objectMapper.createObjectNode();

		// Set signature algorithm and type to default values if none provided,
		// provided values otherwise
		String jwtSigningAlgo = (signingAlgo == null || signingAlgo.isEmpty()) ? defaultJwtSigningAlgorithm
				: signingAlgo;
		String jwtType = (type == null || type.isEmpty()) ? defaultJwtType : type;

		header.put(JWTUtils.SIGNING_ALGORITHM_KEY, (signed) ? jwtSigningAlgo : JWTUtils.NO_SIGNATURE_ALGORITHM_VALUE);
		header.put(JWTUtils.TYPE_KEY, jwtType);

		// Then add all other header fields
		if(extraHeaderFields != null && extraHeaderFields.fields().hasNext()) {
			Iterator<String> extraFieldsIter = extraHeaderFields.fieldNames();
			while (extraFieldsIter.hasNext()) {
				String fieldName = extraFieldsIter.next();
				if (!JWTUtils.TYPE_KEY.equalsIgnoreCase(fieldName) && !JWTUtils.SIGNING_ALGORITHM_KEY.equalsIgnoreCase(fieldName)) {
					header.set(fieldName, extraHeaderFields.get(fieldName));
				}
			}
		}

		return header;
	}

	/**
	 * 
	 * @param originalHeader
	 * @return
	 */
	public JsonNode buildHeader(String type, String signingAlgo, boolean signed, Map<String, Object> originalHeader) {

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode headerJson = (originalHeader != null) ? objectMapper.valueToTree(originalHeader) : objectMapper.createObjectNode();

		return buildHeader(type, signingAlgo, signed, headerJson);
	}

	/**
	 * 
	 * @param jwt
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static boolean isValid(String jwt) throws JsonProcessingException, IOException{

		boolean valid = JWTUtils.isJwt(jwt);
		JsonNode header = JWTUtils.getPart(jwt, JwtPart.HEADER);
		
		if(header.has(JWTUtils.TYPE_KEY) && header.get(JWTUtils.TYPE_KEY) != null) {
			MediaType mediaType = MimeUtils.byName(header.get(JWTUtils.TYPE_KEY).textValue());
			valid = valid && (mediaType != null);
		}
		
		return valid;
	}

	public static AuthorizationStrategyEnum determineStrategy(String accessToken) throws JsonProcessingException, IOException{
		JsonNode pid = JWTUtils.getPart(accessToken, JwtPart.PAYLOAD).get("pid");
		if(pid != null && !pid.isNull()){
			return AuthorizationStrategyEnum.SINGLE_PATIENT;
		} else {
			JsonNode username = JWTUtils.getPart(accessToken, JwtPart.PAYLOAD).get("username");
			JsonNode domain = JWTUtils.getPart(accessToken, JwtPart.PAYLOAD).get("domain");
			if(username != null && !username.isNull() && domain != null && !domain.isNull()){
				return AuthorizationStrategyEnum.AUTHENTICATION_TRUSTED_PARTNER;
			}
		}
		return null;
	}
}
