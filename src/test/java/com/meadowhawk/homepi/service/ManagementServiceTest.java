package com.meadowhawk.homepi.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.dao.ManagedAppDAO;
import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.dao.PiProfileDAOTest;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.ManagedAppsService;
import com.meadowhawk.homepi.service.business.DeviceManagementService;
import com.meadowhawk.util.RandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class ManagementServiceTest {

	@Autowired
	DeviceManagementService managedService;
	
	@Autowired
	PiProfileDAO piProfileDao;
	
	@Test(expected=HomePiAppException.class)
	public void testGetPiProfile() {
		String serialId = "InvalidSerialId1234";
		managedService.getPiProfile(serialId);
	}

	@Test
	public void testGetAllPiProfiles() {
		List<PiProfile> resp = managedService.getAllPiProfiles();
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}

	@Test
	public void testUpdateApiKey() {
		PiProfile profile = piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		final String oldApiKey = profile.getApiKey();
		assertNotNull(profile);
		managedService.updateApiKey(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL, profile.getApiKey());
		
		PiProfile updatedProfile = piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		assertFalse(updatedProfile.getApiKey().equals(oldApiKey));
	}
	
	@Test(expected=HomePiAppException.class)
	public void testUpdateApiKeyBadAPIKey() {
		managedService.updateApiKey(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL, new RandomString(30).nextString());
		
		piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
	}
	
	@Test
	public void testCreateProfile() {
		String piSerialId = new RandomString(50).nextString();
		String ipAddress = "192.168.1.20";
		PiProfile resp = managedService.createPiProfile(piSerialId, ipAddress );
		assertNotNull(resp);
		assertNotNull(resp.getPiId());
		assertNotNull(resp.getApiKey());
		assertEquals(piSerialId,resp.getPiSerialId());
		assertEquals(ipAddress,resp.getIpAddress());
		
		//Clean up
		piProfileDao.delete(resp);
	}
	
	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileNullCheck() {
		managedService.createPiProfile(null, null);
	}
	
	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileBlanksCheck() {
		String piSerialId = "";
		String ipAddress = "";
		managedService.createPiProfile(piSerialId, ipAddress);
		
	}

	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileDupeId() {
		String ipAddress = "123.123.123.123";
		managedService.createPiProfile(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL, ipAddress);
	}
	
	@Test
	public void testUpdatePiProfileBadFieldValue() {
		PiProfile resp = managedService.getPiProfile(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		resp.setName("Test Pi");
		
		managedService.updatePiProfile(resp);
	}

}
