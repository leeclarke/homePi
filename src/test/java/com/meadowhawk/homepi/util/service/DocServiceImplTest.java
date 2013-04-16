package com.meadowhawk.homepi.util.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.util.model.ServiceDocMethodTO;
import com.meadowhawk.homepi.util.model.ServiceDocTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class DocServiceImplTest {
	private Log log = LogFactory.getLog(DocServiceImplTest.class);
	
	@Autowired
	DocService docService;

	@Test
	public void testGetEndpointDocs() {
		
		List<ServiceDocTO> list = docService.getEndpointDocs();
		assertNotNull(list);
		assertTrue(list.size() > 0);
		for (ServiceDocTO serviceDocTO : list) {
			assertNotNull(serviceDocTO);
			assertNotNull(serviceDocTO.getServiceName());
			assertNotNull(serviceDocTO.getServiceDescription());
			assertNotNull(serviceDocTO.getServicePath());
//			log.debug("Name: "+ serviceDocTO.getServiceName());
//			log.debug("Desc: "+ serviceDocTO.getServiceDescription());
//			log.debug("Path: "+ serviceDocTO.getServicePath());
			for (ServiceDocMethodTO methodDoc : serviceDocTO.getMethodDocs()) {
				assertNotNull(methodDoc.getEndPointName());
				assertNotNull(methodDoc.getEndPointRequestType());
				assertNotNull(methodDoc.getEndPointDescription());
				assertNotNull(methodDoc.getEndPointPath());
				assertNotNull(methodDoc.getEndPointProvides());
				assertNotNull(methodDoc.getErrors());
//				assertNotNull(methodDoc.getConsumes());
//				log.debug("####\n\tName: "+methodDoc.getEndPointName());
//				log.debug("\tMethod:"+methodDoc.getEndPointRequestType());
//				log.debug("\tDEsc: "+methodDoc.getEndPointDescription());
//				log.debug("\tPath: "+methodDoc.getEndPointPath());
//				log.debug("\tProcvides: "+methodDoc.getEndPointProvides());
//				log.debug("\tConsumes: "+methodDoc.getConsumes());
				
			}
		}
		
	}

}
