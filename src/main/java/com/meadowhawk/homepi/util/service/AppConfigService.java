package com.meadowhawk.homepi.util.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.util.dao.AppConfigEntryDao;
import com.meadowhawk.homepi.util.service.model.AppConfig;

@Component
public class AppConfigService {

	@Autowired
	private AppConfigEntryDao appConfigDao;
	
	private static AppConfig appConfig;
	
	@SuppressWarnings("static-access")
	public AppConfig getAppConfig(){
		if(appConfig == null){
			this.appConfig = appConfigDao.loadAppConfig();
		}
		return appConfig;
	}
	
	/**
	 * Scheduled task to refresh the config periodically
	 */
	@SuppressWarnings("static-access")
	public void refreshCache(){
		synchronized (appConfig) {
			this.appConfig = appConfigDao.loadAppConfig();
		}
	}
}
