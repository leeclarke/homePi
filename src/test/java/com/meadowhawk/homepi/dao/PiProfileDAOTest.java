package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.util.RandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class PiProfileDAOTest {

	@Autowired
	PiProfileDAO piProfileDao;
	
	@Test
	public void testGetPiProfile() throws HomePiAppException {
		String piSierialId = "2e848bg934";
		
		PiProfile respProfile = piProfileDao.getPiProfile(piSierialId);
		assertNotNull(respProfile);
		assertEquals(piSierialId, respProfile.getPiSerialId());
		assertEquals("Test Pi", respProfile.getName());
		assertEquals("129.168.1.52", respProfile.getIpAddress());
	}

	@Test(expected=HomePiAppException.class)
	public void testGetPiProfile_invalid() throws HomePiAppException {
		String piSierialId = "-1234567";
		piProfileDao.getPiProfile(piSierialId);
	}
	
	@Test
	public void testGetAllPiProfiles() throws HomePiAppException {
		
		List<PiProfile> respProfile = piProfileDao.getAllPiProfiles();
		assertNotNull(respProfile);
		assertTrue(respProfile.size() > 0);
	}
	
	
	@Test
	public void testCreateGetPiProfile() throws HomePiAppException {
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
		assertNotNull(respProfile.getCreateTime());
		assertNotNull(respProfile.getUpdateTime());
		
		//Clean up
		int del =piProfileDao.deletePiProfile(respProfile);
		assertTrue(del > 0);
	}

	@Test
	public void testUpdate(){
		String piSierialId = "2e848bg934";
		
		PiProfile respProfile = piProfileDao.getPiProfile(piSierialId);
		assertNotNull(respProfile);
		assertEquals(piSierialId, respProfile.getPiSerialId());
		
		respProfile.setSshPortNumber(22);
		
		int update = piProfileDao.updatePiProfile(respProfile);
		assertEquals(1, update);
		
		PiProfile updatedProfile = piProfileDao.getPiProfile(piSierialId);
		assertNotNull(updatedProfile);
		assertEquals(new Integer(22), updatedProfile.getSshPortNumber());
		assertFalse(respProfile.getUpdateTime().equals(updatedProfile.getUpdateTime()));
	}
}
