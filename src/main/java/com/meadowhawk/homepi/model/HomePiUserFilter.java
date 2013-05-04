package com.meadowhawk.homepi.model;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

public class HomePiUserFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider serProvider, BeanPropertyWriter arg3) throws Exception {
		
		if(bean instanceof HomePiUser){
			
		} else{
			//log error
			
		}

	}

}
