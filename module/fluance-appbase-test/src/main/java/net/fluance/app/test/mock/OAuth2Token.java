/**
 * 
 */
package net.fluance.app.test.mock;

import java.util.Date;

public class OAuth2Token {

	private Date dateIssued;
	private String accessToken;
	private String refreshToken;
	private String tokenType; 
	private int expiresIn;
	
	/**
	 * @param dateIssued
	 * @param accessToken
	 * @param refreshToken
	 * @param tokenType
	 * @param expiresIn
	 */
	public OAuth2Token(Date dateIssued, String accessToken, String refreshToken, String tokenType, int expiresIn) {
		super();
		this.dateIssued = dateIssued;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
	}
	
	/**
	 * @return the dateIssued
	 */
	public Date getDateIssued() {
		return dateIssued;
	}
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}
	
	/**
	 * @return the expiresIn
	 */
	public int getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param dateIssued the dateIssued to set
	 */
	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @param tokenType the tokenType to set
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"access_token\":\"" + accessToken + "\",\"refresh_token\":\"" + refreshToken + "\", \"token_type\":\"" + tokenType
				+ "\", \"expires_in\":" + expiresIn + "}";
	}
	
	

}
