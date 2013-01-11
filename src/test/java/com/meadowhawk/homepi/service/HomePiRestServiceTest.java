package com.meadowhawk.homepi.service;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HomePiRestServiceTest {

	@Autowired
	@Qualifier("homePiRestService")
	HomePiRestService homePiRestService;
	
	@Test
	public void testGetScriptUpdate() {
		Response resp = homePiRestService.getScriptUpdate();
		
		assertEquals(Status.OK.getStatusCode(), resp.getStatus());
	}

}
