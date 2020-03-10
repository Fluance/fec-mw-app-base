/**
 * 
 */
package net.fluance.app.security.service.auth;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.fluance.app.security.auth.AuthorizationStrategyEnum;


@Service
@Component
public interface IAuthorizationService {

	Object validateAuthorization(Object arg, AuthorizationStrategyEnum authorizationStrategy) throws Exception;
	
}
