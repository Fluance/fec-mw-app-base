package net.fluance.app.log;

import java.util.Map;

import org.springframework.stereotype.Component;

import net.fluance.app.data.model.identity.User;

@Component
public interface LogCreatorFactory {

	public LogCreator instanciateLogCreator(ResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri);
}
