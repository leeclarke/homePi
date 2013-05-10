package com.meadowhawk.homepi.integration;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;

public class DocServiceIT {
	@Test
	public void testGetUser2(){
		
		given().port(8088).
		expect().statusCode(200).
    when().
    	get("/services/docs/index");
	}
}
