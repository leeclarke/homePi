package com.meadowhawk.homepi.service.business;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.PiProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class HomePiUserServiceTest {

	@Autowired
	HomePiUserService homePiUserService;
	
	@Test
	public void testGetPiProfileNoAuth() {
		String userName = "test_user";
		String authToken = "badToken";
		String piSerialId = "2e848bg934";
		PiProfile resp = homePiUserService.getPiProfile(userName, authToken, piSerialId);
		
		assertNotNull(resp);
		assertTrue(resp.isMaskedView());
	}

	@Test
	public void testGetPiProfile() {
		String userName = "test_user";
		String authToken = "XD123-YT53";
		String piSerialId = "2e848bg934";
		PiProfile resp = homePiUserService.getPiProfile(userName, authToken, piSerialId);
		
		assertNotNull(resp);
		assertFalse(resp.isMaskedView());
	}
	
	@Test
	public void testGetPiProfileNullAuthValues() {
		String userName = null;
		String authToken = null;
		String piSerialId = "2e848bg934";
		PiProfile resp = homePiUserService.getPiProfile(userName, authToken, piSerialId);
		
		assertNotNull(resp);
		assertTrue(resp.isMaskedView());
	}
	
	@Test(expected=HomePiAppException.class)
	public void testUpdateUserDataBadToken(){
		String userName = "test_user";
		String authToken = "XD123-YT53";
		String authBadToken = "XD123-YXXXXT53";
		
		HomePiUser updateUser = homePiUserService.getUserData(userName, authToken);
		assertNotNull(updateUser);
		updateUser.setEmail("spam@goo.com");
		
				
		homePiUserService.updateUserData(userName, authBadToken, updateUser );
		
	}
	
	@Test
	public void testAddDeleteAppToProfile() {
		String userName = "test_user";
		String authToken = "XD123-YT53";
		Long appId = 6L;
		String piSerialId = "2e848bg934";
		
		homePiUserService.addAppToProfile(userName, authToken, piSerialId , appId );
		
		homePiUserService.deleteAppToProfile(userName, authToken, piSerialId , appId );
	}
}
