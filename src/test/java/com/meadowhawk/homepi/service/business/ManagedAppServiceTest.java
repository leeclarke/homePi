package com.meadowhawk.homepi.service.business;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.ManagedApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class ManagedAppServiceTest {

	@Autowired
	ManagedAppsService managedAppsService;
	
	@Test
	public void testUpdateManagedApp() {
		
		String userName = "test_user";
		String authToken = "XD123-YT53";
		String appName = "TestApp";
		Long v1 = 2L;
		Long version = System.currentTimeMillis();
		String deploymentPath = "/usr/home/pi/test/"+version;
		String fileName = version + "test.jar";
		long ownerId = 99L;
		
		ManagedApp managedApp =new ManagedApp();
		managedApp.setDeploymentPath(deploymentPath);
		managedApp.setVersionNumber(v1);
		managedApp.setFileName(fileName);
		
		managedApp.setOwnerId(ownerId);
		managedApp.setAppId(ownerId);
		
		managedAppsService.updateUserApps(userName, authToken, appName, managedApp);
		
		//get updated object
		ManagedApp updatedMA = managedAppsService.getUserApp(userName, authToken, appName);
		assertNotNull(updatedMA);
		assertEquals(v1, updatedMA.getVersionNumber());
		assertEquals(deploymentPath, updatedMA.getDeploymentPath());
		assertEquals(fileName, updatedMA.getFileName());
		assertEquals(appName, updatedMA.getAppName()); //didnt update this.
		
		assertFalse("AppId should not be updatable.",ownerId == updatedMA.getAppId());
		assertFalse("UserId should not be updatable.",ownerId == updatedMA.getOwnerId());
	}
	
	@Test
	public void testCreateDeleteManagedApp() {
		
		String userName = "test_user";
		String authToken = "XD123-YT53";
		String appName = "Test App Delete";
		Long v1 = 1L;
		Long version = System.currentTimeMillis();
		String deploymentPath = "/usr/home/pi/test/"+version;
		String fileName = version + "test.jar";
		
		ManagedApp managedApp =new ManagedApp();
		managedApp.setDeploymentPath(deploymentPath);
		managedApp.setVersionNumber(v1);
		managedApp.setFileName(fileName);		
		managedApp.setAppName(appName);
		
		ManagedApp updatedMA = managedAppsService.createUserApp(userName, authToken, managedApp);
		
		assertNotNull(updatedMA);
		assertNotNull(updatedMA.getAppId());
		assertNotNull(updatedMA.getOwnerId());
		assertEquals(v1, updatedMA.getVersionNumber());
		assertEquals(deploymentPath, updatedMA.getDeploymentPath());
		assertEquals(fileName, updatedMA.getFileName());
		assertEquals(appName, updatedMA.getAppName()); //didnt update this.

		//Now delete
		managedAppsService.deleteManageApp(userName, authToken, appName);
		
		//See if still there.
		try{
			ManagedApp resp = managedAppsService.getUserApp(userName, authToken, appName);
			fail("Shouldn't make it here.");
		} catch(HomePiAppException e){
			
			assertEquals("Not found exception is what we want here..",Status.NOT_FOUND,e.getStatus());
		}
	}
}
