package com.meadowhawk.homepi.util.service;

import java.util.Arrays;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.MaskableDataObject;
import com.meadowhawk.homepi.service.business.DeviceManagementService;

/**
 * Responsible for enforcing the validity of the ApiKey on service calls. 
 * @author lee
 */
@Aspect
@Component
public class ApiKeyValidationAspect {
	private static Logger log = Logger.getLogger( ApiKeyValidationAspect.class );
	
	@Autowired
	DeviceManagementService deviceManagementService; 
	
	@Around("@annotation(ApiKeyRequired)")
	public Object checkForApiKey(ProceedingJoinPoint pjp) throws Throwable{
    Object[] args = pjp.getArgs();  
    Object obj = pjp.proceed();  
    
    if (args != null && log.isDebugEnabled()) {  
    	log.debug(">>>>>   AROUND Arguments : " + Arrays.toString(pjp.getArgs()));
    }  

    if (args != null && args.length >= 2)  
    {  
    	if(obj instanceof MaskableDataObject){
    		String piSerialId = (String) args[0];
				String apiKey = (String) args[1];
				
				if(deviceManagementService.validateApiKey(piSerialId, apiKey)){
	  			((MaskableDataObject)obj).setMaskedView(false);
	  		} 
    	}
          
    }  else{
    	log.error("MaskData annotation used with too few params. see Docs.");
    }
    
    return obj;
	}
	
	@Around("@annotation(ApiKeyRequiredException)")
	public Object checkForApiKeyReportException(ProceedingJoinPoint pjp) throws Throwable{
    Object[] args = pjp.getArgs();  
    Object obj = pjp.proceed();  
    
    if (args != null && log.isDebugEnabled()) {  
    	log.debug(">>>>>   AROUND Arguments : " + Arrays.toString(pjp.getArgs()));
    }  

    if (args != null && args.length >= 2)  
    {  
    	if(obj instanceof MaskableDataObject){
    		String piSerialId = (String) args[0];
				String apiKey = (String) args[1];
				
				if(deviceManagementService.validateApiKey(piSerialId, apiKey)){
	  			((MaskableDataObject)obj).setMaskedView(false);
	  		} else{
	  				throw new HomePiAppException(Status.FORBIDDEN);
	  		}
    	}
          
    }  else{
    	log.error("MaskData annotation used with too few params. see Docs.");
    }
    
    return obj;
	}
	
	@Before("@annotation(ApiKeyRequiredBeforeException)")
	public void verifyApiKeyFirstReportException(JoinPoint pjp)	throws Throwable {
		Object[] args = pjp.getArgs();

		if (args != null && log.isDebugEnabled()) {
			log.debug(">>>>>   BEFORE Arguments : " + Arrays.toString(pjp.getArgs()));
		}
		
		String piSerialId = (String) args[0];
		String apiKey = (String) args[1];
		if (!deviceManagementService.validateApiKey(piSerialId, apiKey)) {
			throw new HomePiAppException(Status.FORBIDDEN);			
		}
	}
}
