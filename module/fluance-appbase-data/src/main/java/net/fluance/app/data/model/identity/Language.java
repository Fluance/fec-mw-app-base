package net.fluance.app.data.model.identity;

public enum Language {
	UNKNOWN(""), FR("fr"), EN("en"), DE("de"), IT("it");

	private String value;

	private Language(String language){
		this.value = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	} 

	public static String permissiveValueOf(String value){
		try{
			if (value == null){
				throw new IllegalArgumentException();
			} else {
				Language language = valueOf(value.toUpperCase());
				if (language == null){
					return null;
				}
				return language.getValue();
			}
		}catch(IllegalArgumentException exc){
			return null;
		}
	}
}
