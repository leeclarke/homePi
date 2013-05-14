package com.meadowhawk.homepi.model.filter;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;

import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.model.PiProfile;

/**
 * Filters out private information from the model objects.
 * @author lee
 */
public class HomePiPrivateFilter extends SimpleBeanPropertyFilter {

	public void serializeAsField(Object bean, JsonGenerator jGen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
		
		if(bean instanceof HomePiUser){
			HomePiUser user = (HomePiUser)bean;
			Set<String> filterFields = new HashSet<String>();
			if(user.isMaskedView()){
			      filterFields.add("googleAuthToken");
			      filterFields.add("fullName");
			      filterFields.add("email");
			      filterFields.add("givenName");
			      filterFields.add("familyName");
			      filterFields.add("piProfiles");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		} 
		else if(bean instanceof PiProfile){
			PiProfile profile = (PiProfile)bean;
			Set<String> filterFields = new HashSet<String>();
			if(profile.isMaskedView()){
			      filterFields.add("ipAddress");
			      filterFields.add("apiKey");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		}
		else if(bean instanceof ManagedApp){
			ManagedApp managedApp = (ManagedApp)bean;
			Set<String> filterFields = new HashSet<String>();
			if(managedApp.isMaskedView()){
			      filterFields.add("deploymentPath");
			}
			
			SimpleBeanPropertyFilter.serializeAllExcept(filterFields).serializeAsField(bean, jGen, provider, writer);
		}
	}

}
