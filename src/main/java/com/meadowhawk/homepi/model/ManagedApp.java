package com.meadowhawk.homepi.model;

import org.joda.time.DateTime;

/**
 * ManagedApp is a deployed app on a PI which is managed by HomePi. The information here is used in managing and updating 
 * the represented application.
 * @author lee
 */
public class ManagedApp {

	private Long appId;
	private DateTime updateTime;
	private DateTime createTime;
	private Long versionNumber;
	private String AppName;
	private String fileName;
	private String deploymentPath;
	private Long ownerId;
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public DateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(DateTime lastUpdate) {
		updateTime = lastUpdate;
	}
	public Long getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDeploymentPath() {
		return deploymentPath;
	}
	public void setDeploymentPath(String deploymentPath) {
		this.deploymentPath = deploymentPath;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public DateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}	
	
}
