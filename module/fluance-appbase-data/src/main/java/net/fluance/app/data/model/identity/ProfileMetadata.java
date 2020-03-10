package net.fluance.app.data.model.identity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The persistent class for the company database table.
 * 
 */
@SuppressWarnings("serial")
public class ProfileMetadata implements Serializable{

	protected Integer profileId;
	private String gender;
	private String birthDate;
	private String title;
	private String speciality;
	private String googleToken;
	private String linkedInToken;
	private String email;
	private String externalPhoneNumberOne;
	private String externalPhoneNumbertwo;
	private String latitude;
	private String longitude;
	private String pictureUri;
	private Integer employeeClinicId;
	private String lastLocalizationAt;
	private String preferredPhoneNumber;
	private String supportContactName;
	private String supportContactPhoneNumber;
	private String lastActivityAt; 
	private String iban; 

	public ProfileMetadata() {}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getGoogleToken() {
		return googleToken;
	}

	public void setGoogleToken(String googleToken) {
		this.googleToken = googleToken;
	}

	public String getLinkedInToken() {
		return linkedInToken;
	}

	public void setLinkedInToken(String linkedInToken) {
		this.linkedInToken = linkedInToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExternalPhoneNumberOne() {
		return externalPhoneNumberOne;
	}

	public void setExternalPhoneNumberOne(String externalPhoneNumberOne) {
		this.externalPhoneNumberOne = externalPhoneNumberOne;
	}

	public String getExternalPhoneNumbertwo() {
		return externalPhoneNumbertwo;
	}

	public void setExternalPhoneNumbertwo(String externalPhoneNumbertwo) {
		this.externalPhoneNumbertwo = externalPhoneNumbertwo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public Integer getEmployeeClinicId() {
		return employeeClinicId;
	}

	public void setEmployeeClinicId(Integer employeeClinicId) {
		this.employeeClinicId = employeeClinicId;
	}

	public String getLastLocalizationAt() {
		return lastLocalizationAt;
	}

	public void setLastLocalizationAt(String lastLocalizationAt) {
		this.lastLocalizationAt = lastLocalizationAt;
	}

	public String getPreferredPhoneNumber() {
		return preferredPhoneNumber;
	}

	public void setPreferredPhoneNumber(String preferredPhoneNumber) {
		this.preferredPhoneNumber = preferredPhoneNumber;
	}

	public String getSupportContactName() {
		return supportContactName;
	}

	public void setSupportContactName(String supportContactName) {
		this.supportContactName = supportContactName;
	}

	public String getSupportContactPhoneNumber() {
		return supportContactPhoneNumber;
	}

	public void setSupportContactPhoneNumber(String supportContactPhoneNumber) {
		this.supportContactPhoneNumber = supportContactPhoneNumber;
	}

	public String getLastActivityAt() {
		return lastActivityAt;
	}

	public void setLastActivityAt(String lastActivityAt) {
		this.lastActivityAt = lastActivityAt;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
}