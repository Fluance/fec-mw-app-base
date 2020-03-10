/**
 * 
 */
package net.fluance.app.security.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.fluance.app.security.Application;
import net.fluance.app.test.AbstractTest;
import net.fluance.commons.codec.Base64Utils;
import net.fluance.commons.codec.PKIUtils;
import net.fluance.commons.json.jwt.JWTUtils;
import net.fluance.commons.json.jwt.JWTUtils.JwtPart;;


@SpringApplicationConfiguration(classes = Application.class)
public class JwtHelperTest extends AbstractTest{

	private static final Logger LOGGER = LogManager.getLogger("net.fluance.commons.codec.PKIUtilsTest");
	
	private final String certificateAlias = "fluance";
	
	@Value("${server.ssl.key-alias}")
	private String sslKeyAlias;
	@Value("${server.ssl.key-password}")
	private String sslKeyPassword;

	@Value("${jwt.default.type}")
	private String defaultJwtType;
	
	@Value("${jwt.signing-algorithm.supported}")
	private String jwtSupportedSigningAlgo;
	@Value("${jwt.signing-algorithm.unsupported}")
	private String jwtUnsupportedSigningAlgo;
	@Value("${jwt.signing-algorithm.illegal}")
	private String jwtIllegalSigningAlgo;
	@Value("${jwt.signing-algorithm.other}")
	private String jwtOtherSigningAlgo;

	@Value("${jwt.type.other}")
	private String jwtOtherType;
	
	@Value("${jwt.header}")
	private String jwtHeader;
	@Value("${jwt.payload}")
	private String jwtPayload;
	@Value("${jwt.unsigned}")
	private String jwtUnsigned;
	
	@Value("${jwt.default.signing-algorithm}")
	private String defaultJwtSigningAlgorithm;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	private PublicKey publicKey;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
        String keyStorePath = new File(classLoader.getResource("keystore.jks").getFile()).getAbsolutePath();
        assertTrue(new File(keyStorePath).exists());
        String trustStorePath = new File(classLoader.getResource("truststore.jks").getFile()).getAbsolutePath();
        assertTrue(new File(trustStorePath).exists());
       
        LOGGER.info("keyStorePath: " + keyStorePath);
        LOGGER.info("trustStorePath: " + trustStorePath);
		
		System.setProperty("javax.net.ssl.keyStore", keyStorePath);
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.keyStorePassword", "fluance");	
		
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStorePassword", "fluance");	
		
		publicKey = PKIUtils.readPublicKey(certificateAlias, PKIUtils.DEFAULT_CERTIFICATE_TYPE);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildUnsigned() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode payloadJson = mapper.readTree(jwtPayload);
		
		String jwt = jwtHelper.build(defaultJwtType, defaultJwtSigningAlgorithm, false, null, payloadJson);
		assertNotNull(jwt);
		//An unsigned JWT must finish with a .
		assertTrue(jwt.endsWith("."));
		
		JsonNode receivedHeader = JWTUtils.getPart(jwt, JwtPart.HEADER);
		assertNotNull(receivedHeader);
		// The algo must be NONE since the token is not signed
		assertNotEquals(mapper.readTree(jwtHeader), receivedHeader);
		ObjectNode noAlgoUpperHeader = (ObjectNode) mapper.readTree(jwtHeader);
		ObjectNode noAlgoLowerHeader = (ObjectNode) mapper.readTree(jwtHeader);
		noAlgoUpperHeader.put(JWTUtils.SIGNING_ALGORITHM_KEY, JWTUtils.NO_SIGNATURE_ALGORITHM_VALUE);
		noAlgoLowerHeader.put(JWTUtils.SIGNING_ALGORITHM_KEY, JWTUtils.NO_SIGNATURE_ALGORITHM_VALUE.toLowerCase());
		assertTrue(receivedHeader.equals(noAlgoUpperHeader) || receivedHeader.equals(noAlgoLowerHeader));
		
		JsonNode receivedPayload = JWTUtils.getPart(jwt, JwtPart.PAYLOAD);
		assertEquals(mapper.readTree(jwtPayload), receivedPayload);

		String signature = JWTUtils.getEncodedPart(jwt, JwtPart.SIGNATURE);
		assertEquals("", signature);
	}

	@Test
	public void testBuildSigned() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode payloadJson = mapper.readTree(jwtPayload);
		
		// Default header
		String jwt = jwtHelper.build(defaultJwtType, jwtSupportedSigningAlgo, true, null, payloadJson);
		assertNotNull(jwt);
		JsonNode receivedHeader = JWTUtils.getPart(jwt, JwtPart.HEADER);
		assertNotNull(receivedHeader);
		// The algo and type fields must be present in the header
		assertNotNull(receivedHeader.get(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertNotNull(receivedHeader.get(JWTUtils.TYPE_KEY));
		// The algo and type fields must have the default values
		assertEquals(defaultJwtSigningAlgorithm, receivedHeader.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
		assertEquals(defaultJwtType, receivedHeader.get(JWTUtils.TYPE_KEY).textValue());
		//The payload must be the same
		JsonNode receivedPayload = JWTUtils.getPart(jwt, JwtPart.PAYLOAD);
		assertEquals(mapper.readTree(jwtPayload), receivedPayload);
		// Verify signature
		assertTrue(JWTUtils.verifySignature(jwt, receivedHeader.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue(), publicKey));
	}
	
	@Test
	public void testBuildHeader() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode headerExtraFields = mapper.createObjectNode();
		
		// If type is null
		JsonNode header = jwtHelper.buildHeader(null, jwtSupportedSigningAlgo, true, headerExtraFields);
		assertNotNull(header);
		assertTrue(header.has(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertTrue(header.has(JWTUtils.TYPE_KEY));
		assertEquals(jwtSupportedSigningAlgo, header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
		// Type must be default
		assertNotNull(defaultJwtType, header.get(JWTUtils.TYPE_KEY));

		// If signing algo is null
		header = jwtHelper.buildHeader(defaultJwtType, null, true, headerExtraFields);
		assertNotNull(header);
		assertTrue(header.has(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertTrue(header.has(JWTUtils.TYPE_KEY));
		// Signing algorithm must be default
		assertEquals(defaultJwtSigningAlgorithm, header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
		assertNotNull(defaultJwtType, header.get(JWTUtils.TYPE_KEY));
	
		// If signing algo and type are both null
		header = jwtHelper.buildHeader(null, null, true, headerExtraFields);
		assertNotNull(header);
		assertTrue(header.has(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertTrue(header.has(JWTUtils.TYPE_KEY));
		// Both signing algorithm and type must be default
		assertEquals(defaultJwtSigningAlgorithm, header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
		assertNotNull(defaultJwtType, header.get(JWTUtils.TYPE_KEY));

		// If signing algo and type are both null
		header = jwtHelper.buildHeader("", "", true, headerExtraFields);
		assertNotNull(header);
		assertTrue(header.has(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertTrue(header.has(JWTUtils.TYPE_KEY));
		// Both signing algorithm and type must be default
		assertEquals(defaultJwtSigningAlgorithm, header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
		assertNotNull(defaultJwtType, header.get(JWTUtils.TYPE_KEY));

		// If signing algo and type are both provided
		header = jwtHelper.buildHeader(jwtOtherType, jwtOtherSigningAlgo, true, headerExtraFields);
		assertNotNull(header);
		assertTrue(header.has(JWTUtils.SIGNING_ALGORITHM_KEY));
		assertTrue(header.has(JWTUtils.TYPE_KEY));
		// Both signing algorithm and type must be default
		assertNotNull(jwtOtherType, header.get(JWTUtils.TYPE_KEY));
		assertEquals(jwtOtherSigningAlgo, header.get(JWTUtils.SIGNING_ALGORITHM_KEY).textValue());
	}
	
}
