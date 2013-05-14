package com.meadowhawk.homepi.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.joda.time.DateTime;
import org.junit.Test;

import com.meadowhawk.homepi.model.filter.HomePiPrivateFilter;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class HomePiPrivateFilterTest {
	
	@Test
	public void testFiltering() throws JsonGenerationException, JsonMappingException, IOException {
		HomePiUser user = new HomePiUser();
		String userName = "junit@homepi.org";
		user.setUserName(userName );
		user.setEmail(userName);
		user.setGoogleAuthToken("7has87fn0w94rn0an9fay0w9rnfgw0r7ng9");
		user.setGivenName("J");
		user.setFamilyName("Unit");
		user.setFullName("JUnit");
		user.setMaskedView(true);
		ObjectMapper mapper = getConfiguredMapper();
		
//		System.out.println(mapper.writeValueAsString(user));
		assertTrue(!mapper.writeValueAsString(user).contains("googleAuthToken"));
		
		//switch to unmasked view.
		user.setMaskedView(false);
		assertTrue(mapper.writeValueAsString(user).contains("googleAuthToken"));
	}

	
	private ObjectMapper getConfiguredMapper(){
		ObjectMapper mapper = new ObjectMapper();
	      SerializationConfig serConfig = mapper.getSerializationConfig();
	      
	      SimpleFilterProvider fp = new SimpleFilterProvider();
	      
	      fp.addFilter("privateView", new HomePiPrivateFilter());
	      
	      mapper.setSerializationConfig(serConfig.withFilters(fp));
	      mapper.setFilters(fp);
	      return mapper;
	}
	
	@Test
	public void testFilteringProfile() throws JsonGenerationException, JsonMappingException, IOException {
		PiProfile profile = new PiProfile();
		String userName = "junit@homepi.org";
		UUID apiKey = UUID.fromString("035a0f4c-5dd2-4805-aa0c-9a545a738c51");
		profile.setPiId(332211L);
		profile.setApiKey(apiKey);
		profile.setIpAddress("127.1.1.1");
		profile.setCreateTime(new DateTime());
		profile.setName("Test Profile");
		profile.setSshPortNumber(22);
		profile.setMaskedView(true);
		ObjectMapper mapper = getConfiguredMapper();
		
		System.out.println(mapper.writeValueAsString(profile));
		assertTrue(!mapper.writeValueAsString(profile).contains("apiKey"));
		
		//switch to unmasked view.
		profile.setMaskedView(false);
		assertTrue(mapper.writeValueAsString(profile).contains("apiKey"));
	}

	@Test
	public void testManagedAppFiltering() throws JsonGenerationException, JsonMappingException, IOException {
		ManagedApp ma = new ManagedApp();
		ma.setAppId(123L);
		ma.setAppName("Test App");
		ma.setDeploymentPath("/usr/some/path");
		ma.setMaskedView(true);
		
		ObjectMapper mapper = getConfiguredMapper();
		
		System.out.println(mapper.writeValueAsString(ma));
		assertTrue(!mapper.writeValueAsString(ma).contains("deploymentPath"));
		
		ma.setMaskedView(false);
		assertTrue(mapper.writeValueAsString(ma).contains("deploymentPath"));
	}
	
}
