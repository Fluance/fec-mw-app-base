package net.fluance.app.web.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class FluanceCustomWrapper {

	private Map<String, Object> json = new HashMap<>();
	
	public FluanceCustomWrapper(){}
	
	@JsonAnyGetter
    public Map<String, Object> any() {
        return json;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        json.put(name, value);
    }
    
    @SuppressWarnings("rawtypes")
	public void wrapListEachElement(List list, String topRootName, String elementRootName){
		List<Map<String, Object>> listWrapper = new ArrayList<>();
		for (Object obj : list){
			Map<String, Object> map  = new HashMap<>();
			map.put(elementRootName, obj);
			listWrapper.add(map);
		}
		this.set(topRootName, listWrapper);
    }
    
    @SuppressWarnings("rawtypes")
	public void wrap(Object pojo, String topRootName, String secondRootElement){
    	Map<String, Object> wrappedObject = new HashMap<>();

    	if (pojo instanceof List && pojo != null && ((List) pojo).size() == 1){
    		// adapte json build with the Wso2 Dss logic for single element Lists
    		wrappedObject.put(secondRootElement, ((List) pojo).get(0));
    	} else {
    		wrappedObject.put(secondRootElement, pojo);
    	}
		this.set(topRootName, wrappedObject);
    }
    
    public void wrapPojo(Object pojo, String topRootName){
		this.set(topRootName, pojo);
    }
}
