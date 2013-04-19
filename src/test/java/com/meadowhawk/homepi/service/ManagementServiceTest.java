package com.meadowhawk.homepi.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.dao.ManagedAppDAO;
import com.meadowhawk.homepi.dao.PiProfileDAOTest;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.ManagedAppsService;
import com.meadowhawk.homepi.service.business.ManagementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class ManagementServiceTest {

	@Autowired
	ManagementService managedService;
	
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
