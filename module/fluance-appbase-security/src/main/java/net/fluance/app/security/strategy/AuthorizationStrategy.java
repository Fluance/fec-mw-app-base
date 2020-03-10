package net.fluance.app.security.strategy;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.security.auth.AuthorizationStrategyEnum;

public interface AuthorizationStrategy {

	public User authorize(Object token, Object... objects) throws Exception;

}
