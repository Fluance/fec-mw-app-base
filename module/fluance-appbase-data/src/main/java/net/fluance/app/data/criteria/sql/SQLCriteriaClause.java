/**
 * 
 */
package net.fluance.app.data.criteria.sql;

public enum SQLCriteriaClause {

	WHERE("WHERE"),
	HAVING("HAVING");
	
	private String name;
	
	private SQLCriteriaClause(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
