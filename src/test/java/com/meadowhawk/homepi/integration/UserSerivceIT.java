package com.meadowhawk.homepi.integration;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class UserSerivceIT {

	@Test
	public void testUpdateUser(){
		String userId = "test-user";
		
		expect().
    statusCode(200).
    body(
      "email", equalTo("tester@homepi.com"),
      "firstName", equalTo("Tim"),
      "lastName", equalTo("Testerman"),
      "id", equalTo("1")).
    when().
    get("/service/user/profile/");
	}
	
	
	@Test
	public void testGetUser(){
		String userId = "test-user";
		
		expect().statusCode(200).
    body("email", equalTo("tester@homepi.com"),
        "givenName", equalTo("Test"),
        "familyName", equalTo("User"),
        "locale", equalTo(null),
        "id", equalTo("1")).when().
    	get("/service/user/profile/"+userId);
	}
}
