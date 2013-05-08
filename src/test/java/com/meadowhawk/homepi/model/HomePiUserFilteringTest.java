package com.meadowhawk.homepi.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class HomePiUserFilteringTest {
	
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
//		user.isPrivate = true;
		ObjectMapper mapper = getConfiguredMapper();
		
		System.out.println(mapper.writeValueAsString(user));
		
	}

	
	private ObjectMapper getConfiguredMapper(){
		ObjectMapper mapper = new ObjectMapper();
	      SerializationConfig serConfig = mapper.getSerializationConfig();
	      
	      SimpleFilterProvider fp = new SimpleFilterProvider();
	      
//	      Set<String> filterFields = new HashSet<String>();
//	      filterFields.add("googleAuthToken");
//	      filterFields.add("full_name");
	      fp.addFilter("privateUser", new HomePiUserFilter());
//	      fp.addFilter("onlyAFilter", filter);
	      mapper.setSerializationConfig(serConfig.withFilters(fp));
	      mapper.setFilters(fp);
	      return mapper;
	}
}
