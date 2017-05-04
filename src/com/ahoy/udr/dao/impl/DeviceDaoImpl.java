package com.ahoy.udr.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.dao.DeviceDao;
import com.ahoy.udr.domain.DeviceDo;
import com.ahoy.udr.util.HSession;
import com.ahoy.udr.util.UtilConstants;

public class DeviceDaoImpl implements DeviceDao {

	private SessionFactory factory = HSession.createSessionFactory();
	private static Logger logger = LoggerFactory.getLogger(DeviceDaoImpl.class);

	@Override
	public boolean saveOrUpdate(DeviceDo deviceDo) {
		
		Session session = null;
		boolean flag = false;
		
		try{
			
			session = factory.openSession();		
			Transaction transaction = session.beginTransaction();
			
			session.saveOrUpdate(deviceDo);		
			transaction.commit();
			flag = true;
				
		}catch (HibernateException e) {
			logger.error("[DeviceDaoImpl][saveOrUpdate] Exception : " + UtilConstants.getStackTrace(e));
		}finally{
			session.close();
		}
		return flag;
		
	}

	@Override
	public DeviceDo getByDeviceId(String deviceId, String platform, String appName) {
		Session session = null;
		DeviceDo deviceDo = null;
		List<DeviceDo> deviceDoList = null;

		try {
			session = factory.openSession();
			deviceDoList = session.createCriteria(DeviceDo.class)
					.add(Restrictions.eq("deviceId", deviceId))
					.add(Restrictions.eq("platform", platform))
					.add(Restrictions.eq("appName", appName))
					.list();
			
			if(deviceDoList != null && deviceDoList.size()>0) {
				deviceDo = deviceDoList.get(0);
			}
			
		}catch (HibernateException e) {
			logger.error("["+deviceId+"][DeviceDaoImpl][getByDeviceId] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
			session.close();
		}
		
		return deviceDo;	
	}
	
	@Override
	public long save(DeviceDo deviceDo) {
		
		Session session = null;
		long flag = 0;
		
		try{
			
			session = factory.openSession();		
			Transaction transaction = session.beginTransaction();
			
			flag = (Long)session.save(deviceDo);		
			transaction.commit();
				
		}catch (HibernateException e) {
			logger.error("[DeviceDaoImpl][saveOrUpdate] ERROR: " + UtilConstants.getStackTrace(e));
		}finally{
			session.close();
		}
		return flag;
		
	}
}
