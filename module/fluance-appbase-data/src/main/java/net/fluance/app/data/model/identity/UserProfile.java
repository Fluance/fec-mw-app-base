package net.fluance.app.data.model.identity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProfile extends User {
	
	private static final long serialVersionUID = 1L;

	public static String USER_PROFILE_KEY = "USER_PROFILE_KEY";
	
	private EhProfile profile;
	
	private ProfileMetadata profileMetadata;
	
	public UserProfile(){}
	
	public UserProfile(EhProfile profile, User user){
		super(user.getUsername(), user.getDomain(), user.getAccessToken(), user.getPid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getEvitaUserName(), user.getEvitaRole());
		this.profile = profile;
		this.addresses = user.getAddresses();
		this.telephons = user.telephons;
		this.emails = user.emails;
		this.company = user.getCompany();
		this.title = user.getTitle();
		this.isManager = user.getIsManager();
		this.managerUsername = user.getManagerUsername();
		this.manager = user.getManager();
		this.department = user.getDepartment();
	}

	public UserProfile(EhProfile profile, User user, ProfileMetadata profileMetadata){
		this(profile, user);
		this.profileMetadata = profileMetadata;
	}

	public EhProfile getProfile() {
		return profile;
	}

	public void setProfile(EhProfile profile) {
		this.profile = profile;
	}
	
	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public String getAccessToken(){
		return null;
	}

	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public String getIssuer(){
		return null;
	}

	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public Long getPid(){
		return null;
	}

	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public String getEvitaUserName(){
		return null;
	}

	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public String getEvitaRole(){
		return null;
	}
	
	/**
	 * Override the method of the parent class just to ignore the attribute 
	 */
	@Override
	@JsonIgnore
	public String getEmail(){
		return null;
	}
	
	public ProfileMetadata getProfileMetadata() {
		return profileMetadata;
	}

	public void setProfileMetadata(ProfileMetadata profileMetadata) {
		this.profileMetadata = profileMetadata;
	}

}
