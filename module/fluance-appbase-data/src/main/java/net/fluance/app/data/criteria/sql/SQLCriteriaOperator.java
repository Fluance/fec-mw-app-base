/**
 * 
 */
package net.fluance.app.data.criteria.sql;

public enum SQLCriteriaOperator {

	AND("AND"),
	OR("OR"),
	NOT("NOT");
	
	private String name;
	
	private SQLCriteriaOperator(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
