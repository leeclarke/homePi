package com.meadowhawk.homepi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * HomePi user.
 * @author lee
 */
@Entity
@Table(name = "users")

@NamedQueries(value={@NamedQuery(name="HomePiUser.findByEmail", query = "select u from HomePiUser u where u.email = :email"),
		@NamedQuery(name="HomePiUser.findByUserName", query = "select u from HomePiUser u where u.userName = :name"),
		@NamedQuery(name="HomePiUser.authToken", query="select count(*) from HomePiUser u where u.userName = :userName and u.googleAuthToken = :authToken")})
@JsonFilter("privateUser")
public class HomePiUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Transient
	public boolean privateVersion = false;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime = new DateTime();
	
	@Column(name = "update_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime;
	
	@Column(name = "user_name", unique=true)
	private String userName;
	
	@Column(name = "email", length=255, unique=true	)
	private String email;

	@Column(name = "gauth_token")
	private String googleAuthToken;
	
	@Column(name = "locale")
	private String locale;
	
	@Column(name = "pic_link")
	private String picLink;
	
	@Column(name = "given_name")
	private String givenName;
	
	@Column(name = "family_name")
	private String familyName;
	
	@Column(name = "full_name")
	private String fullName;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<PiProfile> piProfiles = new ArrayList<PiProfile>(0);
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<PiProfile> getPiProfiles() {
		return piProfiles;
	}
	public void setPiProfiles(List<PiProfile> piProfiles) {
		this.piProfiles = piProfiles;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getPicLink() {
		return picLink;
	}
	public void setPicLink(String picLink) {
		this.picLink = picLink;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getGoogleAuthToken() {
		return googleAuthToken;
	}
	public void setGoogleAuthToken(String googleAuthToken) {
		this.googleAuthToken = googleAuthToken;
	}

	public boolean isPrivateVersion() {
		return privateVersion;
	}
	public void setPrivateVersion(boolean privateVersion) {
		this.privateVersion = privateVersion;
	}
}
