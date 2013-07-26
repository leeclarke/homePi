package com.meadowhawk.homepi.service.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.dao.PiProfileDAOTest;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.util.RandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class DeviceManagementServiceTest {

	@Autowired
	DeviceManagementService deviceManagedService;
	
	@Autowired
	PiProfileDAO piProfileDao;
	
	@Test(expected=HomePiAppException.class)
	public void testGetPiProfile() {
		String serialId = "InvalidSerialId1234";
		deviceManagedService.getPiProfile(serialId);
	}
	
	@Test
	public void testGetPiProfileApiKey() {
		String piSerialId = "2e848bg934";
		String apiKey = "035a0f4c-5dd2-4805-aa0c-9a545a738c51";
		PiProfile resp = deviceManagedService.getPiProfile(piSerialId, apiKey);
		assertNotNull(resp);
		assertTrue(!resp.isMaskedView());
	}

	@Test
	public void testGetPiProfileApiKeyInvalid() {
		String piSerialId = "2e848bg934";
		String apiKey = "bad-key";
		PiProfile resp = deviceManagedService.getPiProfile(piSerialId, apiKey);
		assertNotNull(resp);
		assertTrue(resp.isMaskedView());
	}
	
	@Test
	public void testGetAllPiProfiles() {
		List<PiProfile> resp = deviceManagedService.getAllPiProfiles();
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}

	@Test
	public void testUpdateApiKey() {
		PiProfile profile = piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		final String oldApiKey = profile.getApiKey();
		assertNotNull(profile);
		deviceManagedService.updateApiKey( profile.getApiKey(), PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		
		PiProfile updatedProfile = piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		assertFalse(updatedProfile.getApiKey().equals(oldApiKey));
	}
	
	@Test(expected=HomePiAppException.class)
	public void testUpdateApiKeyBadAPIKey() {
		deviceManagedService.updateApiKey(new RandomString(30).nextString(),PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		
		piProfileDao.findByPiSerialId(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
	}
	
	@Test
	public void testCreateProfile() {
		String piSerialId = new RandomString(50).nextString();
		String ipAddress = "192.168.1.20";
		PiProfile resp = deviceManagedService.createPiProfile(piSerialId, ipAddress, null );
		assertNotNull(resp);
		assertNotNull(resp.getPiId());
		assertNotNull(resp.getApiKey());
		assertNotNull(resp.getName());
		assertNotNull(resp.getUserId());
		assertEquals(piSerialId,resp.getPiSerialId());
		assertEquals(ipAddress,resp.getIpAddress());
		
		//Clean up
		piProfileDao.delete(resp);
	}
	
	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileNullCheck() {
		deviceManagedService.createPiProfile(null, null,null);
	}
	
	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileBlanksCheck() {
		String piSerialId = "";
		String ipAddress = "";
		deviceManagedService.createPiProfile(piSerialId, ipAddress,null);
		
	}

	@Test(expected=HomePiAppException.class)
	public void testCreatePiProfileDupeId() {
		String ipAddress = "123.123.123.123";
		deviceManagedService.createPiProfile(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL, ipAddress, null);
	}
	
	@Test
	public void testUpdatePiProfileBadFieldValue() {
		PiProfile resp = deviceManagedService.getPiProfile(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		resp.setName("Test Pi");
		
		deviceManagedService.updatePiProfile(resp);
	}
	
	@Test(expected=HomePiAppException.class)
	public void testUpdateNoApiKey(){
		PiProfile resp = deviceManagedService.getPiProfile(PiProfileDAOTest.DEFAULT_EXISTING_PI_SERIAL);
		resp.setName("Test Pi");
		
		deviceManagedService.updatePiProfile(resp);
	}
	
	
}
