package com.meadowhawk.homepi.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.meadowhawk.homepi.rs.JodaDateTimeSerializer;
import com.meadowhawk.homepi.util.StringUtil;

/**
 * Pi device data used for identifying the remote PI and how to connect to it if possible.
 * @author lee
 */
@Entity
@Table(name = "pi_profile")
@NamedNativeQueries(value={
		@NamedNativeQuery(name="PiProfile.findByPiSerialId", query="SELECT p.pi_id, p.update_time, p.create_time, p.pi_serial_id, p.name, p.ip_address, p.ssh_port_number, p.user_id, p.api_key FROM pi_profile p WHERE p.PI_SERIAL_ID = :piSerialId", resultClass=PiProfile.class),
		@NamedNativeQuery(name="PiProfile.deleteAppProfileAsso", query="DELETE FROM profile_managed_app WHERE app_id = :appId and pi_id = :piId", resultClass=PiProfile.class),
		} 
)
//,		@NamedNativeQuery(name="PiProfile.findByPiSerialIdApiToken", query="select count(*) from pi_profile p where p.pi_serial_id = :piSerialId and p.api_key = :apiKey",resultClass=Long.class)
//})
//@NamedQueries(@NamedQuery(name="PiProfile.findByPiSerialIdApiToken", query="select count(*) from pi_profile p where p.pi_serial_id = :piSerialId and p.apiKey = :apiKey"))
@JsonFilter("privateView")
public class PiProfile extends MaskableDataObject{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pi_id")
	private Long piId;
	
	@Column(name = "pi_serial_id", nullable=false)
	private String piSerialId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "ssh_port_number")
	private Integer sshPortNumber;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime = new DateTime();
	
	@Column(name = "update_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime = new DateTime();
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name="api_key")
	@Type(type="pg-uuid")
	private UUID apiKey;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	@JsonIgnore
	private HomePiUser user;
	
	
	@ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name="PROFILE_MANAGED_APP", 
              joinColumns={@JoinColumn(name="PI_ID")}, 
              inverseJoinColumns={@JoinColumn(name="APP_ID")})
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<ManagedApp> managedApps = new HashSet<ManagedApp>(0);

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
	public Long getPiId() {
		return piId;
	}
	public void setPiId(Long piId) {
		this.piId = piId;
	}
	public Integer getSshPortNumber() {
		return sshPortNumber;
	}
	public void setSshPortNumber(Integer sshPortNumber) {
		this.sshPortNumber = sshPortNumber;
	}
	@JsonSerialize(using=JodaDateTimeSerializer.class)
	public DateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(DateTime createTime) {
		this.createTime = createTime;
	}
	@JsonSerialize(using=JodaDateTimeSerializer.class)
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Set<ManagedApp> getManagedApps() {
		return managedApps;
	}
	public void setManagedApps(Set<ManagedApp> managedApps) {
		this.managedApps = managedApps;
	}
	@JsonIgnore
	public HomePiUser getUser() {
		return user;
	}
	
	public String getApiKey() {
		return (apiKey!=null)?apiKey.toString():null;
	}
	
	public void setApiKey(UUID apiKey) {
		this.apiKey = apiKey;
	}
	
	@Override
	public void setMaskedView(boolean maskView) {
		super.setMaskedView(maskView);
		for (ManagedApp ma : this.managedApps) {
			ma.setMaskedView(isMaskedView());
		}
	}
	
	/**
	 * Helper for getting an app by name when the profile already has the info populated.
	 * @param appName
	 * @return - null if no match
	 */
	@JsonIgnore
	public ManagedApp getManagedAppByName(String appName) {
		if(!StringUtil.isNullOrEmpty(appName)){
			for (ManagedApp ma : this.managedApps) {
				if(appName.equals(ma.getAppName())){
					return ma;
				}
			}
		}
		return null;
	}
}
