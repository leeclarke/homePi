package com.meadowhawk.homepi.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.model.PiProfile;

@Component
public class ManagementService {

	@Autowired
	PiProfileDAO piProfileDao;
	
	public PiProfile getPiProfile(String serialId) {
		return piProfileDao.getPiProfile(serialId);
	}
	
}
