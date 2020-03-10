/**
 * 
 */
package net.fluance.app.test.mock;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScimProfile {

	@JsonProperty("Resources")
	private List<Resource> resources = new ArrayList<>();

	/**
	 * @return the resources
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
}
