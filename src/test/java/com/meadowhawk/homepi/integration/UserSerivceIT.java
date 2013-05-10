package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.junit.Test;

import com.meadowhawk.homepi.integration.jax.HomePiUserTestFilter;
import com.meadowhawk.homepi.model.HomePiUser;

public class UserSerivceIT {

	static String BASE_URI = "/services/user/profile/";
	
	/**
	 * Helper, Get HomePiUser as JSON.
	 * @param user
	 * @return
	 */
	private static String getUserJson(HomePiUser user) {
		try{
			ObjectMapper mapper = new ObjectMapper();
			SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("privateUser",new HomePiUserTestFilter());
			// and then serialize using that filter provider:
			return mapper.writer(filters).writeValueAsString(user);
		} catch(Exception e) {
			fail("JSON Serialization failed." + e);
			return null;
		}
	}
	
	@Test
	public void testUpdateUser() throws JsonGenerationException, JsonMappingException, IOException{
		String userId = "test-user42";
		String org_email = "test-user2@homepi.org";
		HomePiUser user = new HomePiUser();
		String newUserName = "test-user42";
		user.setUserName(newUserName);
		user.setEmail("x@y.com"); //should not be changed, not updatable
		user.setFamilyName("SuperUser");
		user.setGivenName("Testaz");
		user.setPicLink("http://homepi.org/mypic.jpg");
		
		String json = getUserJson(user);

		given().log().all().port(8088).headers("access_token","XYZ-123").
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
    statusCode(200).log().body().
    body("userName",equalToIgnoringCase(newUserName),
    		"updateTime", notNullValue(),
    		"email", equalToIgnoringCase(org_email)
    		).
    when().
    post(BASE_URI+userId);
	}
	
	@Test
	public void testUpdateUserInvalidAuthToken() throws JsonGenerationException, JsonMappingException, IOException{
		String userId = "test-user42";
		HomePiUser user = new HomePiUser();
		String newUserName = "test-user42";
		user.setUserName(newUserName);
		user.setEmail("x@y.com"); //should not be changed
		user.setFamilyName("SuperUser");
		user.setGivenName("Testaz");
		user.setPicLink("http://homepi.org/mypic.jpg");
		
		
		SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("privateUser",new HomePiUserTestFilter());
		// and then serialize using that filter provider:
		String json = getUserJson(user);

		given().log().all().port(8088).
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
    statusCode(403).log().body().
    when().
    post(BASE_URI+userId);
		
		given().log().all().port(8088).headers("access_token","BAD-TOKEN").
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
    statusCode(403).log().body().
    when().
    post(BASE_URI+userId);
	}
	
	@Test
	public void testGetUser_Public(){
		String userId = "test_user";
		
		given().port(8088).
		expect().statusCode(200).log().body().
    body("email", nullValue(),
        "givenName", nullValue(),
        "familyName", nullValue(),
        "fullName", nullValue(),
        "googleAuthToken", nullValue(),
        "locale", equalTo("en"),
        "userName",equalTo(userId),
        "userId", equalTo(1)).when().
    	get(BASE_URI+userId);
	}
	
	@Test
	public void testGetUser_Private(){
		String userId = "test_user";
		
		given().port(8088).headers("access_token","XD123-YT53"). 
		expect().statusCode(200).log().body().
    body("privateVersion", nullValue(),
    		"email", equalToIgnoringCase("tester@homepi.com"),
        "givenName", equalToIgnoringCase("Test"),
        "familyName", equalToIgnoringCase("User"),
        "fullName", equalToIgnoringCase("Test User"),
        "locale", equalTo("en"),
        "userName",equalTo(userId),
        "userId", equalTo(1)).when().
    	get(BASE_URI+userId);
	}
	
}
