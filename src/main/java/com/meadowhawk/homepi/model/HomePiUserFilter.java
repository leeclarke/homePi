package com.meadowhawk.homepi.model;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

/**
 * Filters out private information from the user profile.
 * @author lee
 */
public class HomePiUserFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		
		if(bean instanceof HomePiUser){
			HomePiUser user = (HomePiUser)bean;
			Set<String> filterFields = new HashSet<String>();
			if(user.isPrivateVersion()){
			      filterFields.add("googleAuthToken");
			      filterFields.add("fullName");
			      filterFields.add("email");
			      filterFields.add("fullName");
			      filterFields.add("givenName");
			      filterFields.add("family	Name");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		} 
	}

}
