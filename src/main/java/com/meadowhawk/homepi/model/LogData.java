package com.meadowhawk.homepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "log_data")
public class LogData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Long logId;
	
	@Column(name = "pi_id", nullable=false)
	private Long piId;
	
	@Column(name = "user_id", nullable=false)
	private Long userId;
	
	@Column(name = "app_id", nullable=true)
	private Long appId;
	
	@Column(name = "log_type_id")
	private Long logTypeId = 1L;
	
	@Column(name = "pi_serial_id", nullable=false)
	private String piSierialId;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime;
	
	@Column(name = "log_key", nullable=false)
	private String logKey;
	
	@Column(name = "log_value", nullable=false)
	private String logMessage;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getPiId() {
		return piId;
	}

	public void setPiId(Long piId) {
		this.piId = piId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLogTypeId() {
		return logTypeId;
	}

	public void setLogTypeId(Long logType) {
		this.logTypeId = logType;
	}

	public String getPiSierialId() {
		return piSierialId;
	}

	public void setPiSierialId(String piSierialId) {
		this.piSierialId = piSierialId;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}

	public String getLogKey() {
		return logKey;
	}

	public void setLogKey(String logKey) {
		this.logKey = logKey;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
}
