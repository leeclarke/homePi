package com.meadowhawk.homepi.util.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the data object being returned by the service should be checked to see if it needs Masking. Expected Data object 
 * would also need to be a subclass of <code>MaskableDataObject</code>.
 * 
 * NOTE:  Currently it is EXPECTED that the first two parameters of the annotated method will be (String userName, String authToken,...) if not the enforcement will always be private.
 * @author lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MaskData {

}
