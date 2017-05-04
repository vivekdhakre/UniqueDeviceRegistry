package com.ahoy.udr.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.util.UtilConstants;


public class ProcessPostbackBKP {
	
	private static Logger logger = LoggerFactory.getLogger(ProcessPostbackBKP.class);
	private static final int AD_PKEY_POSITION = 1;
	private static final int AD_PLACE_PKEY_POSITION = 2;
	private static final int PROPERTY_KEY_POSITION = 3;

	public static String start(String referrer, String deviceId, String appName) {

		long startTime = System.currentTimeMillis();
		String apiResponse ="";
		String ref = "";
		String[] params = new String[] {};
		
		long adPkey, adplacePkey, propertyKey;
		adPkey = adplacePkey = propertyKey = 0;
		
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
					
					String ai = "";
					for(int i=0; i<params.length; i++) {
						
						String p = params[i];  // parameter
						String v = "";			// value
						if(p.contains("=")) {
							v = p.substring(p.lastIndexOf("=")+1);
						}
						
						if(!v.isEmpty()) {
						
							if(p.contains("ai=")) {
								BigInteger bigInt = new BigInteger(v,10);
								ai = bigInt.toString(16);
								String[] aiArray = ai.split("\\D");
								int counter = 1;
								for(int j=0; j<aiArray.length; j++) {
									if(!aiArray[j].isEmpty()) {
										if(counter == AD_PKEY_POSITION) { // 1
											adPkey = Long.parseLong(aiArray[j]);
										}
										else if(counter == AD_PLACE_PKEY_POSITION) {  // 2
											adplacePkey = Long.parseLong(aiArray[j]);
										}
										else if(counter == PROPERTY_KEY_POSITION) {  // 3
											propertyKey = Long.parseLong(aiArray[j]);
										}
										counter++;
									}
								}
							}
							
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
					}
					
					if(ai.isEmpty() || adPkey == 0 || adplacePkey == 0 || propertyKey == 0) {
						apiResponse += " * NO AD DATA IN REFERRER ";
					}					
					
					if((aff_sub.isEmpty() || aff_sub.equalsIgnoreCase("NA")) && 
							(aff_sub2.isEmpty() || aff_sub2.equalsIgnoreCase("NA")) && 
							(aff_sub3.isEmpty() || aff_sub3.equalsIgnoreCase("NA"))) {
						apiResponse += " * NO TP UNQ ID IN REFERRER ";
					}
					
					if(apiResponse.isEmpty()) {
						// posting logic

						String str = "http://ads.uahoy.in/uad/postbackAds?"
								+ "click_id="+ URLEncoder.encode(propertyKey+"."+adplacePkey+"."+adPkey+"."+"-AH", "UTF-8")
								+ "&androidId=" + URLEncoder.encode(deviceId, "UTF-8")
								+ "&packageName=" + URLEncoder.encode(appName, "UTF-8");
						
						apiResponse = post(str, deviceId);
						
						apiResponse += "SUCCESS";
					}
				}
				else {
					apiResponse += " * IMPROPER REFERRER FORMAT ";
				}
			}

		}catch(Exception e) {
			apiResponse = "Internal Server Error! | Exception : " + UtilConstants.getStackTrace(e);
			logger.error("["+deviceId+"][ProcessPostback][start] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
			logger.info("["+deviceId+"][ProcessPostback][start] Device Id : " + deviceId +
					" | Has A Referrer : " + referrer +
					" | With Parameters { Ad Key : " + adPkey +
					", Ad Place Key : " + adplacePkey +
					", Property Key : " + propertyKey + " } " +
					" | App Package : " + appName +
					" | Total Time Taken : " + (System.currentTimeMillis()-startTime) +
					" | API Response : " + apiResponse);
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
