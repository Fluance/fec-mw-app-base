/**
 * 
 */
package net.fluance.app.security.service.xacml;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import net.fluance.app.security.service.support.entitlement.EntitlementDecision;

public interface XacmlPDP {
	
	/**
	 * Evaluates permissions of user on a given resource and for a given action
	 * @param resource
	 * @param action
	 * @param subjectId
	 * @param subjectRoles
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public EntitlementDecision evaluate(String resource, String action, String subjectId, List<String> subjectRoles) throws Exception;
	
}
