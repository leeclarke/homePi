package com.meadowhawk.homepi.util.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.util.service.model.AppConfigEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class AppConfigDaoTest {

	@Autowired
	AppConfigEntryDao appConfigDao;
	
	@Test
	public void testLoadAppConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		List<AppConfigEntry> resp = appConfigDao.findAll();
		assertNotNull(resp);
		assertTrue(resp.size() > 0);
	}

}
