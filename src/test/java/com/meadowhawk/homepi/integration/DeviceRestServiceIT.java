package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.jayway.restassured.internal.RestAssuredResponseImpl;
import com.jayway.restassured.path.json.JsonPath;

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
//TODO: FIX		
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
		given().port(8088).headers("api_key",apiKey).
		expect().statusCode(204).log().body().
			when().
		post(BASE_URI + serialId + "/api");
		
		//Get updated results and compare.
		Object resp2 = given().port(8088).headers("api_key","XD123-YT53").
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
	
	//GET /pi/{piSerialId}
	
	
	//GET  /pi/{piSerialId}/log
  //POST /pi/{piSerialId}/log
	
	// /update
	
	//	GET /pi/update
	@Test
	public void testPiSoftwareUpdate() {
		String piSerialId = "2e848bg934";
		
		
		given().port(8088).headers("api_key",apiKey ).
			expect().statusCode(200).log().body().contentType("text/x-python").
    body(notNullValue()).when().
    	get(BASE_URI+"update");
	}
}
