package net.fluance.app.log;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.model.LogModel;

@Service
public interface LogService {

	public void log(ResourceType resourceType, Object payload, Map<String, String[]> parameters, User user, String httpMethod, String uri, String resourceId);

	/**
	 * Should be implemented only by Mw-app LogServices. Due to Spring IoC issue
	 *
	 * This method must be tagged as {@link Async} when it will be implemented because the return class {@link Future}
	 *
	 * @param resourceType
	 * @param payload
	 * @param parameters
	 * @param user
	 * @param httpMethod
	 * @param parentPid
	 * @param parentVn
	 */
	public Future<String> log(ResourceType resourceType, Object payload, Map<String, String[]> parameters, User user, String httpMethod, Long parentPid, Long parentVn, String uri, String resourceId);

	LogModel buildLogModel(ResourceType resourceType, Object payload, Map<String, String[]> parameters, User user, String httpMethod, String uri, String resourceId) throws UnsupportedOperationException;

}
