package com.meadowhawk.homepi.model;

import org.joda.time.DateTime;

/**
 * Pi device data used for identifying the remote PI and how to connect to it if possible.
 * @author lee
 */
public class PiProfile {
	private Integer piId;
	private String piSerialId;
	private String name;
	private String ipAddress;
	private Integer sshPortNumber;
	private DateTime createTime;
	private DateTime updateTime;
	

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Integer getPiId() {
		return piId;
	}
	public void setPiId(Integer piId) {
		this.piId = piId;
	}
	public Integer getSshPortNumber() {
		return sshPortNumber;
	}
	public void setSshPortNumber(Integer sshPortNumber) {
		this.sshPortNumber = sshPortNumber;
	}
	public DateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}
	public DateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}
	public String getPiSerialId() {
		return piSerialId;
	}
	public void setPiSerialId(String piSerialId) {
		this.piSerialId = piSerialId;
	}
	
	
}
