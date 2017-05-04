package com.ahoy.udr.dao;

import com.ahoy.udr.domain.SecureDo;

public interface SecureDao {
	public boolean saveOrUpdate(SecureDo secureDo);
	
	public SecureDo getByKey(String apiKey);
	
	public SecureDo getByKeyEnable(String key);
	
	public SecureDo getByKeyApiname(String key, String apiName);
}
