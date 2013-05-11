package com.meadowhawk.homepi.rs;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import com.meadowhawk.homepi.model.filter.HomePiUserFilter;
import com.meadowhawk.homepi.model.filter.PiProfileFilter;

@Provider
@Produces("application/json")
public class JacksonConfigurator  implements ContextResolver<ObjectMapper>{
	private ObjectMapper mapper = new ObjectMapper();

  public JacksonConfigurator() {
      SerializationConfig serConfig = mapper.getSerializationConfig();
      
      SimpleFilterProvider fp = new SimpleFilterProvider();
      
      //Add filters here!  TODO: Extract to properties and inject
      fp.addFilter("privateUser", new HomePiUserFilter());
      fp.addFilter("privateProfile", new PiProfileFilter());

      mapper.setSerializationConfig(serConfig.withFilters(fp));
      mapper.setFilters(fp);
  }

  public ObjectMapper getContext(Class<?> arg0) {
      return mapper;
  }
}
