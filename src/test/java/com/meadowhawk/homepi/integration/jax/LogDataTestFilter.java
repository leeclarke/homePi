package com.meadowhawk.homepi.integration.jax;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.model.ManagedApp;

public class LogDataTestFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov, BeanPropertyWriter writer) throws Exception {
		if (bean instanceof LogData) {
			Set<String> filterFields = new HashSet<String>();
			filterFields.add("logKey");
			filterFields.add("logMessage");
			filterFields.add("createTime");
			filterFields.add("logTypeId");

			SimpleBeanPropertyFilter.filterOutAllExcept(filterFields).serializeAsField(bean, jgen, prov, writer);
		}
	}

}
