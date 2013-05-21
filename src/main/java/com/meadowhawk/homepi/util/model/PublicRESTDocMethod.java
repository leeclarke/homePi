package com.meadowhawk.homepi.util.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for providing information to populate the generated API Docs.
 * @author lee
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface PublicRESTDocMethod {

	/**
	 * User Friendly Name for an EndPoint.
	 * @return - name
	 */
	public String endPointName();
	
	/**
	 * Group name provides the UI with the ability to group the EndPoints into logical subsets to make the Docs easier to navigate.
	 * @return - group
	 */
	public String group() default "";
	
	/**
	 * Description of the EndPoint.
	 * @return
	 */
	public String description();
	
	/**
	 * Listing of full links which will trigger a call. (Assuming its a get, clicking on it should result in a response.)
	 * @return
	 */
	public String[] sampleLinks();
	
	/**
	 * Listing of possible Error Codes that can result. Best formatted as xxx - Explaination  ie.  403 - 
	 * @return
	 */
	public String[] errorCodes() default "[unspecified]";
	
	
}
