package com.meadowhawk.homepi.util.service;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


/**
 * Check for Authentication header in EndPoint requests.
 * @author lee
 */
@Aspect
@Component
public class CheckUserAuthToken{


	@Pointcut("execution(* com.meadowhawk.homepi.service.*.*(..))")
  public void getAuthToken() { }
	
	@Before("getAuthToken()")
	public void logJoinPoint(JoinPoint joinPoint){
		System.err.println(">>>>>   BEFORE Arguments : " + Arrays.toString(joinPoint.getArgs()));
		System.err.println(">>>>>   Method Name:" + joinPoint.getSignature().getName());
	}
	
	@After("getAuthToken()")
	public void checkRequestForAuthToken(JoinPoint joinPoint){
//		Object[] args = joinPoint.getArgs();
		System.err.println(">>>>>   AFTER Arguments : " + Arrays.toString(joinPoint.getArgs()));
		System.err.println(">>>>>   Method Name:" + joinPoint.getSignature().getName());
		
		
//TODO Test with this.
			//get auth token if logged in.
		//get userInfo if logged in.
		
	}


}
