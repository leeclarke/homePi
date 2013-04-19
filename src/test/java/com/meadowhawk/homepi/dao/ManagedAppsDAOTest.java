package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.ManagedApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class ManagedAppsDAOTest {

	@Autowired
	ManagedAppDAO managedAppsDAO;
	
//	@Test
//	public void testCreateGetManagedApps() {
//		String piSerialId = "2e848bg934";
//		ManagedApp mngAppCreate = new ManagedApp();
//		Long ownerId = 0L;
//		mngAppCreate.setOwnerId(ownerId );
//		
//		
//		List<ManagedApp> response = managedAppsDAO.getManagedApps(piSerialId);
//		assertNotNull(response);
//		assertTrue("No results returned",response.size()>0);
//	}

//	@Test
//	public void testGetManagedAppsFailed() {
//		String piSerialId = "InvalidID";
//		
//		List<ManagedApp> response = managedAppsDAO.getManagedApps(piSerialId);
//		assertNotNull(response);
//		assertTrue(response.size() == 0);
//	}
	
	@Test(expected=NoResultException.class)
	public void testSaveFindDelete() {
		String appName = "TestApp";
		String deploymentPath = "/usr/home/pi/test";
		String fileName = "TestFile.jar";
		
		Long versionNumber = 1L;
		Long ownerId = 1L;
		ManagedApp entity = new ManagedApp();
		
		entity.setFileName(fileName);
		entity.setVersionNumber(versionNumber);
		entity.setDeploymentPath(deploymentPath);
		entity.setAppName(appName);
		entity.setOwnerId(ownerId );
		
		managedAppsDAO.save(entity);
		assertNotNull(entity.getAppId());
		final Long newId = new Long(entity.getAppId());
		
		managedAppsDAO.delete(entity);
		managedAppsDAO.findOne(newId);
	}
	
	@Test
	public void testThis(){
		String appName = "TestApp";
		String deploymentPath = "/usr/home/pi/test";
		String fileName = "TestFile.jar";
		
		Long versionNumber = 1L;
		Long ownerId = 1L;
		ManagedApp entity = new ManagedApp();
		
		entity.setFileName(fileName);
		entity.setVersionNumber(versionNumber);
		entity.setDeploymentPath(deploymentPath);
		entity.setAppName(appName);
		entity.setOwnerId(ownerId );
		
		managedAppsDAO.save(entity);
		assertNotNull(entity.getAppId());
		final Long newId = new Long(entity.getAppId());
		System.out.println("newId:" + newId);
	}
	
}
