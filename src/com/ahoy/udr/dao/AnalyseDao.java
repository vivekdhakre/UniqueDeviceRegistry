package com.ahoy.udr.dao;

import java.util.List;

import com.ahoy.udr.domain.DeviceDo;

public interface AnalyseDao {

	public DeviceDo get(long devicePkey);
	
	public boolean saveOrUpdate(DeviceDo deviceDo);

	public List<DeviceDo> getAllDownloads(String appName, 
			String startDate,
			String endDate);

}
