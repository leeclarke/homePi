package com.meadowhawk.homepi.util.dao;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.AbstractJpaDAO;
import com.meadowhawk.homepi.util.service.model.AppConfig;
import com.meadowhawk.homepi.util.service.model.AppConfigEntry;

@Component
public class AppConfigEntryDao extends AbstractJpaDAO< AppConfigEntry > {

	public AppConfigEntryDao() {
		setClazz(AppConfigEntry.class );
	}
	
	/**
	 * 
	 * @return
	 */
	public AppConfig loadAppConfig(){
		return new AppConfig(this.findAll());
	}
}
