package com.meadowhawk.homepi.util.model;

/**
 * Response Model object for unimplemented endPoints to return so the users know they done yet work. 
 * @author lee
 */
public class TODO {
	private String status = "TODO";
	private String message = "This feature has not been developed yet. Rest assured that our highly caffeinated code monkeys are hard at work on this and it will surely be ready soon.";
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
