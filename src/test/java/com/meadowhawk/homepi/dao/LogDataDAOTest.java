package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.dao.AbstractJpaDAO.DBSortOrder;
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
		List<LogData> resp = logDataDAO.findByLogKey(SYSTEM_LOG_TYPE);
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}
	
	@Test
	public void testFindByLogType() {
		String piSerialId = "12345";
		List<LogData> resp = logDataDAO.findByLogType(piSerialId, SYSTEM_LOG_TYPE);
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}
	
	//search is keyed on PiId  and [appId] optional.  Also can be filtered by Type and LogKey.  Can be sorted by CreateDate - default is DESC
	
	@Test
	public void testFindByDynamicParams(){
		
		String piSerialId = "12345";
		long appId = 6L;
		
		List<LogData> resp = logDataDAO.findByDynamicParams(piSerialId, appId);
		assertNotNull(resp);
		assertTrue(resp.size()>1);
		DateTime firstDT = resp.get(0).getCreateTime();
		DateTime secondDT = resp.get(1).getCreateTime();
		assertTrue("Default DESC sort, first create timeshould be after the next in list.", firstDT.isAfter(secondDT));
		
		//Refined Search with Type
		String logKey = "%Temp%";
		List<LogData> resp2 = logDataDAO.findByDynamicParams(piSerialId, appId, logKey);
		assertNotNull(resp2);
		assertTrue(resp2.size()>1);
		//TODO: verify log keys match
		
		
//		//make ascending order as well.
		List<LogData> resp3 = logDataDAO.findByDynamicParams(piSerialId, appId, null, null, DBSortOrder.ASC);
		assertNotNull(resp3);
		assertTrue(resp3.size()>1);
		DateTime firstDTA = resp3.get(0).getCreateTime();
		DateTime secondDTA = resp3.get(1).getCreateTime();
		
		assertTrue("ASC sort, first create timeshould be before the next in list.", firstDTA.isBefore(secondDTA));
		
	}
}
