package com.meadowhawk.homepi.util.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleInfoTest {

	@Test
	public void testBuildGoogleInfo() {
		
		String jsonString = "{" +
				 "\"sub\": \"1234567890\"," +
				 "\"name\": \"Lee Clarke\"," +
				 "\"given_name\": \"Lee\"," +
				 "\"family_name\": \"test\"," +
				 "\"profile\": \"https://plus.google.com/109829574817987307394\"," +
				 "\"picture\": \"/AAAAAAAAAAI/AAAAAAAACR8/5SSgJOcVEWA/photo.jpg\"," +
				 "\"email\": \"lee.tester@homepi.org\"," +
				 "\"email_verified\": true," +
				 "\"gender\": \"male\"," +
//				 "\"birthdate\": \"0000-06-24\"," +
				 "\"locale\": \"en\"" +
				"}";
		
		GoogleInfo userInfo = GoogleInfo.buildGoogleInfo(jsonString );
		assertNotNull(userInfo);
		
		
	}

}
