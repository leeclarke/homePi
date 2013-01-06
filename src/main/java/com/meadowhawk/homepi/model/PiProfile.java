package com.meadowhawk.homepi.model;

/**
 * Pi device data used for identifying the remote PI and how to connect to it if possible.
 * @author lee
 */
public class PiProfile {
	private Integer piId;
	private String piSierialId;
	private String Name;
	private String ipAddress;
	private String sshPort;
	
	public String getPiSierialId() {
		return piSierialId;
	}
	public void setPiSierialId(String piSierialId) {
		this.piSierialId = piSierialId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getSshPort() {
		return sshPort;
	}
	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}
	
	
}
