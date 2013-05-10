package com.meadowhawk.homepi.service.business;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.HomePiUserDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.GoogleInfo;

/**
 * @author lee
 */
@Component
public class HomePiUserService {

	@Autowired
	HomePiUserDAO homePiUserDao;
	
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
	public HomePiUser getUserData(String userName, String authToken) {
		HomePiUser hUser =  null;
		try{
			hUser = homePiUserDao.findByUserName(userName);
			
			if(StringUtil.isNullOrEmpty(authToken) || !hUser.getGoogleAuthToken().equals(authToken)){
				hUser.setPrivateVersion(true);
			} 
		} catch(NoResultException nre){
			throw new HomePiAppException(Status.NOT_FOUND,"Invalid user");
		}
		
		return hUser;
	}


	/**
	 * Update user data.  Not all values are editable!
	 * @param userName 
	 * @param updateUser - user
	 * @param authToken - auth token
	 * @return updated user if successful
	 */
	public HomePiUser updateUserData(String userName, HomePiUser updateUser, String authToken) {
		if (StringUtil.isNullOrEmpty(authToken)) {
			throw new HomePiAppException(Status.FORBIDDEN);
		}
		HomePiUser hUser = null;
		if (homePiUserDao.authorizeToken(userName, authToken)) {
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
		} else {
			throw new HomePiAppException(Status.FORBIDDEN, "Permission denied");
		}

		return hUser;
	}
	
}
