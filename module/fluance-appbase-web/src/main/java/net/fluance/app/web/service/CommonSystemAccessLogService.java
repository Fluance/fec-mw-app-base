package net.fluance.app.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.log.ResourceType;
import net.fluance.app.log.model.LogModel;
import net.fluance.commons.lang.FluancePrintingMap;

@Service
public class CommonSystemAccessLogService implements LogService{

	public static final String PID_KEY 		= "pid";
	public static final String VISITNB_KEY 	= "vnb";

	private final Logger logger 		= LogManager.getLogger();
	private final Logger errorLogger 	= LogManager.getRootLogger();

	@Async
	@Override
	public void log(ResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri, String resourceId) {
		log(resourceType, payload, params, user, httpMethod, null, null, uri, resourceId);
	}

	@Async
	@Override
	public Future<String> log(ResourceType resourceType, Object payload, Map<String, String[]> parameters, User user, String httpMethod, Long parentPid, Long parentVn, String uri, String resourceId) {
		Map<String, String[]> notLockedparams = new HashMap<>();
		if (parameters != null) {
			notLockedparams.putAll(parameters);
		}
		if (parentPid != null) {
			notLockedparams.put(PID_KEY, new String[]{parentPid.toString()});
		}
		if (parentVn != null) {
			notLockedparams.put(VISITNB_KEY, new String[]{parentVn.toString()});
		}
		try {
			LogModel log = buildLogModel(resourceType, payload, notLockedparams, user, httpMethod, uri, resourceId);
			initializeLog4jThreadContext(log);
			logger.info("Saving System Access Log.");
		} catch (Exception e) {
			errorLogger.error("Unable to save System Access Log", e);
		}

		return new AsyncResult<>("Log Done");
	}

	@Override
	public LogModel buildLogModel(ResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri, String resourceId) throws UnsupportedOperationException {
		String parentPid = (params!=null && params.get(PID_KEY) != null) ? params.get(PID_KEY)[0] : "" ;
		String parentVn = (params!=null && params.get(VISITNB_KEY) != null) ? params.get(VISITNB_KEY)[0] : "";
		String objectType = resourceType !=null ? resourceType.getResourceType() : "";
		String appUser = user.getDomain() + "/" + user.getUsername();
		return new LogModel(appUser, resourceId, httpMethod, parentPid, parentVn, uri, new FluancePrintingMap<>(params).toString(), objectType);
	}

	private void initializeLog4jThreadContext(LogModel log) {
		ThreadContext.put("username", log.getUserName());
		ThreadContext.put("resourceType", log.getResourceType());
		ThreadContext.put("resourceId", log.getResourceId());
		ThreadContext.put("httpMethod", log.getHttpMethod());
		ThreadContext.put("uri", log.getUri());
		ThreadContext.put("parameters", log.getParameters());
		if(log.getParentVisitNb() != null){
			ThreadContext.put("parentVisitNb", log.getParentVisitNb());
		}
		if(log.getParentPid() != null){
			ThreadContext.put("parentPid", log.getParentPid());
		}
	}
}
