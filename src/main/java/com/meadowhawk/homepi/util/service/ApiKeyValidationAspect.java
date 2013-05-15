package com.meadowhawk.homepi.util.service;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import com.meadowhawk.homepi.model.MaskableDataObject;
import com.meadowhawk.homepi.service.business.DeviceManagementService;

/**
 * @author lee
 */
public class ApiKeyValidationAspect {
	private static Logger log = Logger.getLogger( ApiKeyValidationAspect.class );
	
	@Autowired
	DeviceManagementService deviceManagementService; 
	
	@Around("@annotation(ApiKeyRequired)")
	public Object checkForPrivateData(ProceedingJoinPoint pjp) throws Throwable{
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
}
