package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.junit.Test;

import com.jayway.restassured.internal.RestAssuredResponseImpl;
import com.jayway.restassured.path.json.JsonPath;
import com.meadowhawk.homepi.integration.jax.PiProfileTestFilter;
import com.meadowhawk.homepi.model.PiProfile;

public class HomePiRestServiceIT {
	static final String BASE_URI = "/services/homepi/user/";
	static final String PI_URI = "/pi/";
	
	private static String getBaseUserUri(String userId) {
		return BASE_URI + userId + PI_URI;
	}
	
	/**
	 * Helper, Get PiProfile as JSON.
	 * @param profile
	 * @return
	 */
	private static String getProfileJson(PiProfile profile) {
		try{
			ObjectMapper mapper = new ObjectMapper();
			SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("privateView",new PiProfileTestFilter());
			// and then serialize using that filter provider:
			return mapper.writer(filters).writeValueAsString(profile);
		} catch(Exception e) {
			fail("JSON Serialization failed." + e);
			return null;
		}
	}
	
	///services/homepi/user/{user_id}/pi/{piSerialId}
	@Test
	public void testGetUser_PiProfile_Public(){
		String serialId = "2e848bg934";
		String userId = "test_user";
		
		given().port(8088).
		expect().statusCode(200).log().body().
		body("apiKey", nullValue(),
	        "ipAddress", nullValue(),
	        "name", notNullValue(),
	        "piSerialId", equalTo(serialId)).when().
    	get(getBaseUserUri(userId) + serialId);
	}
	
	@Test
	public void testGetUser_PiProfile_Private(){
		String serialId = "2e848bg934";
		String userId = "test_user";
		
		given().port(8088).headers("access_token","XD123-YT53").
		expect().statusCode(200).log().body().
		body("apiKey", notNullValue(),
	        "ipAddress", notNullValue(),
	        "name", notNullValue(),
	        "piSerialId", equalTo(serialId)).when().
    	get(getBaseUserUri(userId) + serialId);
	}
//	access_token: XD123-YT53
	
	///services/homepi/user/{user_id}/pi
	@Test
	public void testGetUser_PiProfiles_Public(){
		String userId = "test_user";
		
		Object resp = given().port(8088).
		expect().statusCode(200).log().body().
	    body("name", notNullValue(),
	        "piSerialId", notNullValue()).when().
    	get(getBaseUserUri(userId));
		
		String body = ((RestAssuredResponseImpl)resp).body().asString();
		assertFalse(body.contains("apiKey"));
		assertFalse(body.contains("ipAddress"));
	}
	
	@Test
	public void testGetUser_PiProfiles_Private(){
		String userId = "test_user";
		
		Object resp = given().port(8088).headers("access_token","XD123-YT53").
				expect().statusCode(200).log().body().
		    body("name", notNullValue(),
		        "piSerialId", notNullValue()).when().
		    	get(getBaseUserUri(userId));
				
//		System.out.println((RestAssuredResponseImpl)resp);
		String body = ((RestAssuredResponseImpl)resp).body().asString();
		assertTrue(body.contains("apiKey"));
		assertTrue(body.contains("ipAddress"));
	}
	
	
	///POST services/homepi/user/{user_id}/pi/{piSerialId}/api
	@Test
	public void testGetNewApiKey() {
		String serialId = "hls1zeugsi";
		String userId = "test_user";

		//Get current apiKey for validation
		Object resp = given().port(8088).headers("access_token","XD123-YT53").
		expect().statusCode(200).log().body().
		body("apiKey", notNullValue(),
	        "ipAddress", notNullValue(),
	        "name", notNullValue(),
	        "piSerialId", equalTo(serialId)).when().
    	get(getBaseUserUri(userId) + serialId);
		
		
		JsonPath body = ((RestAssuredResponseImpl)resp).body().jsonPath();
		
		String oldApiKey = body.getString("apiKey");
		
		//Make the call
		given().port(8088).headers("access_token","XD123-YT53").
		expect().statusCode(204).log().body().
			when().
    post(getBaseUserUri(userId) + serialId+ "/api");
		
		//Get updated results and compare.
		Object resp2 = given().port(8088).headers("access_token","XD123-YT53").
				expect().statusCode(200).log().body().
				body("apiKey", notNullValue(),
			        "ipAddress", notNullValue(),
			        "name", notNullValue(),
			        "piSerialId", equalTo(serialId)).when().
		    	get(getBaseUserUri(userId) + serialId);
		
		JsonPath body2 = ((RestAssuredResponseImpl)resp2).body().jsonPath();
		assertFalse(oldApiKey.equals(body2.getString("apiKey")));
	}
	
	//POST /services/homepi/user/{user_id}/pi/{piSerialId}  
	@Test
	public void testUpdateProfile() {
		String serialId = "2e848bg934";
		String userId = "test_user";
		String apiKey = "035a0f4c-5dd2-4805-aa0c-9a545a738c51";
		String name = "New Name";
		String ipAddress = "129.168.1.6";
		
		PiProfile profile = new PiProfile();
		profile.setApiKey( UUID.fromString(apiKey));
		profile.setIpAddress(ipAddress);
		profile.setName(name);
		profile.setPiSerialId(serialId);
		profile.setSshPortNumber(9090);
		
		String json = getProfileJson(profile);
		
		given().log().all().port(8088).headers("access_token","XD123-YT53").
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
		    statusCode(204).log().body().
		when().
		    post(getBaseUserUri(userId) + serialId);
		
		//Add GET to verify results.
		given().port(8088).headers("access_token","XD123-YT53").
				expect().statusCode(200).log().body().
		    body("name", equalTo(name),
		        "piSerialId", equalTo(serialId),
		        "ipAddress",equalTo(ipAddress),
		        "apiKey", notNullValue(),
		        "sshPortNumber", notNullValue()).when().
		    	get(getBaseUserUri(userId) + serialId);
		
	}
	
	@Test
	public void testUpdateProfile_badAuth() {
		String serialId = "2e848bg934";
		String userId = "test_user";
		String apiKey = "035a0f4c-5dd2-4805-aa0c-9a545a738c51";
		String name = "New Name";
		String ipAddress = "129.168.1.6";
		
		PiProfile profile = new PiProfile();
		profile.setApiKey( UUID.fromString(apiKey));
		profile.setIpAddress(ipAddress);
		profile.setName(name);
		profile.setPiSerialId(serialId);
		profile.setSshPortNumber(9090);
		
		String json = getProfileJson(profile);
		
		given().log().all().port(8088).headers("access_token","XYZ-123").
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
    statusCode(403).log().body().
    when().
    post(getBaseUserUri(userId) + serialId);
	}
	
	
	//View User
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
	
	//GET /user/{user_id}/pi/{piSerialId}/log
	@Test
	public void testLogRetrieval() {
		//TODO: code hasn't been designed yet.
		fail("code hasn't been designed yet.");
	}
}
