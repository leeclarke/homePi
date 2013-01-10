package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.util.RandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class PiProfileDAOTest {

	@Autowired
	PiProfileDAO piProfileDao;
	
	@Test
	public void testGetPiProfile() {
		String piSierialId = "2e848bg934";
		
		PiProfile respProfile = piProfileDao.getPiProfile(piSierialId);
		assertNotNull(respProfile);
		assertEquals(piSierialId, respProfile.getPiSerialId());
		assertEquals("Test Pi", respProfile.getName());
		assertEquals("129.168.1.52", respProfile.getIpAddress());
	}

	@Test
	public void testCreateGetPiProfile() {
		PiProfile profile = new  PiProfile();
		String ipAddress = "129.168.1.52";
		String name = "Test Pi";
		String piSierialId = new RandomString(10).nextString();
		
		profile.setIpAddress(ipAddress);
		profile.setName(name);
		profile.setPiSerialId(piSierialId);
		int resp = piProfileDao.createPiProfile(profile);
		assertEquals(1, resp);
		
		
		//call get to validate
		PiProfile respProfile = piProfileDao.getPiProfile(piSierialId);
		assertNotNull(respProfile);
		assertEquals(piSierialId, respProfile.getPiSerialId());
		assertEquals(name, respProfile.getName());
		assertEquals(ipAddress, respProfile.getIpAddress());
		
	}

}
