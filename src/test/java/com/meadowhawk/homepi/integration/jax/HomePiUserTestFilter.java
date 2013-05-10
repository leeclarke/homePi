package com.meadowhawk.homepi.integration.jax;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.HomePiUser;

public class HomePiUserTestFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		
		if(bean instanceof HomePiUser){
			HomePiUser user = (HomePiUser)bean;
			Set<String> filterFields = new HashSet<String>();
      filterFields.add("userName");
      filterFields.add("email");
      filterFields.add("locale");
      filterFields.add("picLink");
      filterFields.add("givenName");
      filterFields.add("familyName");
      filterFields.add("fullName");
			
			SimpleBeanPropertyFilter.filterOutAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		} 
	}

}
