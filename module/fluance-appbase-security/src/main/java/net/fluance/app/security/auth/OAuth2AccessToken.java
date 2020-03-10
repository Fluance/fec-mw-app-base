/**
 * 
 */
package net.fluance.app.security.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2AccessToken implements Serializable {

	@JsonProperty("accessToken")
	private String accessToken;
	@JsonProperty("tokenType")
	private String tokenType; 
	@JsonProperty("refreshToken")
	private String refreshToken;
	@JsonProperty("expirationDate")
	private long expirationDate;

	public OAuth2AccessToken() {}
	
	/**
	 * @param accessToken
	 * @param tokenType
	 * @param refreshToken
	 * @param expirationDate
	 * @param jwt
	 */
	public OAuth2AccessToken(String accessToken, String tokenType, String refreshToken, long expirationDate) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.refreshToken = refreshToken;
		this.expirationDate = expirationDate;
	}

	public OAuth2AccessToken(String accessToken, String tokenType) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * @param tokenType the tokenType to set
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @return the expiresIn
	 */
	public long getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public void setExpirationDate(long expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result + (int) (expirationDate ^ (expirationDate >>> 32));
		result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
		result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OAuth2AccessToken)) {
			return false;
		}
		OAuth2AccessToken other = (OAuth2AccessToken) obj;
		if (accessToken == null) {
			if (other.accessToken != null) {
				return false;
			}
		} else if (!accessToken.equals(other.accessToken)) {
			return false;
		}
		if (expirationDate != other.expirationDate) {
			return false;
		}
		if (refreshToken == null) {
			if (other.refreshToken != null) {
				return false;
			}
		} else if (!refreshToken.equals(other.refreshToken)) {
			return false;
		}
		if (tokenType == null) {
			if (other.tokenType != null) {
				return false;
			}
		} else if (!tokenType.equals(other.tokenType)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{"
					+ "\"accessToken\":\"" + accessToken + "\","
					+ "\"refreshToken\":\"" + refreshToken + "\","
					+ "\"tokenType\":\"" + tokenType + "\","
					+ "\"expirationDate\":" + expirationDate
					+ "}";
	}

	
}
