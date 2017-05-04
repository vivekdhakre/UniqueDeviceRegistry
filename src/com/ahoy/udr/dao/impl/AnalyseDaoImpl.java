package com.ahoy.udr.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.dao.AnalyseDao;
import com.ahoy.udr.domain.DeviceDo;
import com.ahoy.udr.util.HSession;
import com.ahoy.udr.util.UtilConstants;

public class AnalyseDaoImpl implements AnalyseDao {

	private SessionFactory factory = HSession.createSessionFactory();
	private static Logger logger = LoggerFactory.getLogger(AnalyseDaoImpl.class);
	
	@Override
	public DeviceDo get(long analyticsPkey) {
		DeviceDo analyticsDo = null;
		
		Session session = null;
		try{
			
			session = factory.openSession();
			analyticsDo = (DeviceDo)session.get(DeviceDo.class,analyticsPkey);
			
		} catch (Exception ex) {
			logger.error("Exception : " + UtilConstants.getStackTrace(ex));
		} finally {
			session.close();
		}
		return analyticsDo;
	}
	
	@Override
	public boolean saveOrUpdate(DeviceDo analyticsDo) {
		
		Session session = null;
		boolean flag = false;
		try {
			
			session = factory.openSession();
			
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(analyticsDo);
			tx.commit();
			
			flag = true;
			
		} catch (Exception ex) {
			logger.error("Exception : " + UtilConstants.getStackTrace(ex));
		} finally {
			session.close();
		}
		return flag;

	}
	


	@Override
	@SuppressWarnings("unchecked")
	public List<DeviceDo> getAllDownloads(String appName, 
			String startDate,
			String endDate) {

		List<DeviceDo> listDeviceDo = null;
		Session session = null;
		try {
			session = factory.openSession();
			listDeviceDo = session.createCriteria(DeviceDo.class) 
	                .add(Restrictions.ge("createdOn", Timestamp.valueOf(startDate)))  
	                .add(Restrictions.lt("createdOn", Timestamp.valueOf(endDate)))
	                .add(Restrictions.eq("appName", appName))
	                .list();
 			
		} catch (Exception ex) {
			logger.error("Exception : " + UtilConstants.getStackTrace(ex));
		} finally {
			session.close();
		}
		return listDeviceDo;
	}

}






