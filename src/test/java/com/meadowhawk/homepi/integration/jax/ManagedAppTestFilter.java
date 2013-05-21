package com.meadowhawk.homepi.integration.jax;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.ManagedApp;

public class ManagedAppTestFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		
		if (bean instanceof ManagedApp) {
			Set<String> filterFields = new HashSet<String>();
			filterFields.add("versionNumber");
			filterFields.add("appName");
			filterFields.add("fileName");
			filterFields.add("deploymentPath");

			SimpleBeanPropertyFilter.filterOutAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		}
	}

}
