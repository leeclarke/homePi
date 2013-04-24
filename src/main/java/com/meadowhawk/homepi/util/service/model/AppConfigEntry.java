package com.meadowhawk.homepi.util.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "APP_CONFIG")
public class AppConfigEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="config_id")
	private Long id;
	
	@Column(name="config_key")
	private String key;
	
	@Column(name="config_value")
	private String value;

	@Column(name = "update_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime = new DateTime();
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}

	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}
}
