package com.meadowhawk.homepi.service.business;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.HomePiUserDAO;
import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.GoogleInfo;
import com.meadowhawk.homepi.util.service.AuthRequiredBeforeException;
import com.meadowhawk.homepi.util.service.MaskData;

/**
 * @author lee
 */
@Component
public class HomePiUserService {

	@Autowired
	HomePiUserDAO homePiUserDao;
	
	@Autowired
	DeviceManagementService managementService;

	@Autowired
	PiProfileDAO piProfileDAO;
	
	/**
	 * Retrieve user by email address.
	 * @param email
	 * @return
	 */
	public HomePiUser getUser(String email){
		return homePiUserDao.findByUserName(email);
	}


	/**
	 * Updates user data.
	 * @param hpUser
	 * @return
	 */
	public HomePiUser updateUserData(HomePiUser hpUser){
		hpUser.setUpdateTime(new DateTime());
		homePiUserDao.update(hpUser);
		
		return hpUser;
	}
	
	/**
	 * Retrieve existing user from Google Auth, if not found create new user.
	 * @param user - google auth user info
	 * @return homePi user
	 */
	public HomePiUser getUserFromGoogleAuth(GoogleInfo user) throws HomePiAppException {
		HomePiUser hUser =  null;
		try{
			hUser = homePiUserDao.findByEmail(user.getEmail());
			hUser.setGoogleAuthToken(user.getAuth_token());  //save new auth code.
			updateUserData(hUser);
			//TODO: Consider calling adaptor to update fields if diff from stored?
		} catch(NoResultException nre){
			hUser = UserAuthAdaptor.adaptGoogleInfo(user);
			homePiUserDao.save(hUser);
		}
		
		return hUser;
	}


	/**
	 * Retrieves User profile, only public data is displayed unless auth tokens match.
	 * @param userName
	 * @param authToken
	 * @return
	 */
	@MaskData
	public HomePiUser getUserData(String userName, String authToken) {
		HomePiUser hUser =  null;
		try{
			hUser = homePiUserDao.findByUserName(userName);

		} catch(NoResultException nre){
			throw new HomePiAppException(Status.NOT_FOUND,"Invalid user");
		}
		
		return hUser;
	}


	/**
	 * Update user data.  Not all values are editable!
	 * @param userName 
	 * @param authToken - auth token
	 * @param updateUser - user
	 * @return updated user if successful
	 */
	@AuthRequiredBeforeException
	@MaskData
	public HomePiUser updateUserData(String userName, String authToken, HomePiUser updateUser) {
		HomePiUser hUser = null;
			try {
				hUser = homePiUserDao.findByUserName(userName);
				hUser.setFamilyName(updateUser.getFamilyName());
				hUser.setGivenName(updateUser.getGivenName());
				hUser.setFullName(updateUser.getFullName());
				hUser.setPicLink(updateUser.getPicLink());
				hUser.setUserName(updateUser.getUserName());
				hUser.setUpdateTime(new DateTime());
				homePiUserDao.update(hUser);
				hUser = homePiUserDao.findByUserName(hUser.getUserName());
			} catch (NoResultException nre) {
				throw new HomePiAppException(Status.NOT_FOUND, "Invalid user");
			}

		return hUser;
	}
	
	
	/**
	 * Validates user token and user name.
	 * @param userName
	 * @param authToken
	 * @return - true if authorized.
	 */
	public boolean verifyUserToken(String userName, String authToken){
		if(StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(authToken) ){
			return false;
		}
		return homePiUserDao.authorizeToken(userName, authToken);
	}


	/**
	 * Wrapped method allows for Annotation and AOP support. Different rules apply to the managemnetService as it is a PI only service.
	 * @param userName
	 * @param authToken
	 * @param piSerialId
	 * @return requested profile.
	 */
	@MaskData
	public PiProfile getPiProfile(String userName, String authToken, String piSerialId) {
		PiProfile profile = managementService.getPiProfile(piSerialId);
		
		return profile;
	}


	/**
	 * Update PiProfile id user auth is valid.
	 * @param userName
	 * @param authToken
	 * @param piSerialId
	 * @param piProfile
	 * @return
	 */
	@AuthRequiredBeforeException
	public void updatePiProfile(String userName, String authToken, String piSerialId, PiProfile piProfile) {
		PiProfile profile = managementService.getPiProfile(piSerialId);
		//make updates
		//TODO: Add ProfileAdaptort for copying updatable values.
		profile.setIpAddress(piProfile.getIpAddress());
		profile.setName(piProfile.getName());
		profile.setSshPortNumber(piProfile.getSshPortNumber());
		
		managementService.updatePiProfile(profile);
	}


	/**
	 * @param userName
	 * @param authToken
	 * @param piSerialId
	 * @param appId
	 */
	@AuthRequiredBeforeException
	public void addAppToProfile(String userName, String authToken, String piSerialId, Long appId) {
		if(StringUtil.isNullOrEmpty(piSerialId) || appId == null){
			throw new HomePiAppException(Status.BAD_REQUEST,"Missing key values for request.");
		}
		HomePiUser user = this.getUserData(userName, authToken);
		PiProfile profile = managementService.getPiProfile(piSerialId);
		ManagedApp ma = null;
		
		for (ManagedApp mApp : user.getManagedApps()) {
			if(mApp.getAppId().equals(appId)){
				ma = mApp;
				break;
			}
		}
		
		if(ma == null){
			throw new HomePiAppException(Status.BAD_REQUEST,"Invlaid appi id.");
		}
		
		profile.getManagedApps().add(ma);
		piProfileDAO.update(profile);
	}


	/**
	 * @param userName
	 * @param authToken
	 * @param piSerialId
	 * @param appId
	 */
	@AuthRequiredBeforeException
	public void deleteAppToProfile(String userName, String authToken, String piSerialId, Long appId) {
		if(StringUtil.isNullOrEmpty(piSerialId) || appId == null){
			throw new HomePiAppException(Status.BAD_REQUEST,"Missing key values for request.");
		}
		
		PiProfile profile = managementService.getPiProfile(piSerialId);

		piProfileDAO.removeAppFromProfile(profile, appId);
	}
}
