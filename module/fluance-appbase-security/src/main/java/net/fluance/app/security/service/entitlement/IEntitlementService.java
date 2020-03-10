/**
 * 
 */
package net.fluance.app.security.service.entitlement;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.fluance.app.security.service.support.entitlement.EntitlementDecision;

@Service
@Component
public interface IEntitlementService {

	EntitlementDecision getDecision(Object... args) throws Exception;
	
}
