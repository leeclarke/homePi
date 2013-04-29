package com.meadowhawk.homepi.util.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoogleInfoTest {

	@Test
	public void testBuildGoogleInfo() {
		
		String jsonString = "{" +
				 "\"sub\": \"109829574817987307394\"," +
				 "\"name\": \"Lee Clarke\"," +
				 "\"given_name\": \"Lee\"," +
				 "\"family_name\": \"Clarke\"," +
				 "\"profile\": \"https://plus.google.com/109829574817987307394\"," +
				 "\"picture\": \"https://lh4.googleusercontent.com/-5KIKQF7_2og/AAAAAAAAAAI/AAAAAAAACR8/5SSgJOcVEWA/photo.jpg\"," +
				 "\"email\": \"lee.k.clarke@gmail.com\"," +
				 "\"email_verified\": true," +
				 "\"gender\": \"male\"," +
//				 "\"birthdate\": \"0000-01-26\"," +
				 "\"locale\": \"en\"" +
				"}";
		
		GoogleInfo userInfo = GoogleInfo.buildGoogleInfo(jsonString );
		assertNotNull(userInfo);
		
		
	}

}
