package com.meadowhawk.homepi.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

@Provider
@Produces("application/json")
public class JacksonConfigurator  implements ContextResolver<ObjectMapper>{
	private ObjectMapper mapper = new ObjectMapper();

  public JacksonConfigurator() {
      SerializationConfig serConfig = mapper.getSerializationConfig();
      
      SimpleFilterProvider fp = new SimpleFilterProvider();
      
      Set<String> filterFields = new HashSet<String>();
      filterFields.add("googleAuthToken");
      filterFields.add("full_name");
			fp.addFilter("privateUser", SimpleBeanPropertyFilter.serializeAllExcept(filterFields));
//      fp.addFilter("onlyAFilter", filter);
      mapper.setSerializationConfig(serConfig.withFilters(fp));
      mapper.setFilters(fp);
  }

  public ObjectMapper getContext(Class<?> arg0) {
      return mapper;
  }
}
