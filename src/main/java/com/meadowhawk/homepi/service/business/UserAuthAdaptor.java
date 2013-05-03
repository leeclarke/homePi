package com.meadowhawk.homepi.service.business;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.GoogleInfo;

/**
 * @author lee
 */
public class UserAuthAdaptor {

	/**
	 * @param user
	 * @return
	 */
	public static HomePiUser adaptGoogleInfo(GoogleInfo user) throws HomePiAppException{
		HomePiUser adaptedUser = null;
		if (user != null) {
			adaptedUser = new HomePiUser();
			adaptedUser.setEmail(user.getEmail());
			
			// set default user name.
			if (!StringUtil.isNullOrEmpty(user.getName())) {
				adaptedUser.setUserName(user.getName());
			} else{
				adaptedUser.setUserName(user.getEmail());
			}
			adaptedUser.setGoogleAuthToken(StringUtil.assureLength(user.getAuth_token(), 255));
			adaptedUser.setLocale(StringUtil.assureLength(user.getLocale(),100));
			adaptedUser.setPicLink(StringUtil.assureLength(user.getPicture(),255));
			adaptedUser.setGivenName(StringUtil.assureLength(user.getGiven_name(),255));
			adaptedUser.setFamilyName(StringUtil.assureLength(user.getFamily_name(),255));
			adaptedUser.setFullName(StringUtil.assureLength(user.getName(),510));
		}
		return adaptedUser;
	}
}
