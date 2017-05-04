package com.ahoy.udr.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.util.UtilConstants;



public class PlayStoreRedirect extends HttpServlet {
	
	private static final long serialVersionUID = -6887456764626549460L;
	private static Logger logger = LoggerFactory.getLogger(PlayStoreRedirect.class);

	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		/* 
		 * U-Ad will hit this API after Ahoy App Ad Click
		http://uahoy.com/dr/redirect?i=<IDENTIFIER>(APP PACKAGE NAME)
		&aff_sub=<U_AD_RELATED_REFERRER>
		*/
		long startTime = System.currentTimeMillis();
		String apiResponse ="";
		String redirectUrl = "";
		String referrer = "";
		String referrerToAppend = "";
		InputStream in = null;
		
		String aff_sub = request.getParameter("aff_sub");
		String aff_sub2 = request.getParameter("aff_sub2");
		String aff_sub3 = request.getParameter("aff_sub3");
		String appName = request.getParameter("i");
		String apiName = request.getServletPath();
		PrintWriter out = response.getWriter();
		
		try {
			if(appName != null && !appName.trim().equals("")) {
				
				if(aff_sub != null && !aff_sub.isEmpty()) {
					referrer = referrer.isEmpty() ? referrer : referrer + "&";
					referrer += "aff_sub=" + aff_sub;
				}
				else if(aff_sub2 != null && !aff_sub2.isEmpty()) {
					referrer = referrer.isEmpty() ? referrer : referrer + "&";
					referrer += "aff_sub2=" + aff_sub2;
				}
				else if(aff_sub3 != null && !aff_sub3.isEmpty()) {
					referrer = referrer.isEmpty() ? referrer : referrer + "&";
					referrer += "aff_sub3=" + aff_sub3;
				}
				
				if(!referrer.isEmpty()) {
					referrer = URLEncoder.encode(referrer, "UTF-8");
					referrerToAppend = "&referrer=" + referrer;
				}
				
				// reading from file to determine redirect url
				Properties p = new Properties();
				in = this.getClass().getClassLoader()
						.getResourceAsStream("applicationpackage.properties");
				if(in == null) {
					logger.info("[PlayStoreRedirect][process] Unable To Read File 'applicationpackage.properties'");
				}
				else {
					p.load(in);
					Enumeration<?> e = p.propertyNames();
					while(e.hasMoreElements()) {
						String key = (String) e.nextElement();
						if(key.equalsIgnoreCase(appName)) {
							redirectUrl = p.getProperty(key) + referrerToAppend;
						}
					}
				}
			    
				/*
				if(appName.equalsIgnoreCase(UtilConstants.ANDROID_APP_PKG_UREWARD)) {
					redirectUrl = UtilConstants.PLAY_STORE_URL_UREWARD + referrerToAppend;
				}
				else if(appName.equalsIgnoreCase(UtilConstants.ANDROID_APP_PKG_CHD)) {
					redirectUrl = UtilConstants.PLAY_STORE_URL_CHD + referrerToAppend;
				}
				else if(appName.equalsIgnoreCase(UtilConstants.ANDROID_APP_PKG_UAHOY)) {
					redirectUrl = UtilConstants.PLAY_STORE_URL_UAHOY + referrerToAppend;
				}
				else if(appName.equalsIgnoreCase(UtilConstants.ANDROID_APP_PKG_WIFI_FINDER)) {
					redirectUrl = UtilConstants.PLAY_STORE_URL_WIFI_FINDER + referrerToAppend;
				}
				else if(appName.equalsIgnoreCase(UtilConstants.ANDROID_APP_NGO)) {
					redirectUrl = UtilConstants.PLAY_STORE_URL_NGO + referrerToAppend;
				}
				*/
				if(!redirectUrl.isEmpty()) {
	 				apiResponse = UtilConstants.SUCCESS_MESSAGE;
					response.sendRedirect(redirectUrl);
				}
				else {
					apiResponse = "Unable To Redirect! Possibly Due To Incorrect App Package Name";
				}
			}
			else {
				apiResponse = "Identifier or Reference Token Missing or Empty";
			}
		}catch(Exception e) {
			apiResponse = "Internal Server Error!";
			logger.error("[PlayStoreRedirect][process] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
			if (in != null) {
				try {
					in.close();
				}catch(IOException ioe) {
					logger.error("[PlayStoreRedirect][process] Exception : " + UtilConstants.getStackTrace(ioe));
				}
			}
			logger.info("[PlayStoreRedirect][process] API Response : " + apiResponse + 
					" | Total Time Taken : " + (System.currentTimeMillis()-startTime) + 
					" | App Name : " + appName +
					" | Api Name : " + apiName +
					" | Referrer : " + referrer + 
					" | Redirect URL : " + redirectUrl);
			out.println(apiResponse);
		}					
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
