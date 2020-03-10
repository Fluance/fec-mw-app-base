package net.fluance.app.data.model.identity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserReference implements Serializable{

	String username;
	String firstName;
	String lastName;
	String email;

	public UserReference(){
		super();
	}
	
	public UserReference(String username, String firstName, String lastName) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode userInfos = mapper.createObjectNode();
		if(this.getUsername() !=null ){
			userInfos.put("username", this.getUsername());
		}
		if(this.getFirstName() !=null ){
			userInfos.put("firstName", this.getFirstName());
		}
		if(this.getLastName() !=null ){
			userInfos.put("lastName", this.getLastName());
		}
		if(this.getEmail() !=null ){
			userInfos.put("email", this.getEmail());
		}
		return userInfos.toString();
	}
}
