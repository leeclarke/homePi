package com.meadowhawk.homepi.util.service;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.MaskableDataObject;
import com.meadowhawk.homepi.service.business.HomePiUserService;


/**
 * Works along with the @MaskData annotation to filter returned data based on user ownership. Note that field objects are not enforced, it 
 * is up to the Parent object to override the setMaskedView() method and enforce.
 * @author lee
 */
@Aspect
@Component
public class MaskDataAspect{
	private static Logger log = Logger.getLogger( MaskDataAspect.class );
	
	@Autowired
	HomePiUserService userService;

	@Pointcut("execution(* com.meadowhawk.homepi.service.business.*.*(..))")
  public void getAuthToken() { }
	
//	@Before("getAuthToken()")
//	public void logJoinPoint(JoinPoint joinPoint){
//		System.err.println(">>>>>   BEFORE Arguments : " + Arrays.toString(joinPoint.getArgs()));
//		System.err.println(">>>>>   Method Name:" + joinPoint.getSignature().getName());
//	}
//	
	
//	@After("getAuthToken() && @annotation(MaskData)")
//	public void checkRequestForPrivateData(JoinPoint joinPoint){
////		Object[] args = joinPoint.getArgs();
//		System.err.println(">>>>>   AFTER Arguments : " + Arrays.toString(joinPoint.getArgs()));
//		System.err.println(">>>>>   Method Name:" + joinPoint.getSignature().getName());
//		
////		joinPoint.
//		
////		if(!userService.verifyUserToken(userName, authToken)){
////			profile.setMaskedView(true);
////		}
//		
////TODO Test with this.
//			//get auth token if logged in.
//		//get userInfo if logged in.
//		
//	}

	@Around("@annotation(MaskData)")
	public Object checkForPrivateData(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("Handling memoize");  
    Object[] args = pjp.getArgs();  
    Object obj = pjp.proceed();  
    
    if (args != null && log.isDebugEnabled()) {  
    	log.debug(">>>>>   AFTER Arguments : " + Arrays.toString(pjp.getArgs()));
    }  
    
    if (args != null && args.length >= 2)  
    {  
    	if(obj instanceof MaskableDataObject){
    		String userName = (String) args[0];
				String authToken = (String) args[1];
				if(userService.verifyUserToken(userName, authToken)){
	  			((MaskableDataObject)obj).setMaskedView(false);
	  		}
    	}
          
    }  else{
    	log.error("MaskData annotation used with too few params. see Docs.");
    }
    
    return obj;
	}
	
}
