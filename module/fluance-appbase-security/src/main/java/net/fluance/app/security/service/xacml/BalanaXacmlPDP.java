/**
 * 
 */
package net.fluance.app.security.service.xacml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.w3c.dom.Document;
import org.wso2.balana.Balana;
import org.wso2.balana.ConfigurationStore;
import org.wso2.balana.PDP;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

import net.fluance.app.security.service.support.entitlement.EntitlementDecision;
import net.fluance.commons.xml.XMLUtils;
import net.fluance.commons.xml.XacmlUtils;


public class BalanaXacmlPDP implements XacmlPDP {
	private static final Logger LOGGER = LogManager.getLogger(BalanaXacmlPDP.class);
	
	public static final String POLICY_DIR = "/conf/xacml-policies";
	public static final String CONFIG_FILE = "/conf/xacml-pdp-config.xml";
	private static final List<String> NO_ROLES_LIST = new ArrayList<>();
	
	@Value("${permissionCacheEnabled:false}")
	public boolean permissionCacheEnabled;
	
	static {
		NO_ROLES_LIST.add("");
	}
	
	public BalanaXacmlPDP() {
		this(CONFIG_FILE, POLICY_DIR);
	}
	
	/**
	 * @param policyDir
	 * @param pdpConfigFile
	 */
	public BalanaXacmlPDP(String pdpConfigFile, String policyDir) {
		
		LOGGER.info("[XACML_CONFIG] Previous PDP config file in system properties: " + System.getProperty(ConfigurationStore.PDP_CONFIG_PROPERTY));
		LOGGER.info("[XACML_CONFIG] Previous policy directory in system properties: " + System.getProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY));

		System.setProperty(ConfigurationStore.PDP_CONFIG_PROPERTY, pdpConfigFile);
    	System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyDir);
    	
    	LOGGER.info("[XACML_CONFIG] Set PDP config file in system properties: " + ConfigurationStore.PDP_CONFIG_PROPERTY);
		LOGGER.info("[XACML_CONFIG] Set policy directory in system properties: " + FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY);
		
		LOGGER.info("[XACML_CONFIG] PDP config file real path: " + (new File(pdpConfigFile)).getAbsolutePath());
		LOGGER.info("[XACML_CONFIG] Policy directory real pathy: " + (new File(policyDir)).getAbsolutePath());
	}

	@Override
	@Cacheable(value="PermissionEvaluation",  condition = "#root.target.permissionCacheEnabled")
	public synchronized EntitlementDecision evaluate(String resource, String action, String subjectId, List<String> subjectRoles)
			throws Exception {
    	Balana balana = Balana.getInstance();
    	 
    	PDP pdp = new PDP(balana.getPdpConfig());
    	
    	String request = XacmlUtils.simpleRequest(resource, action.toUpperCase(), subjectId, (subjectRoles==null || subjectRoles.isEmpty()) ? NO_ROLES_LIST : subjectRoles);

    	String response = pdp.evaluate(request);
    	
		Document responseDoc = XMLUtils.xmlFromString(response);
		String xpathDecisionValueExpression = "/Response/Result/Decision";
		String decisionValue = XMLUtils.queryString(responseDoc.getDocumentElement(), xpathDecisionValueExpression);
		
    	return EntitlementDecision.fromDecision(decisionValue);
	}

}
