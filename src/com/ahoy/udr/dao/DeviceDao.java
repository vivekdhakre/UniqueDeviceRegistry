package com.ahoy.udr.dao;

import com.ahoy.udr.domain.DeviceDo;

public interface DeviceDao {
	
	public boolean saveOrUpdate(DeviceDo deviceDo);
	public long save(DeviceDo deviceDo);
	public DeviceDo getByDeviceId(String deviceId, String platform, String appName);
}
