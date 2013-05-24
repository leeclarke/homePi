package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.model.LogData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class LogDataDAOTest {

	private static final String LOG_KEY = "Device Temp";
	private static final String SYSTEM_LOG_TYPE = "SYSTEM";
	
	@Autowired
	LogDataDAO logDataDAO;
	
	@Test
	public void testSaveFindBySerial() {
		LogData logData = new LogData();
		Long appId = 6L;
		Long userId = 1L;
		Long piId = 1L;
		String piSerialId = "12345";
		String logMessage = "68.5";

		
		List<LogData> resp = logDataDAO.findBySerialId(piSerialId);
		assertNotNull(resp);
		assertTrue(resp.size()>0);
		
		logData.setAppId(appId);
		logData.setUserId(userId);
		logData.setPiId(piId);
		logData.setLogKey(LOG_KEY);
		logData.setLogMessage(logMessage);

		logDataDAO.save(logData);
		
		//verify that there are now more
		List<LogData> resp2 = logDataDAO.findBySerialId(piSerialId);
		assertNotNull(resp);
		assertTrue(resp2.size()>resp.size());
	}
	
	@Test
	public void testFindByLogKey() {
		List<LogData> resp = logDataDAO.findByLogType(SYSTEM_LOG_TYPE);
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}
	
	@Test
	public void testFindByLogType() {
		List<LogData> resp = logDataDAO.findByLogType(SYSTEM_LOG_TYPE);
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}
}
