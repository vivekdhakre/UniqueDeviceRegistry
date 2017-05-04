package com.ahoy.udr.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="secure_detail")
public class SecureDo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5880743970321864554L;

	@Id
	@GeneratedValue
	@Column(name = "secure_pkey")
	private Long secureApiPKey;
	
	@Column(name="api_key")
	private String apiKey;
	
	@Column(name="status")
	private boolean status;
	
	@Column(name="source")
	private String src;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;
	
	@Column(name="total_count")
	private long totalCount;
	
	@Column(name="today_count")
	private long todayCount;
	
	@Column(name="yesterday_count")
	private long yesterdayCount;
	
	@Column(name="present_month_count")
	private long presentMonthCount;
	
	@Column(name="last_month_count")
	private long lastMonthCount;

	public Long getSecureApiPKey() {
		return secureApiPKey;
	}

	public void setSecureApiPKey(Long secureApiPKey) {
		this.secureApiPKey = secureApiPKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
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


	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(long todayCount) {
		this.todayCount = todayCount;
	}

	public long getYesterdayCount() {
		return yesterdayCount;
	}

	public void setYesterdayCount(long yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	public long getPresentMonthCount() {
		return presentMonthCount;
	}

	public void setPresentMonthCount(long presentMonthCount) {
		this.presentMonthCount = presentMonthCount;
	}

	public long getLastMonthCount() {
		return lastMonthCount;
	}

	public void setLastMonthCount(long lastMonthCount) {
		this.lastMonthCount = lastMonthCount;
	}

}
