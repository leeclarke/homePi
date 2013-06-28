package com.meadowhawk.homepi.util.service;

import static org.junit.Assert.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:localbeans.xml" })
public class ServiceUtilsTest {

	@Context UriInfo uriInfo; 

	@Test
	public void testLoad() throws URISyntaxException {
		String testUri = "http://homepi.org/services/user/googleauth";
		URI expected = new URI("http://homepi.org/dashboard.html");
		URI uri = new URI(testUri);
		String resourceFileName = "dashboard.html";
		URI resp = ServiceUtils.getUIResource(uri, resourceFileName, null);
		
		assertNotNull(resp);
		//TODO Add test and debug this.
		assertEquals(expected, resp);
	}

	@Test
	public void testLoadQP() throws URISyntaxException {
		String testUri = "http://homepi.org/services/user/googleauth";
		URI expected = new URI("http://homepi.org/dashboard.html?test=value");
		URI uri = new URI(testUri);
		String resourceFileName = "dashboard.html";
		Map<String, String> params = new HashMap<String, String>();
		params.put("test", "value");
		URI resp = ServiceUtils.getUIResource(uri, resourceFileName, params );
		
		assertNotNull(resp);
		//TODO Add test and debug this.
		assertEquals(expected, resp);
	}
}
