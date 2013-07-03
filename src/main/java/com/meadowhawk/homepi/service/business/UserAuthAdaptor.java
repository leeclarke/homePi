package com.meadowhawk.homepi.service.business;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.GoogleInfo;

/**
 * Adapts the Google returned data to the HomePi User data object while enforcing a few data rules.
 * @author lee
 */
public class UserAuthAdaptor {

	private static final String AT = "@";
	private static final String SPACE = " ";
	private static final String UNDERSCORE = "_";

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
				//No spaces in user name, this is used as a key for profile retrieval and life is just easier if there is NO spaces in the name. Translate to underscores!
				adaptedUser.setUserName(user.getName().replace(SPACE,UNDERSCORE));
			} else{
				adaptedUser.setUserName(user.getEmail().substring(0, user.getEmail().indexOf(AT)));
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
