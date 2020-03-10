package net.fluance.app.data.criteria.sql;

import net.fluance.commons.lang.StringUtils;

public class PostgresqlCriteria extends SQL99Criteria {

	public PostgresqlCriteria() {
		super();
	}
	
	/**
	 * 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria containsCaseInsensitive(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " ILIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = "%" + value + "%";
		sqlStatement += (" " + columnName + " ILIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria startsWithCaseInsensitive(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " ILIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = value + "%";
		sqlStatement += (" " + columnName + " ILIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * 
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria endsWithCaseInsensitive(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " ILIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = "%" + value;
		sqlStatement += (" " + columnName + " ILIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	public SQL99Criteria equalsWithCaseInsensitive(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " ILIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = value;
		sqlStatement += (" " + columnName + " ILIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
}
