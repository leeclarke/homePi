package com.meadowhawk.homepi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
@Entity
@Table(name = "log_data")
@NamedNativeQueries(value={
		@NamedNativeQuery(name="LogData.findByLogKey", query="SELECT log_id, pi_id, user_id, app_id, log_type_id, create_time, log_key, log_value FROM log_data WHERE log_key LIKE :logKey", resultClass=LogData.class),
		@NamedNativeQuery(name="LogData.findByPiSerialId", query="SELECT l.log_id, l.pi_id, l.user_id, l.app_id, l.log_type_id, l.create_time, l.log_key, l.log_value FROM log_data l, pi_profile p WHERE l.pi_id = p.pi_id AND p.pi_serial_id = :piSerialId", resultClass=LogData.class),
		@NamedNativeQuery(name="LogData.findByLogType", query="SELECT l.log_id, l.pi_id, l.user_id, l.app_id, l.log_type_id, l.create_time, l.log_key, l.log_value FROM log_data l, log_type t WHERE t.log_type_id = l.log_type_id AND t.log_type_name = :logTypeName", resultClass=LogData.class)
		
}
		)
public class LogData extends MaskableDataObject implements Serializable {

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
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime = new DateTime();;
	
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
