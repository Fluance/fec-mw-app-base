/**
 * 
 */
package net.fluance.app.data.criteria.sql;

import java.util.Date;

import net.fluance.commons.lang.StringUtils;

public class SQL99Criteria {
	
	protected String sqlStatement;
	// The number of expressions used after the WHERE or HAVING clause
	protected int filteringExpressions;
	
	public SQL99Criteria() {
		this.filteringExpressions = 0;
		this.sqlStatement = "";
	}

	/**
	 * Puts the WHERE keyword
	 * @return
	 */
	public SQL99Criteria where() {
		sqlStatement += SQLCriteriaClause.WHERE.getName(); 
		return this;
	}
	
	/**
	 * Allows to provide a custom expression to be evaluated
	 * @param custom
	 * @return
	 */
	public SQL99Criteria custom(String custom) {
		sqlStatement += (" " + custom); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Equal to
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria equals(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + "=" + value + "A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + "=" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + "=" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + "= CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + "=" + value); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Different from
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria notEquals(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + "<>" + value + "A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + "<>" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + "<>" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + "<> CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + "<>" + value); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Greater than
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria gt(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + "> " + value + ":A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + ">" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + ">" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + "> CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + ">" + value); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Less than
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria lt(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + "<" + value + "A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + "<" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + "<" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + "> CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + "<" + ((value instanceof String) ? StringUtils.simpleQuote((String) value) : value)); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Greater or equal
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria ge(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + ">=" + value + "A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + ">=" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + ">=" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + "> CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + ">=" + value); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Less or equal
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria le(String columnName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + "<=" + value + "A value must be provided to perform comparison");
		}
		if((value instanceof String)) {
			sqlStatement += (" " + columnName + "<=" + (StringUtils.simpleQuote((String) value))); 
		} else if(value instanceof Character) {
			sqlStatement += (" " + columnName + "<=" + (StringUtils.simpleQuote((Character) value))); 
		} else if(value instanceof Date) {
			sqlStatement += (" " + columnName + ">= CAST('" + value + "' AS DATE)"); 
		} else {
			sqlStatement += (" " + columnName + "<=" + ((value instanceof String) ? StringUtils.simpleQuote((String) value) : value)); 
		}
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * String contains other String
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria contains(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " LIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = "%" + value + "%";
		sqlStatement += (" " + columnName + " LIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * String starts with String
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria startsWith(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " LIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = value + "%";
		sqlStatement += (" " + columnName + " LIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * String ends with
	 * @param columnName
	 * @param value
	 * @return
	 */
	public SQL99Criteria endsWith(String columnName, String value) {
		if(value == null) {
			throw new IllegalArgumentException(columnName + " LIKE " + value + "A value must be provided to perform comparison");
		}
		String sqlValue = "%" + value;
		sqlStatement += (" " + columnName + " LIKE " + (StringUtils.simpleQuote(sqlValue))); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * Column value existing in set
	 * @param columnName
	 * @param values
	 * @return
	 */
	public SQL99Criteria in(String columnName, Object... values) {
		if(values == null) {
			throw new IllegalArgumentException("IN operator must be used with a set of values");
		}
		String set = "(";
		for(Object object : values) {
			set += ((object instanceof String) ? ("'" +object+"'") : object) + ",";
		}
		set = set.substring(0, set.lastIndexOf(","));
		set += ")";
		sqlStatement += (" " + columnName + " IN " + set); 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * AND logical operator
	 * @return
	 */
	public SQL99Criteria and() {
		sqlStatement += ((" ") + SQLCriteriaOperator.AND.getName()); 
		return this;
	}
	
	/**
	 * OR logical operator
	 * @return
	 */
	public SQL99Criteria or() {
		sqlStatement += ((" ") + SQLCriteriaOperator.OR.getName()); 
		return this;
	}
	
	/**
	 * NOT logical operator
	 * @param expressionOrColumnName
	 * @return
	 */
	public SQL99Criteria not(String expressionOrColumnName) {
		sqlStatement += " " + SQLCriteriaOperator.NOT.getName() + " (" + expressionOrColumnName + ")"; 
		filteringExpressions += 1;
		return this;
	}

	/**
	 * 
	 * @param expressionOrColumnName
	 * @return
	 */
	public SQL99Criteria isNull(String expressionOrColumnName) {
		sqlStatement += " (" + expressionOrColumnName + ") IS NULL" ; 
		filteringExpressions += 1;
		return this;
	}
	
	/**
	 * 
	 * @param expressionOrColumnName
	 * @return
	 */
	public SQL99Criteria isNotNull(String expressionOrColumnName) {
		sqlStatement += " (" + expressionOrColumnName + ") IS NOT NULL" ; 
		filteringExpressions += 1;
		return this;
	}

	/**
	 * @return the filteringExpressions
	 */
	public int getFilteringExpressions() {
		return filteringExpressions;
	}

	/**
	 * @param filteringExpressions the filteringExpressions to set
	 */
	public void setFilteringExpressions(int filteringExpressions) {
		this.filteringExpressions = filteringExpressions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sqlStatement;
	}

}
