/**
 * 
 */
package net.fluance.app.web.util.swagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;
import net.fluance.commons.json.JsonUtils;

public class SwaggerSpecUtils {

	private static Logger LOGGER = LogManager.getLogger(SwaggerSpecUtils.class);

	/**
	 * 
	 * @param specLocation
	 * @return
	 */
	public static Swagger load(String specLocation) {
		Swagger swagger = new SwaggerParser().read(specLocation);
		return swagger;
	}

	/**
	 * 
	 * @param specLocation
	 * @param apiPath
	 * @return
	 */
	public static Path getPath(String specLocation, String apiPath) {
		Swagger swagger = load(specLocation);
		Path path = (swagger != null) ? swagger.getPath(apiPath) : null;
		return path;
	}

	/**
	 * 
	 * @param swagger
	 * @param apiPath
	 * @return
	 */
	public static Path getPath(Swagger swagger, String apiPath) {
		Path path = (swagger != null) ? swagger.getPath(apiPath) : null;
		return path;
	}

	/**
	 * Get an operation from a swagger specification, given the API path and the HTTP method. 
	 * The swagger specification is loaded from the given location
	 * @param specLocation
	 * @param apiPath
	 * @param method
	 * @return
	 */
	public static Operation getOperation(String specLocation, String apiPath, HttpMethod method) {
		Path path = getPath(specLocation, apiPath);
		Operation operation = (path != null) ? pathOperation(path, method) : null;
		return operation;
	}
	
	/**
	 * Get an operation from a swagger specification, given the API path and the HTTP method. 
	 * @param specLocation
	 * @param apiPath
	 * @param method
	 * @return
	 */
	public static Operation getOperation(Swagger swagger, String apiPath, HttpMethod method) {
		Path path = getPath(swagger, apiPath);
		Operation operation = (path != null) ? pathOperation(path, method) : null;
		return operation;
	}

	/**
	 * 
	 * @param specLocation
	 * @param apiPath
	 * @param method
	 * @param responseStatus
	 * @return
	 */
	public static Response getOperationResponse(String specLocation, String apiPath, HttpMethod method,
			HttpStatus responseStatus) {
		Operation operation = getOperation(specLocation, apiPath, method);
		Response response = (operation != null) ? operation.getResponses().get(Integer.toString(responseStatus.value()))
				: null;
		return response;
	}

	/**
	 * 
	 * @param swagger
	 * @param apiPath
	 * @param method
	 * @param responseStatus
	 * @return
	 */
	public static Response getOperationResponse(Swagger swagger, String apiPath, HttpMethod method,
			HttpStatus responseStatus) {
		Operation operation = getOperation(swagger, apiPath, method);
		Response response = (operation != null) ? operation.getResponses().get(Integer.toString(responseStatus.value()))
				: null;
		return response;
	}

	/**
	 * 
	 * @param specLocation
	 * @param apiPath
	 * @param method
	 * @param responseStatus
	 * @return
	 */
	public static Property getOperationResponseSchema(String specLocation, String apiPath, HttpMethod method,
			HttpStatus responseStatus) {
		Response response = getOperationResponse(specLocation, apiPath, method, responseStatus);
		Property responseSchema = (response != null) ? response.getSchema() : null;
		return responseSchema;
	}

	/**
	 * 
	 * @param specLocation
	 * @param apiPath
	 * @param method
	 * @param responseStatus
	 * @return
	 */
	public static String getOperationResponseSchemaText(String specLocation, String apiPath, HttpMethod method,
			HttpStatus responseStatus) {
		Property p = getOperationResponseSchema(specLocation, apiPath, method, responseStatus);
		Swagger swagger = load(specLocation);
		return SwaggerSpecUtils.getSchemaText(swagger, p);
	}
	
	/**
	 * 
	 * @param swagger
	 * @param apiPath
	 * @param method
	 * @param responseStatus
	 * @return
	 */
	public static Property getOperationResponseSchema(Swagger swagger, String apiPath, HttpMethod method,
			HttpStatus responseStatus) {
		Response response = getOperationResponse(swagger, apiPath, method, responseStatus);
		Property responseSchema = (response != null) ? response.getSchema() : null;
		return responseSchema;
	}

	public static List<SecuritySchemeDefinition> getAuthorizationRequirement(String specLocation, String path,
			HttpMethod method) {
		Swagger swagger = SwaggerSpecUtils.load(specLocation);
		Map<String, SecuritySchemeDefinition> securitySchemeDefinitions = swagger.getSecurityDefinitions();
		Set<String> definitionKeys = securitySchemeDefinitions.keySet();
		Iterator<String> secDefIterator = definitionKeys.iterator();
		List<SecuritySchemeDefinition> securityDefinitions = new ArrayList<>();
		while (secDefIterator.hasNext()) {
			String currentKey = secDefIterator.next();
			SecuritySchemeDefinition currentDefinition = securitySchemeDefinitions.get(currentKey);
			securityDefinitions.add(currentDefinition);
		}

		return securityDefinitions;

	}

	/**
	 * 
	 * @param specLocation
	 * @param path
	 * @param method
	 * @return
	 */
	public static List<SecuritySchemeDefinition> getAuthorizationRequirement(String specLocation, String path,
			String method) {
		return getAuthorizationRequirement(specLocation, path, HttpMethod.valueOf(method));
	}

	/**
	 * 
	 * @param specLocation
	 * @param path
	 * @param method
	 * @param status
	 * @return
	 */
	public static JSONObject getOperationResponseJsonSchema(String specLocation, String path, HttpMethod method,
			HttpStatus status) {
		Swagger swagger = SwaggerSpecUtils.load(specLocation);
		Property responseProp = SwaggerSpecUtils.getOperationResponseSchema(swagger, path, method, status);
		JSONObject responseSchema = (responseProp instanceof RefProperty)
				? SwaggerSpecUtils.toJsonSchema(swagger, (RefProperty) responseProp)
				: SwaggerSpecUtils.toJsonSchema(responseProp);
		return responseSchema;
	}

	/**
	 * 
	 * @param path
	 * @param method
	 * @return
	 */
	public static Operation pathOperation(Path path, final HttpMethod method) {
		Operation operation = null;
		switch (method) {
		case POST:
			operation = path.getPost();
			break;
		case GET:
			operation = path.getGet();
			break;
		case PUT:
			operation = path.getPut();
			break;
		case DELETE:
			operation = path.getDelete();
			break;
		case OPTIONS:
			operation = path.getOptions();
			break;
		case HEAD:
			operation = path.getHead();
			break;
		case PATCH:
			operation = path.getPatch();
			break;
		default:
			break;
		}
		return operation;
	}

	/**
	 * 
	 * @param property
	 * @return
	 */
	public static JSONObject toJsonSchema(Property property) {
		JSONObject jsonObject = new JSONObject();

		if (property.getReadOnly() != null) {
			jsonObject.put("readOnly", property.getReadOnly());
		}
		if (property.getAccess() != null) {
			jsonObject.put("access", property.getAccess());
		}
		if (property.getDescription() != null) {
			jsonObject.put("description", property.getDescription());
		}
		if (property.getType() != null) {
			jsonObject.put("type", property.getType());
		}
		if (property.getTitle() != null) {
			jsonObject.put("title", property.getTitle());
		}
		if (property.getPosition() != null) {
			jsonObject.put("position", property.getPosition());
		}
		if (property.getName() != null) {
			jsonObject.put("name", property.getName());
		}
		if (property.getFormat() != null) {
			jsonObject.put("format", property.getFormat());
		}
		if (property.getExample() != null) {
			jsonObject.put("example", property.getExample());
		}
		if (property.getXml() != null) {
			jsonObject.put("xml", property.getXml());
		}

		if (property instanceof ObjectProperty) {
			Map<String, Property> properties = ((ObjectProperty) property).getProperties();
			for (Property prop : properties.values()) {
				if (prop.getName() != null) {
					jsonObject.append(prop.getName(), toJsonSchema(prop));
				}
			}
		}

		return jsonObject;
	}

	public static JSONObject toJsonSchema(Property property, Model parent) {
		JSONObject jsonObject = new JSONObject();

		if (property.getReadOnly() != null) {
			jsonObject.put("readOnly", property.getReadOnly());
		}
		if (property.getDescription() != null) {
			jsonObject.put("description", property.getDescription());
		}
		if (property.getType() != null) {
			jsonObject.put("type", property.getType());
		}
		if (property.getTitle() != null) {
			jsonObject.put("title", property.getTitle());
		}
		if (property.getPosition() != null) {
			jsonObject.put("position", property.getPosition());
		}
		if (property.getName() != null) {
			jsonObject.put("name", property.getName());
		}
		if (property.getFormat() != null) {
			jsonObject.put("format", property.getFormat());
		}
		if (property.getExample() != null) {
			jsonObject.put("example", property.getExample());
		}
		if (property.getXml() != null) {
			jsonObject.put("xml", property.getXml());
		}

		return jsonObject;
	}

	/**
	 * Assumes the reference definition is in the same document
	 * 
	 * @param swagger
	 * @param property
	 * @return
	 */
	public static JSONObject toJsonSchema(Swagger swagger, RefProperty refProperty) {
		String refName = ((RefProperty) refProperty).getSimpleRef();
		Model model = swagger.getDefinitions().get(refName);
		if (model instanceof ModelImpl) {
			JSONObject jsonModel = toJsonSchema(swagger, (ModelImpl) model);
			return jsonModel;
		} else {
			return null;
		}
	}

	public static JSONObject toJsonSchema(Swagger swagger, ModelImpl model) {

		JSONObject jsonModel = new JSONObject();

		if (model.isSimple()) {
			jsonModel.put("isSimple", model.isSimple());
		}
		if (model.getDescription() != null) {
			jsonModel.put("description", model.getDescription());
		}
		if (model.getType() != null) {
			jsonModel.put("type", model.getType());
		} else {
			jsonModel.put("type", "object");
		}
		if (model.getTitle() != null) {
			jsonModel.put("title", model.getTitle());
		}
		if (model.getName() != null) {
			jsonModel.put("name", model.getName());
		}

		Map<String, Property> properties = model.getProperties();
		Set<String> propertiesKeySet = properties.keySet();
		for (String propertyKey : propertiesKeySet) {
			Property property = properties.get(propertyKey);
			JSONObject propertyJson = new JSONObject();
			if (property instanceof RefProperty) {
				propertyJson = toJsonSchema(swagger, (RefProperty) property);
			} else if (property instanceof Property) {
				propertyJson = toJsonSchema(property);
			} else if (property instanceof ModelImpl) {
				propertyJson = toJsonSchema(swagger, (RefProperty) property);
			}
			if (!jsonModel.has("properties") || (!(jsonModel.get("properties") instanceof JSONObject))) {
				JSONObject jsonProperties = new JSONObject();
				jsonModel.put("properties", jsonProperties);
			}
			((JSONObject) jsonModel.get("properties")).put(propertyKey, propertyJson);
			if (property.getRequired()) {
				if (!jsonModel.has("required") || (!(jsonModel.get("required") instanceof JSONArray))) {
					JSONArray required = new JSONArray();
					jsonModel.append("required", required);
				}
				jsonModel.getJSONArray("required").put(propertyKey);
			}
		}

		return jsonModel;
	}

	/**
	 * Helper Methode who checks if the headers defined in the Swagger Yaml
	 * exists in a CloseableHttpResponse and are as expected
	 * 
	 * @param response
	 *            CloseableHttpResponse received
	 * @param yaml
	 *            Swagger spec
	 * @param pathApi
	 *            path api called
	 * @param httpStatus
	 * @return
	 */
	public static boolean areHeadersValid(CloseableHttpResponse response, Swagger yaml, String pathApi,
			HttpStatus httpStatus) {
		LOGGER.debug("Checking headers...");
		Path debug = yaml.getPath(pathApi);
		Map<String, Property> expectedResponseHeaders = debug.getGet().getResponses().get(httpStatus.value() + "").getHeaders();
		if (expectedResponseHeaders != null){
			for (String key : expectedResponseHeaders.keySet()) {
				LOGGER.debug("Expected Header : " + key);
				if (response.getHeaders(key) == null || response.getHeaders(key).length == 0
						|| !(response.getHeaders(key)[0] instanceof Header)) {
					LOGGER.error("Header not found as expcted");
					return false;
				} else {
					LOGGER.debug("Header found in response : " + ((Header) response.getHeaders(key)[0]).getName());
				}
			}
		}
		LOGGER.debug("Headers OK");
		return true;
	}

	public static boolean areHeadersValid(CloseableHttpResponse response, Swagger yaml, String pathApi, String httpMethod,
			HttpStatus httpStatus) {
		LOGGER.debug("Checking headers...");
		Operation operation = getOperation(yaml, pathApi, HttpMethod.valueOf(httpMethod));
		if(operation != null) {
			Map<String, Property> expectedResponseHeaders = operation.getResponses().get(httpStatus.value() + "")
					.getHeaders();
			for (String key : expectedResponseHeaders.keySet()) {
				LOGGER.debug("Expected Header : " + key);
				if (response.getHeaders(key) == null || response.getHeaders(key).length == 0
						|| !(response.getHeaders(key)[0] instanceof Header)) {
					LOGGER.error("Header not found as expcted");
					return false;
				} else {
					LOGGER.debug("Header found in response : " + ((Header) response.getHeaders(key)[0]).getName());
				}
			}
		}
		LOGGER.debug("Headers OK");
		return true;
	}
	
	/**
	 * Gets the reponse schema of the json expected, Can Recursive in case of an
	 * ArrayModel
	 * 
	 * @param swagger
	 * @param p
	 *            Property
	 * @return swagger reponse schema
	 */
	public static String getSchemaText(Swagger swagger, Property p) {
		if (p instanceof RefProperty) {
			String definitionName = ((RefProperty) p).getSimpleRef();
			Model m = swagger.getDefinitions().get(definitionName);
			if (m instanceof ArrayModel) {
				Property pp = ((ArrayModel) m).getItems();
				return getSchemaText(swagger, pp);
			} else {
				return toJsonSchema(swagger, (ModelImpl) m).toString();
			}
		} else if (p instanceof ArrayProperty){
			Property pp = ((ArrayProperty) p).getItems();
			return getSchemaText(swagger, pp);
		} else if(p instanceof ObjectProperty) {
			JSONObject jsonObject = toJsonSchema(p);
			return jsonObject.toString();
		}
		return "";
	}

	/**
	 * Get schema as text, if the property is ArrayProperty
	 * @param swagger
	 * @param p
	 * @return
	 */
	public static String getSchemaText(Swagger swagger, ArrayProperty p) {
		JSONArray jsonArray = new JSONArray();
		Property items = p.getItems();
		if (items instanceof ArrayProperty) {
			jsonArray.put(new JSONArray(getSchemaText(swagger, (ArrayProperty) items)));
		} else if (items instanceof RefProperty) {
			jsonArray.put(new JSONObject(getSchemaText(swagger, items)));
		} else {
			jsonArray.put(new JSONObject(toJsonSchema(items)));
		}
		return jsonArray.toString();
	}

	/**
	 * Processes an array property to return a JSON representation
	 * @param arrayProp the Swagger array property
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws JsonProcessingException 
	 */
	public static JSONArray toJsonSchema(Swagger swagger, ArrayProperty arrayProp) throws JsonProcessingException, JSONException, IOException {
		JSONArray jsonArray = new JSONArray();

		Property items = arrayProp.getItems();
		
		if(items instanceof RefProperty) {
			String schemaText = getSchemaText(swagger, items);
			if(JsonUtils.isArray(schemaText)) {
				jsonArray.put(new JSONArray(schemaText));
			} else if(JsonUtils.isObject(schemaText)) {
				jsonArray.put(new JSONObject(schemaText));
			}
		} else if(items instanceof ArrayProperty) {
			jsonArray.put(toJsonSchema(items));
		} else {
			jsonArray.put(toJsonSchema(items));
		}
		
		return jsonArray;
	}

}
