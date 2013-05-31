package com.meadowhawk.homepi.service.business;

public enum WEB_PARAMS_LOG_DATA{
	APP_NAME("app_name"),LOG_KEY("log_key"),LOG_TYPE("log_Type");
	
	private String queryPram;
	
	WEB_PARAMS_LOG_DATA(String value){
		this.queryPram = value;
	}
	
	public String webParam(){
		return this.queryPram;
	}
	
}