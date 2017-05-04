package com.ahoy.udr.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "device")
public class DeviceDo implements Serializable {
	
	private static final long serialVersionUID = 5440029943709898791L;

	@Id
	@GeneratedValue
	@Column(name = "device_pkey")
	private Long devicePkey;
	
	@Column(name="device_id")
	private String deviceId;
	
	@Column(name="platform")
	private String platform;
	
	@Column(name="app_name")
	private String appName;
	
	@Column(name="referrer")
	private String referrer;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;

	public Long getDevicePkey() {
		return devicePkey;
	}

	public void setDevicePkey(Long devicePkey) {
		this.devicePkey = devicePkey;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	

}
