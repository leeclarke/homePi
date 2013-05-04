package com.meadowhawk.homepi.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RSApplication extends Application {
	@Override
  public Set<Class<?>> getClasses() {
      Set<Class<?>> classes = new HashSet<Class<?>>();
      // your classes here
      classes.add(JacksonConfigurator.class);
      return classes;
   }
}
