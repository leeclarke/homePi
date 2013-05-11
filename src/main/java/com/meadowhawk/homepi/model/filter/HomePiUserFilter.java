package com.meadowhawk.homepi.model.filter;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.HomePiUser;

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
			      filterFields.add("givenName");
			      filterFields.add("familyName");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		} 
	}

}
