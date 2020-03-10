package net.fluance.app.log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.model.LogModel;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.commons.lang.FluancePrintingMap;

public abstract class LogCreator {

	protected Logger logger = LogManager.getLogger();
	
	protected ResourceType resourceType;
	protected Object payload;
	protected FluancePrintingMap<String, String[]> params;
	protected User user;
	protected String httpMethod;
	protected String uri;

	@Autowired
	protected UserProfileLoader profileLoader;
	
	public abstract LogModel getLogModel();
	
	protected String getUsername(User user){
		if (user.isPatient()){
			return user.getEvitaUserName();
		} else {
			return user.getUsername();
		}
	}

	protected String getDomain(User user){
		if (user.isPatient()){
			return user.getUsername(); 
		} else {
			return user.getDomain();
		}
	}

    public void init(){
		try {
			User user = profileLoader.getUserWithInfos(this.user);
			//Add third party user information
			user.setThirdPartyUser(this.user.getThirdPartyUser());
			
			this.user = user;
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | HttpException | IOException | URISyntaxException e) {
			logger.warn("Unable to get user info details", e);
		}
    }
}
