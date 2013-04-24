package com.meadowhawk.homepi.util.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.util.service.model.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class AppConfigServiceTest {

	@Autowired
	AppConfigService appConfigService;
	
	@Test
	public void testLoad() {
		
		AppConfig resp = appConfigService.getAppConfig();
		assertNotNull(resp);
		assertNotNull(resp.getByKey("google_auth_client_id"));
	}

}
