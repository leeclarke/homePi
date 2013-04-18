package com.meadowhawk.homepi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * 
 * @author lee
 */
@Entity
@Table(name = "users")
@NamedQuery(name="HomePiUser.findByUserName", query = "select u from HomePiUser u where u.userName = :name")
public class HomePiUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
	private Long userId;
	
	@Column(name = "create_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createTime;
	
	@Column(name = "update_time")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime;
	
	@Column(name = "user_name")
	private String userName;
	
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
	
	
}
