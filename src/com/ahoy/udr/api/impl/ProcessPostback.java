package com.ahoy.udr.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.util.UtilConstants;


public class ProcessPostback {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessPostback.class);

	public static String start(String referrer, String deviceId, String appName) {

		long startTime = System.currentTimeMillis();
		String apiResponse ="";
		String url = "";
		String ref = "";
		String[] params = new String[] {};
		
		String aff_sub, aff_sub2, aff_sub3;
		aff_sub = aff_sub2 = aff_sub3 = "";
		
		try {
			// referrer decoding logic + hitting uad postback url
			
			/*
			 * Ad -> Target URL -> U-Ad -> Google Play Store -> Mobile App -> This API -> U-Ad
			 * 
			 * 'Target URL' For Third Party Ad Engines
			 * 
			 * http://ads.uahoy.in/api/v1/ad?
			 * &a=<AD_NAME>
			 * &i=<IDENTIFIER> (APPLICATION_PACKAGE_NAME for an mob app)
			 * &p=<PLATFORM>&an=<AD_NETWORK>&s=<SOURCE>
			 * &ai=<AD_ID_WHICH_INCLUDES_AD_PKEY_+_PROPERTY_PKEY_+_ADPLACE_PKEY>
			 * &andi={androidId}&ip={ip}
			 * &aff_sub={aff_sub}
			 * &aff_sub2={aff_sub2}
			 * &aff_sub3={aff_sub3}
			 * 
			 * Referrer will include four tokens, these will be URL encoded
			 * 
			 * -- example --
			 * 
			 * &ai=85382513741485967969433520568131072
			 * &aff_sub=102aa190a287ea4ae073412abd0932
			 * &aff_sub2=34983498
			 * &aff_sub3=NA
			 * 
			 * aff are third party dynamic/static variables
			 * 
			 */
			try {
				ref = URLDecoder.decode(referrer,"UTF-8");
			}catch(UnsupportedEncodingException e) {
				apiResponse = "Internal Server Error! | Unsupported Encoding Exception : " + UtilConstants.getStackTrace(e);
				logger.error("["+deviceId+"][ProcessPostback][start] Unsupported Encoding Exception : " + UtilConstants.getStackTrace(e));
			}
			
			if(!ref.isEmpty()) {
				
				if(ref.contains("&")) {
					params = ref.split("&");
					for(int i=0; i<params.length; i++) {
						String p = params[i];  // parameter
						String v = "";			// value
						if(p.contains("=")) {
							v = p.substring(p.lastIndexOf("=")+1);
						}
						
						if(!v.isEmpty()) {
							if(p.contains("aff_sub=")) {
								aff_sub = v;
							}
							else if(p.contains("aff_sub2=")) {
								aff_sub2 = v;
							}
							else if(p.contains("aff_sub3=")) {
								aff_sub3 = v;
							}
						}
						else {
							apiResponse += " * IMPROPER REFERRER FORMAT ";
						}
					}		

				}
				else {
					String v = ref.substring(ref.lastIndexOf("=")+1);
					if(ref.contains("aff_sub=")) {
						aff_sub = v;
					}
					if(ref.contains("aff_sub2=")) {
						aff_sub2 = v;
					}
					if(ref.contains("aff_sub3=")) {
						aff_sub3 = v;
					}
				}
				
				
				url = "http://ads.uahoy.in/bi/postback?"
						+ "click_id={aff_sub}"
						+ "&androidId={aff_sub2}"
						+ "&packageName={aff_sub3}";
				
				if((aff_sub.isEmpty() || aff_sub.equalsIgnoreCase("NA")) && 
						(aff_sub2.isEmpty() || aff_sub2.equalsIgnoreCase("NA")) && 
						(aff_sub3.isEmpty() || aff_sub3.equalsIgnoreCase("NA"))) {
					apiResponse += " * NO INFO IN REFERRER ";
				}
				else {
					url = url.replace("{aff_sub}", aff_sub);
					
					if(aff_sub2.isEmpty() || aff_sub2.equalsIgnoreCase("NA")) {
						url = url.replace("&androidId={aff_sub2}", "");
					}
					else {
						url = url.replace("{aff_sub2}", aff_sub2);
					}
					
					if(aff_sub3.isEmpty() || aff_sub3.equalsIgnoreCase("NA")) {
						url = url.replace("&packageName={aff_sub3}", "");
					}
					else {
						url = url.replace("{aff_sub3}", aff_sub3);
					}
					
				}
				
				if(apiResponse.isEmpty()) { 
					apiResponse = post(url, deviceId);
					if(apiResponse.trim().isEmpty() ) {
						apiResponse = UtilConstants.SUCCESS_MESSAGE;
					}
				}
			}
		}catch(Exception e) {
			apiResponse = "Internal Server Error! | Exception : " + UtilConstants.getStackTrace(e);
			logger.error("["+deviceId+"][ProcessPostback][start] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
			logger.info("["+deviceId+"][ProcessPostback][start]" +
					" API Time Taken : " + (System.currentTimeMillis()-startTime) +
					" | Device Id : " + deviceId +
					" | Has A Referrer : " + referrer +
					" | App Package : " + appName +
					" | Postback Url : " + url +
					" | API Response : " + apiResponse );
		}	
		
		return apiResponse;
	}
	
	public static String post(String address, String deviceId) {
	
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(address);
			conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(1000 * 2);
			conn.setReadTimeout(1000 * 5);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));     
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				result.append(inputLine);
			}
			br.close();
		} catch(IOException e) {	
			logger.error("["+deviceId+"][ProcessPostback][post] Exception Message : " + e.getMessage() + 
					" | Trace : " + UtilConstants.getStackTrace(e));	
		}
		
		return result.toString();
	}
	
}
