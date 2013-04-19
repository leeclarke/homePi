package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.NoResultException;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.util.RandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class PiProfileDAOTest {

	@Autowired
	PiProfileDAO piProfileDao;
	
	//Existing Valid PiProfile to use for testing where expected exists. Don't delete. 
	public static final String DEFAULT_EXISTING_PI_SERIAL = "2e848bg934";
	
	@Test
	public void testGetPiProfile() {
		
		PiProfile respProfile = piProfileDao.findByPiSerialId(DEFAULT_EXISTING_PI_SERIAL);
		assertNotNull(respProfile);
		assertEquals(DEFAULT_EXISTING_PI_SERIAL, respProfile.getPiSerialId());
		assertEquals("Test Pi", respProfile.getName());
		assertEquals("129.168.1.52", respProfile.getIpAddress());
		
	}

	@Test
	public void testGetAllPiProfiles() throws HomePiAppException {
		
		List<PiProfile> respProfile = piProfileDao.findAll();
		assertNotNull(respProfile);
		assertTrue(respProfile.size() > 0);
	}
	
	
	@Test(expected=NoResultException.class)
	public void testCreateGetPiProfile() {
		PiProfile profile = new  PiProfile();
		String ipAddress = "129.168.1.52";
		String name = "Test Pi";
		Long userId = 1L;
		String piSierialId = new RandomString(10).nextString();
		
		profile.setIpAddress(ipAddress);
		profile.setName(name);
		profile.setPiSerialId(piSierialId);
		
		profile.setUserId(userId);
		piProfileDao.save(profile);
		assertNotNull(profile.getPiId());
		
		//call get to validate
		PiProfile respProfile = piProfileDao.findByPiSerialId(piSierialId);
		assertNotNull(respProfile);
		assertEquals(piSierialId, respProfile.getPiSerialId());
		assertEquals(name, respProfile.getName());
		assertEquals(ipAddress, respProfile.getIpAddress());
		assertNotNull(respProfile.getCreateTime());
		
		//Clean up
		piProfileDao.delete(respProfile);
		piProfileDao.findByPiSerialId(piSierialId); //CAuses a NoResultException as expected.
		
	}

	@Test
	public void testUpdate(){
		
		PiProfile respProfile = piProfileDao.findByPiSerialId(DEFAULT_EXISTING_PI_SERIAL);
		assertNotNull(respProfile);
		assertEquals(DEFAULT_EXISTING_PI_SERIAL, respProfile.getPiSerialId());
		
		respProfile.setSshPortNumber(22);
		final DateTime priorUpdateTime = new DateTime(respProfile.getUpdateTime());
		
		//Test adding a ManagedApp
		String appName = "TestApp";
		String deploymentPath = "/usr/home/pi/test";
		String fileName = "TestFile.jar";
		
		Long versionNumber = 1L;
		Long ownerId = 1L;
		ManagedApp newApp = new ManagedApp();
		
		newApp.setFileName(fileName);
		newApp.setVersionNumber(versionNumber);
		newApp.setDeploymentPath(deploymentPath);
		newApp.setAppName(appName);
		newApp.setOwnerId(ownerId );
		
		respProfile.getManagedApps().add(newApp);
		
		piProfileDao.update(respProfile);
		
		PiProfile updatedProfile = piProfileDao.findByPiSerialId(DEFAULT_EXISTING_PI_SERIAL);
		assertNotNull(updatedProfile);
		assertEquals(new Integer(22), updatedProfile.getSshPortNumber());
		assertFalse(priorUpdateTime.equals(updatedProfile.getUpdateTime()));
		assertTrue(updatedProfile.getManagedApps().size()>0);
	}
}
