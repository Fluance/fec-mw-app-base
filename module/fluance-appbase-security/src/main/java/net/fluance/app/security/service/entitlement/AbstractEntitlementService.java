/**
 * 
 */
package net.fluance.app.security.service.entitlement;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.app.security.service.support.entitlement.EntitlementDecision;
import net.fluance.app.security.service.support.entitlement.PermissionEvaluateRequestBody;
import net.fluance.app.security.service.support.entitlement.PreparedPermissionEvaluateRequestBody;
import net.fluance.commons.net.HttpUtils;

public abstract class AbstractEntitlementService implements IEntitlementService {

	@Value("${permission.service.url}")
	private String permissionServiceUrl;
	
	@Value("${permission.service.url.AuthorizationHeader}")
	private String permissionAuthorizationHeader;
	
	/**
	 * Arguments indexes in parameters array:
	 * [0]: resource
	 * [1]: username
	 * [2]: domain name
	 * [3]: action
	 * [4]: user roles
	 * Only resource, username, domain name and action are required
	 */
	@Override
	public EntitlementDecision getDecision(Object... args) throws Exception {
		if(!validateDecisionRequestArguments(args)) {
			throw new IllegalArgumentException("At least username, domain, resource and action are required");			
		}
		
		URI permissionSvcUri = HttpUtils.buildUri(permissionServiceUrl);
		
		PermissionEvaluateRequestBody evalReqBody = extractEvaluateRequestBody(args);
		
		Header authorizationHeader = new BasicHeader("Authorization", permissionAuthorizationHeader);
		HttpRequestBase request = buildPermissionRequest(permissionSvcUri, evalReqBody, HttpGet.METHOD_NAME);
		request.setHeader(authorizationHeader);
		
		CloseableHttpResponse response = HttpUtils.send(request, true);
		
		String responseBodyStr = EntityUtils.toString(response.getEntity());
		
		EntitlementDecision decision = new ObjectMapper().readValue(responseBodyStr, EntitlementDecision.class);
		
		return decision;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PermissionEvaluateRequestBody extractEvaluateRequestBody(Object... args) {
		PermissionEvaluateRequestBody evalReqBody = null;
		if(args.length == 4) {
			evalReqBody = new PermissionEvaluateRequestBody((String)args[0], (String)args[1], (String) args[2], (String) args[3]);
		} else if(args.length == 5) {
			evalReqBody = new PreparedPermissionEvaluateRequestBody((String)args[0], (String)args[1], (String) args[2], (String) args[3], (List<String>)args[4]);
		}
		return evalReqBody;
	}
	
	/**
	 * 
	 * @param permissionSvcUri
	 * @param evalReqBody
	 * @param method
	 * @return
	 * @throws Exception
	 */
	private HttpRequestBase buildPermissionRequest(URI permissionSvcUri, PermissionEvaluateRequestBody evalReqBody, String method) throws Exception {
		switch (method) {
		case HttpGet.METHOD_NAME:
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("resource", evalReqBody.getResource()));
			params.add(new BasicNameValuePair("username", evalReqBody.getUsername()));
			params.add(new BasicNameValuePair("domain", evalReqBody.getDomain()));
			params.add(new BasicNameValuePair("action", evalReqBody.getAction()));
			if(evalReqBody instanceof PreparedPermissionEvaluateRequestBody && ((PreparedPermissionEvaluateRequestBody) evalReqBody).getUserRoles() != null) {
				for(String role : ((PreparedPermissionEvaluateRequestBody) evalReqBody).getUserRoles()) {
					params.add(new BasicNameValuePair("user_roles", role));
				}
			}
			HttpGet getRequest = HttpUtils.buildGet(permissionSvcUri.getScheme(), permissionSvcUri.getHost(), permissionSvcUri.getPort(), permissionSvcUri.getPath(), null, params);
			return getRequest;
		case HttpPost.METHOD_NAME:
			ObjectMapper mapper = new ObjectMapper();
			String requestBody = mapper.writeValueAsString(evalReqBody);
			StringEntity stringEntity = new StringEntity(requestBody);
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()));
			HttpPost postRequest = HttpUtils.buildPost(permissionSvcUri, null, stringEntity);
			return postRequest;
		default:
			throw new UnsupportedOperationException("Method " + method + " is not supported");
		}
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	private boolean validateDecisionRequestArguments(Object[] args) {
		if(args == null) {
			throw new IllegalStateException("Cannot read arguments");
		} 
		
		boolean isRequestValid = false; 
		
		if(args.length == 4) {
			isRequestValid = (args[0]==null || args[0] instanceof String) && (args[1]==null || args[1] instanceof String) && (args[2]==null || args[2] instanceof String) && (args[3]==null || args[3] instanceof String);
		} else if(args.length == 5) {
			isRequestValid = (args[0] == null || args[0] instanceof String)
					&& (args[1] == null || args[1] instanceof String) && (args[2] == null || args[2] instanceof String)
					&& (args[3] == null || args[3] instanceof String) && (args[4] == null || args[4] instanceof List);
		}
	
		return isRequestValid;
	}
	
}
