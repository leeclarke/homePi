package com.meadowhawk.homepi.util.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicates that method expects the FIRST and SECOND parameters in the method signature to be the deviceId and apiKey, respectively. This annotation 
 * results in a validation of the api key and device serial id. Missing values will result in a filed authentication.
 * @author lee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiKeyRequired {

}
