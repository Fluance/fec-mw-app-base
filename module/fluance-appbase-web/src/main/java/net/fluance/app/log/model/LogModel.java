package net.fluance.app.log.model;

public class LogModel {

	private String userName;
	private String resourceId;
	private String httpMethod;
	private String parentPid;
	private String parentVisitNb;
	private String uri;
	private String parameters;
	private String resourceType;
	
	public LogModel(){}

	public LogModel(String userName, String resourceId, String httpMethod, String parentPid, String parentVisitNb, String uri, String parameters, String resourceType) {
		this.userName = userName;
		this.resourceId = resourceId;
		this.httpMethod = httpMethod;
		this.parentPid = parentPid;
		this.parentVisitNb = parentVisitNb;
		this.uri = uri;
		this.parameters = parameters;
		this.resourceType = resourceType;
	}

	public String getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getParentPid() {
		return parentPid;
	}

	public void setParentPid(String parentPid) {
		this.parentPid = parentPid;
	}

	public String getParentVisitNb() {
		return parentVisitNb;
	}

	public void setParentVisitNb(String parentVisitNb) {
		this.parentVisitNb = parentVisitNb;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}
