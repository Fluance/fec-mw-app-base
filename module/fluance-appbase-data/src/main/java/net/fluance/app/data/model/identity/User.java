/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends UserIdentity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String USER_KEY = "USER_KEY";

	protected String firstName;
	protected String lastName;
	protected String email;
	protected String partnerUserName;
	protected String partnerRole;
	protected List<Email> emails;
	protected List<Telephon> telephons;
	protected List<Address> addresses;
	protected String company;
	protected String title;
	@JsonProperty("isManager")
	protected boolean isManager;
	@JsonProperty("manager")
	protected UserReference manager;
	protected String managerUsername;
	@JsonProperty("thirdPartyUser")
	protected ThirdPartyUserReference thirdPartyUser;
	protected String department;

	public User() {
		super();
	}
	
	public User(String username, String domain, String oAuth2AccessToken, Long pid) {
		super(username, domain, oAuth2AccessToken, pid);
	}

	public User(String username, String domain, String oAuth2AccessToken, Long pid, String firstName, String lastName, String email, String evitaUserName, String evitaRole) {
		super(username, domain, oAuth2AccessToken, pid);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.partnerUserName = evitaUserName;
		this.partnerRole = evitaRole;
	}
	
	@Override
	public String toString(){

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode userInfos = mapper.createObjectNode();
		if(!(this.getEmail()==null)){
			userInfos.put("email", this.getEmail());
		}
		if(!(this.getEvitaRole()==null)){
			userInfos.put("evitaRole", this.getEvitaRole());
		}
		if(!(this.getEvitaUserName()==null)){
			userInfos.put("evitaUserName", this.getEvitaUserName());
		}
		if(!(this.getFirstName()==null)){
			userInfos.put("firstName", this.getFirstName());
		}
		if(!(this.getLastName()==null)){
			userInfos.put("lastName", this.getLastName());
		}
		if(!(this.getAddresses()==null ||this.getAddresses().isEmpty())){
			ArrayNode addressFields = mapper.createArrayNode();
			for (Address address : this.getAddresses()) { 
				ObjectNode addressNode = mapper.createObjectNode();
				addressNode.put("line", address.getAddressLine());
				addressNode.put("country", address.getCountry());
				addressNode.put("locality", address.getLocality());
				addressNode.put("postalCode", address.getPostalCode());
				addressNode.put("state", address.getState());
				addressFields.add(addressNode);
			}
			userInfos.set("addresses", addressFields);
		}
		if(!(this.getEmails()==null|| this.getEmails().isEmpty())){
			ArrayNode emailFields = mapper.createArrayNode();
			for (Email email : this.getEmails()) { 
				ObjectNode emailNode = mapper.createObjectNode();
				emailNode.put(email.getType(), email.getValue());
				emailFields.add(emailNode);
			}
			userInfos.set("emails", emailFields);
		}
		if(!(this.getTelephons()==null || this.getTelephons().isEmpty()) ){
			ArrayNode telephonFields = mapper.createArrayNode();
			for (Telephon telephon : this.getTelephons()) { 
				ObjectNode telephonNode = mapper.createObjectNode();
				telephonNode.put(telephon.getType(), telephon.getValue());
				telephonFields.add(telephonNode);
			}
			userInfos.set("telephons", telephonFields);
		}
		if(!(this.getIssuer()==null)){
			userInfos.put("issuer", this.getIssuer());
		}
		if(!(this.getCompany()==null)){
			userInfos.put("company", this.getCompany());
		}
		if(!(this.getTitle()==null)){
			userInfos.put("title", this.getTitle());
		}
		userInfos.put("isManager", this.getIsManager());
		if(this.getManager() != null){
			userInfos.put("manager", this.getManager().toString());
		}
		if(this.getManagerUsername() != null){
			userInfos.put("managerUsername", this.getManagerUsername());
		}
		return userInfos.toString();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEvitaUserName() {
		return partnerUserName;
	}

	public void setEvitaUserName(String evitaUserName) {
		this.partnerUserName = evitaUserName;
	}

	public String getEvitaRole() {
		return partnerRole;
	}

	public void setEvitaRole(String evitaRole) {
		this.partnerRole = evitaRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@JsonIgnore
	public boolean isPatient(){
		return this.pid != null && this.pid > 0;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<Telephon> getTelephons() {
		return telephons;
	}

	public void setTelephons(List<Telephon> telephons) {
		this.telephons = telephons;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@JsonIgnore
	public String toJsonString() throws JsonProcessingException {
		UserClean clean = new UserClean(this);
		return new ObjectMapper().writeValueAsString(clean);
	}
	
	/**
	 * This Internal Class is used only to customise the User Object to Save with some @JsonIgnore properties ...
	 * @author bbenaidja
	 *
	 */
	public class UserClean extends UserProfile{

		private static final long serialVersionUID = 1L;

		@JsonIgnore
		public EhProfile getProfile() {
			return null;
		}
		@JsonIgnore
		public String getUsername(){
			return null;
		}
		@JsonIgnore
		public String getDomain(){
			return null;
		}
		
		public UserClean(User user){
			super(null, user);
		}
	}
	
	/**
	 * Add the user identity informations to this object
	 * @param user : the user object with the user identity informations.
	 */
	public void setUserIdentityFromUser(User user) {
		this.setAccessToken(user.getAccessToken());
		this.setUsername(user.getUsername());
		this.setDomain(user.getDomain());
		this.setPid(user.getPid());
		this.setIssuer(user.getIssuer());
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(boolean isManager) {
		this.isManager = isManager;
	}
	
	public UserReference getManager() {
		return manager;
	}
	
	public void setManager(UserReference manager) {
		this.manager = manager;
	}
	
	public String getManagerUsername() {
		return managerUsername;
	}
	
	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}
	
	public ThirdPartyUserReference getThirdPartyUser() {
		return thirdPartyUser;
	}

	public void setThirdPartyUser(ThirdPartyUserReference thirdPartyUser) {
		this.thirdPartyUser = thirdPartyUser;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
}
