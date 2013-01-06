package com.meadowhawk.homepi.service.business;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.PiProfile;

@Component
public class ManagementService {

	public PiProfile getPiProfile(String serialId) {
		
		PiProfile prof = new PiProfile();
		prof.setIpAddress("123.1.1.2");
		prof.setPiSierialId("123ee224");
		prof.setName("TestP");
		return prof;
	}
	
}
