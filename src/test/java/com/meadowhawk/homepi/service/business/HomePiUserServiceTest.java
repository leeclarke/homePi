package com.meadowhawk.homepi.service.business;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
