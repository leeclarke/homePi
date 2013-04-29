package com.meadowhawk.homepi.util.model;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.meadowhawk.homepi.util.StringUtil;

/**
 * Model represents Google User Info retrieved from profile.
 * @author lee
 */
public class GoogleInfo {
	//TODO: Verify completeness of info.
	@JsonProperty("sub")
	private String id;
	private String email;
	private String email_verified;
	private String name;
	private String given_name;
	private String family_name;
	private String profile;
	private String picture;
	private String gender;
	private String birthdate;
	private String locale;
	@JsonIgnore
	private String auth_token;

	public GoogleInfo(){
		
	}
	
	public static GoogleInfo buildGoogleInfo(String jsonString) {
		if (!StringUtil.isNullOrEmpty(jsonString)) {
			ObjectMapper om = new ObjectMapper();
			try {
				return om.readValue(jsonString, GoogleInfo.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiven_name() {
		return given_name;
	}

	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getAuth_token() {
		return auth_token;
	}

	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}


	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getEmail_verified() {
		return email_verified;
	}

	public void setEmail_verified(String email_verified) {
		this.email_verified = email_verified;
	}
}
