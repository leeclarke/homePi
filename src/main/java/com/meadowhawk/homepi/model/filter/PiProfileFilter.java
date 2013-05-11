package com.meadowhawk.homepi.model.filter;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.PiProfile;

public class PiProfileFilter implements BeanPropertyFilter {
//TODO: This one isnt filtering when in a collection.
	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		if(bean instanceof PiProfile){
			PiProfile user = (PiProfile)bean;
			Set<String> filterFields = new HashSet<String>();
			if(user.isPrivateVersion()){
			      filterFields.add("piSerialId");
			      filterFields.add("ipAddress");
			      filterFields.add("apiKey");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jgen, provider, writer);
		} 
		
	}

}
