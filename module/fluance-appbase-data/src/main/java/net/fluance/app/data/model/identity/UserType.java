package net.fluance.app.data.model.identity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserType {
	UNKNOWN(""), 
	USER("user"), 
	APPLICATION("application"),
	NAV_DISABLED("nav_disabled"),
	SHARED("shared");

	private String value;

	private UserType(String userType){
		this.value = userType;
	}

	public String getValue() {
		return value;
	}

	@JsonCreator
	public static UserType permissiveValueOf(String value){
		if (value == null){
			return UserType.UNKNOWN;
		} else {
			UserType[] userTypes = UserType.values();
			for (int i=0;i<userTypes.length;i++){
				if(userTypes[i].getValue().equalsIgnoreCase(value))
					return userTypes[i];
			}
			return UserType.UNKNOWN;
		}
	}
}
