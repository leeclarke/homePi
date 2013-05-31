package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.joda.time.DateTime;
import org.junit.Test;

import com.jayway.restassured.internal.RestAssuredResponseImpl;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.meadowhawk.homepi.integration.jax.ManagedAppTestFilter;
import com.meadowhawk.homepi.model.LogData;

public class DeviceRestServiceIT {
	static final String BASE_URI = "/services/homepi/device/pi/";
	
	static final String BASE_HOMEPI_URI = "/services/homepi/user/";
	static final String PI_URI = "/pi/";
	static final String apiKey = "035a0f4c-5dd2-4805-aa0c-9a545a738c51";
	
	private static String getBaseUserUri(String userId) {
		return BASE_HOMEPI_URI + userId + PI_URI;
	}
	
	//POST /pi/{piSerialId}/api
	@Test
	public void testGetNewApiKey() {	
		String serialId = "2e848bg934";
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
		given().port(8088).headers("api_key",oldApiKey).
		expect().statusCode(204).log().body().
			when().
		post(BASE_URI + serialId + "/api");
		
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
	
	//POST /pi/{piSerialId}/reg
	//TODO: Really need to be able to delete these after they get created but there isn't a REST delete function.
	
	
	//GET /pi/{piSerialId}
	@Test
	public void testGetProfile() {
		String serialId = "pi456y765";

		//Get current apiKey for validation
		given().port(8088).headers("api_key","8b72f46d-b470-43d4-8919-6148aeb152e3").
		expect().statusCode(200).log().body().
		body("apiKey", notNullValue(),
	        "ipAddress", notNullValue(),
	        "name", notNullValue(),
	        "piSerialId", equalTo(serialId)).when().
    	get(BASE_URI + serialId);
	}	
	
	
	
	//	GET /pi/update
	@Test
	public void testPiSoftwareUpdate() {
		String piSerialId = "2e848bg934";  //TODO: Should add this for tracking updates.
		
		given().port(8088).headers("api_key",apiKey ).
			expect().statusCode(200).log().body().contentType("text/x-python").
    body(notNullValue()).when().
    	get(BASE_URI+"update");
	}
	
	//POST /pi/{piSerialId}/log
  //GET /pi/{piSerialId}/log
	@Test
	public void testCreateLogEntry() {
		String piSerialId = "12345";  
		String thisApiKey = "cb9c270c-9c5c-49a4-af93-9bc286071140";
		String logKey = "AutoTest";		
		String logMessage = "AutoTestValue";
		LogData logData = new LogData();
		
		logData.setCreateTime(new DateTime());
		logData.setLogKey(logKey);
		logData.setLogMessage(logMessage);
		
		//get existing data for key
		Response resp = given().port(8088).headers("api_key",thisApiKey ).
			expect().statusCode(200).log().body().
	  body(notNullValue()).when().
	  	get(BASE_URI + piSerialId + "/log?log_key="+logKey);
		
		JsonPath body = ((RestAssuredResponseImpl)resp).body().jsonPath();
		final int existingCount = body.getList("$").size(); 
		
		
		String json = getLogDataAppJson(logData);
		
		given().log().all().port(8088).headers("api_key",thisApiKey).
		contentType(MediaType.APPLICATION_JSON).body(json).
		expect().
		    statusCode(Status.NO_CONTENT.getStatusCode()).log().body().
		when().
		    post(BASE_URI + piSerialId + "/log");
		
		
		//get new data for key
		Response resp2 = given().port(8088).headers("api_key", thisApiKey).
			expect().statusCode(200).log().body().
	  body(notNullValue()).when().
	  	get(BASE_URI + piSerialId + "/log?log_key="+logKey);
		
		JsonPath body2 = ((RestAssuredResponseImpl)resp2).body().jsonPath();
		final int newCount = body2.getList("$").size();
		
		assertFalse("List Size matches after a POST.. create must have failed.",newCount == existingCount);
		
	}
	
	/**
	 * Helper, converts an logData object into Json for POSTs
	 */
	private static String getLogDataAppJson(LogData logData) {
		try{
			ObjectMapper mapper = new ObjectMapper();
			SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("privateView",new ManagedAppTestFilter());
			// and then serialize using that filter provider:
			return mapper.writer(filters).writeValueAsString(logData);
		} catch(Exception e) {
			fail("JSON Serialization failed." + e);
			return null;
		}
	}	
}
