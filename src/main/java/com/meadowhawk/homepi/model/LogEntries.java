package com.meadowhawk.homepi.model;

import org.joda.time.DateTime;

public class LogEntries {
	private Integer piId;
	private String piSierialId;
	private DateTime timeStamp;
	private String message;
	
	public Integer getPiId() {
		return piId;
	}
	public void setPiId(Integer piId) {
		this.piId = piId;
	}
	public String getPiSierialId() {
		return piSierialId;
	}
	public void setPiSierialId(String piSierialId) {
		this.piSierialId = piSierialId;
	}
	public DateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
