package com.meadowhawk.homepi.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

/**
 * @author lee
 */
public class HomePiAppException extends RuntimeException {
	private Status status;
	private String reason;
    private int errorCode = 0;
    private Object model;
	

	private static final long serialVersionUID = -6097407408194841416L;

	public HomePiAppException() {
		this(Status.BAD_REQUEST);
	}
	
	public HomePiAppException(Status status) {
		this.status = status;
	}

	public HomePiAppException(String msg) {
		this(Status.BAD_REQUEST, msg);
	}
	
	public HomePiAppException(Status status, String msg) {
		super(msg);
		this.status = status;
	}
	
	public HomePiAppException(Status status, String msg, String reason, Integer errCode) {
		this(status,msg,reason,errCode,null);
	}
	
	public HomePiAppException(Status status, String msg, String reason, Integer errCode, Object model) {
		super(msg);
		this.status = status;
		this.errorCode = errCode;
		this.reason = reason;
		this.model = model;
	}
	
	
	public HomePiAppException(Throwable msg) {
		this(Status.BAD_REQUEST,msg);
	}
	
	public HomePiAppException(Status status,Throwable msg) {
		super(msg);
		this.status = Status.BAD_REQUEST;
		this.reason = msg.getMessage();
	}

	public HomePiAppException(String msg, Throwable t) {
		this(Status.BAD_REQUEST, msg, t);
	}
	
	public HomePiAppException(Status status, String msg, Throwable t) {
		super(msg, t);
		this.status = status;
		this.reason = t.getMessage();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Map<String, Object> toResponse() {
		Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("status", this.status.toString());
		entity.put("message", this.getMessage());
		entity.put("errorCode", this.errorCode);
		entity.put("reason", this.reason);
		if(this.model != null){
			entity.put("model", this.model);
		}
		return entity;
	}

}
