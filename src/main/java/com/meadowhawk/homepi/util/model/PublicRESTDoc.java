/**
 * 
 */
package com.meadowhawk.homepi.util.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Adds documentation to a REST service object for use in generating
 * @author lee
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface PublicRESTDoc {
	
	/**
	 * @return
	 */
	public String serviceName();
	
	/**
	 * @return
	 */
	public String description();
	
	//Sample Response.
	
}
