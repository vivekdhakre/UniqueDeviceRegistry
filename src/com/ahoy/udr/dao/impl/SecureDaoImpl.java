package com.ahoy.udr.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.dao.SecureDao;
import com.ahoy.udr.domain.SecureDo;
import com.ahoy.udr.util.HSession;
import com.ahoy.udr.util.UtilConstants;

public class SecureDaoImpl implements SecureDao{
	
	private static Logger logger = LoggerFactory.getLogger(SecureDaoImpl.class);
	
	private SessionFactory factory = HSession.createSessionFactory();

	@Override
	public boolean saveOrUpdate(SecureDo secureDo) {
		
		Session session = null;
		boolean flag = false;
		try{
			session = factory.openSession();
			
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(secureDo);
			transaction.commit();
			flag = true;
				
		}catch (Exception e) {
			logger.error("[SecureDaoImpl][save] Exception : "+e);
		}finally{
			session.close();
			
		}
		return flag;
		
		
	}

	@Override
	public SecureDo getByKey(String apiKey) {
		Session session = null;
		SecureDo secureDo = null;
		
		try{
			session = factory.openSession();
			
			List<SecureDo> secureDoList = session.createCriteria(SecureDo.class)
				.add(Restrictions.eq("apiKey", apiKey)).list();
			
			if(secureDoList!=null && secureDoList.size()>0){
				secureDo = secureDoList.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[SecureDaoImpl][getByKey] Exception : "+e);
		}finally{
			session.close();
		}
		
		return secureDo;
	}
	
	public SecureDo getByKeyApiname(String key, String apiName){
		Session session = null;
		SecureDo secureDo = null;
		
		try{
			session = factory.openSession();
			List<SecureDo> secureDoList = session.createCriteria(SecureDo.class)
				.add(Restrictions.eq("apiKey", key))
				.add(Restrictions.like("apiName","%"+apiName+",%"))
				.add(Restrictions.eq("status", UtilConstants.ENABLED)).list();
			
			if(secureDoList!=null && secureDoList.size()>0){
				secureDo = secureDoList.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[SecureDaoImpl][getByKey] Exception : "+e);
		}finally{
			session.close();
		}
		
		return secureDo;
	}
	
	@Override
	public SecureDo getByKeyEnable(String key) {
		Session session = null;
		SecureDo secureDo = null;
		
		try{
			session = factory.openSession();
			List<SecureDo> secureDoList = session.createCriteria(SecureDo.class)
				.add(Restrictions.eq("apiKey", key))
				.add(Restrictions.eq("status", UtilConstants.ENABLED)).list();
			
			if(secureDoList!=null && secureDoList.size()>0){
				secureDo = secureDoList.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[SecureDaoImpl][getByKey] Exception : " + e);
		}finally{
			session.close();
		}
		
		return secureDo;
	}

}
