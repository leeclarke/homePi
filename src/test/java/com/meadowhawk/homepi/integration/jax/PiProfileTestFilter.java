package com.meadowhawk.homepi.integration.jax;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.PiProfile;

/**
 * THis is used by Integration Testing to produce a specific POST JSON representation, not production code.
 * @author lee
 */
public class PiProfileTestFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		
		if (bean instanceof PiProfile) {
			Set<String> filterFields = new HashSet<String>();
			filterFields.add("ipAddress");
			filterFields.add("name");
			filterFields.add("sshPortNumber");
			filterFields.add("piSerialId");
			filterFields.add("apiKey");

			SimpleBeanPropertyFilter.filterOutAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		}
	}

}
