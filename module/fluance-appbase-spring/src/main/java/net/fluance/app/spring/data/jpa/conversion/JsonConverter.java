/**
 * 
 */
package net.fluance.app.spring.data.jpa.conversion;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonConverter implements AttributeConverter<Object, String> {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Override
	public String convertToDatabaseColumn(Object meta) {
	    try {
	        return OBJECT_MAPPER.writeValueAsString(meta);
	      } catch (IOException ex) {
	        return null;
	        // or throw an error
	      }
	}

	@Override
	public Object convertToEntityAttribute(String dbData) {
		try {
		      return OBJECT_MAPPER.readValue(dbData, Object.class);
		    } catch (IOException ex) {
		      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
		      return null;
		    }
	}

}
