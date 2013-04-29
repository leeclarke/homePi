package com.meadowhawk.homepi.util.service.model;

import java.util.List;

/**
 * @author lee
 */
public class AppConfig {

	private List<AppConfigEntry> config;

	public AppConfig(){
		
	}
	
	public AppConfig(List<AppConfigEntry> confEntries) {
		this.config = confEntries;
	}

	public List<AppConfigEntry> getConfig() {
		return config;
	}

	public void setConfig(List<AppConfigEntry> config) {
		this.config = config;
	}

	/**
	 * @param key
	 * @return
	 */
	public AppConfigEntry getByKey(String key) {
		for (AppConfigEntry entry : this.config) {
			if(entry.getKey().equalsIgnoreCase(key)){
				return entry;
			}
		}
		return null;
	}
	
}
