package com.meadowhawk.homepi.service.business;

import javax.persistence.NoResultException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.HomePiUserDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
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
	
}