package com.ahoy.udr.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.dao.SecureDao;
import com.ahoy.udr.dao.impl.SecureDaoImpl;
import com.ahoy.udr.domain.SecureDo;


public class SecureKeySave extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1292511583517261630L;
	private static Logger logger = LoggerFactory.getLogger(SecureKeySave.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		String apikey = request.getParameter("apikey");
		String status = request.getParameter("status");
		String src = request.getParameter("src");
		String resp="";
		try{
			if(apikey!=null && !apikey.trim().equals("") && status != null && !status.trim().equals("") 
					&& (status.trim().equalsIgnoreCase("true") || status.trim().equalsIgnoreCase("false")) && src!=null && !src.trim().equals("")){
				
				SecureDao secureDao = new SecureDaoImpl();				
				SecureDo secureDo = secureDao.getByKey(apikey);
				
				if(secureDo == null){
					secureDo = new SecureDo();
					secureDo.setApiKey(apikey);
					secureDo.setStatus(Boolean.valueOf(status));
					secureDo.setSrc(src);
					secureDo.setCreatedBy("ADMIN");
					secureDo.setCreatedOn(new Timestamp(new Date().getTime()));
					secureDo.setUpdatedBy("ADMIN");
					secureDo.setUpdatedOn(new Timestamp(new Date().getTime()));
					
					boolean flag = secureDao.saveOrUpdate(secureDo);
					resp = flag ? "success-save" : "Internal DB Error";
				}else{
					secureDo.setSrc(src);
					secureDo.setStatus(Boolean.valueOf(status));
					secureDo.setUpdatedOn(new Timestamp(new Date().getTime()));
					boolean flag = secureDao.saveOrUpdate(secureDo);
//					resp ="update:"+flag;
					resp = flag ? "success-update" : "Internal DB Error";
				}
			}
			else {
				resp="Invalid Parameter(s)";
			}
			
		} catch(Exception e) {
			resp="Internal Server Error";
			logger.error("[SecureKeySave][process] Exception: "+e);
		} finally {
			out.println(resp);
			logger.info("[SecureKeySave][process] apikey:["+apikey+"], status:"+status+", src:"+src+" | resp:"+resp);
		}
		
		
		
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}
}
