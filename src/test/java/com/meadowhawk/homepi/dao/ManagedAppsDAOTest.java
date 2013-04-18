package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.ManagedApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class ManagedAppsDAOTest {

	@Autowired
	ManagedAppDAO managedAppsDAO;
	
	@Test
	public void testCreateGetManagedApps() {
		String piSerialId = "2e848bg934";
		ManagedApp mngAppCreate = new ManagedApp();
		Long ownerId = 0L;
		mngAppCreate.setOwnerId(ownerId );
		
		
		List<ManagedApp> response = managedAppsDAO.getManagedApps(piSerialId);
		assertNotNull(response);
		assertTrue("No results returned",response.size()>0);
	}

	@Test
	public void testGetManagedAppsFailed() {
		String piSerialId = "InvalidID";
		
		List<ManagedApp> response = managedAppsDAO.getManagedApps(piSerialId);
		assertNotNull(response);
		assertTrue(response.size() == 0);
	}
	
}
