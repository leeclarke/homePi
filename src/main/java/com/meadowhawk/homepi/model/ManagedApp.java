package com.meadowhawk.homepi.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * ManagedApp is a deployed app on a PI which is managed by HomePi. The information here is used in managing and updating 
 * the represented application.
 * @author lee
 */
@Entity
@Table(name = "MANAGED_APP")
@NamedNativeQuery(name="ManagedApp.findByPiSerialId", query="SELECT m.app_id, m.update_time, m.create_time, m.version_number, m.app_name, m.file_name, m.deployment_path, m.user_id" +
		"  FROM MANAGED_APP m, USER_PI_MANAGED_APP u, PI_PROFILE p " +
		"WHERE p.PI_SERIAL_ID = :piSerialId  AND p.PI_ID = u.PI_ID AND u.APP_ID = m.APP_ID", resultClass=ManagedApp.class)

public class ManagedApp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "app_id")
	private Long appId;

	
	@ManyToMany(mappedBy="managedApps", fetch=FetchType.EAGER)
	@JsonIgnore
	private Set<PiProfile> piProfiles = new HashSet<PiProfile>(0);
	
	@Column(name = "update_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime = new DateTime();
	
	@Column(name = "version_number", nullable=false)
	private Long versionNumber;
	
	@Column(name = "app_name", nullable=false)
	private String AppName;
	
	@Column(name = "file_name", nullable=false)
	private String fileName;
	
	@Column(name = "deployment_path", nullable=false)
	private String deploymentPath;
	
	@Column(name = "user_id", nullable=false)
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
	@JsonIgnore
	public Set<PiProfile> getPiProfiles() {
		return piProfiles;
	}
	public void setPiProfiles(Set<PiProfile> piProfiles) {
		this.piProfiles = piProfiles;
	}
	

	
}
