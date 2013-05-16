package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

public class DeviceRestServiceIT {
	
	
	//	GET /services/homepi/update
	@Test
	public void testPiSoftwareUpdate() {
		String api_key = "";
		given().port(8088).headers("api_key",api_key ).
		expect().statusCode(200).log().body().
    body(notNullValue()).when().
    	get("/services/homepi/update");
	}
}
