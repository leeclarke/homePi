package com.meadowhawk.homepi.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.HomePiUserDAO;
import com.meadowhawk.homepi.model.HomePiUser;

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
	
}
