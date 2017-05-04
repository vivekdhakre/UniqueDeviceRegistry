package com.ahoy.udr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.dao.SecureDao;
import com.ahoy.udr.dao.impl.SecureDaoImpl;
import com.ahoy.udr.domain.SecureDo;

public class Secure {
	private static Logger logger = LoggerFactory.getLogger(Secure.class);
	public boolean validateApi(String apiKey,  String src) {
		boolean flag = false;
		try {
			flag = saveCount(apiKey);
		}catch (Exception e) {
			logger.error("[Secure][validateApi] API Key : " + apiKey + " | Exception : " + e);
		}
	
		return flag;	
	}
	
	private boolean saveCount(String apiKey) {
		boolean flag = false;
		boolean saveToCentralFlag = false;
		int currentDay=0;
		int	storedDay=0;
		int	currentMonth=0;
		int	storedMonth=0;
		long todayCount = 1L;
		long yesterdayCount = 1L;
		long presentMonthCount = 1L;
		long lastMonthCount = 1L;
		try {
			SecureDao secureDao = new SecureDaoImpl();
			SecureDo secureDo = secureDao.getByKeyEnable(apiKey);
			
			if(secureDo !=null) { 
				secureDo.setTotalCount(secureDo.getTotalCount()+1);
				
				// we take Timestamp variable as we store it in database through secureDo
				Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
				
				// converting Timestamp to Date object and then formatting it with SimpleDateFormat class
				String currentDayAndMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(timeStamp.getTime()));
				
				// String returned is in the format date-month, spliting it through String's split method
				currentDay = Integer.parseInt(currentDayAndMonth.split("-")[0]);
				currentMonth = Integer.parseInt(currentDayAndMonth.split("-")[1]);
				//int currentYear = Integer.parseInt(currentDayAndMonth.split("-")[2]);
				
				String storedDayAndMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(secureDo.getUpdatedOn().getTime()));
				
				storedDay = Integer.parseInt(storedDayAndMonth.split("-")[0]);
				storedMonth = Integer.parseInt(storedDayAndMonth.split("-")[1]);
				//int storedYear = Integer.parseInt(storedDayAndMonth.split("-")[2]);
				
				todayCount = secureDo.getTodayCount();
				yesterdayCount = secureDo.getYesterdayCount();
				presentMonthCount = secureDo.getPresentMonthCount();
				lastMonthCount = secureDo.getLastMonthCount();
				
				if (currentDay != storedDay) {
					secureDo.setYesterdayCount(todayCount);
					secureDo.setTodayCount(1L);
					if(currentMonth != storedMonth) {
						secureDo.setLastMonthCount(presentMonthCount);
						secureDo.setPresentMonthCount(1L);
						
//						if(currentYear != storedYear) {
//						}
						
					}
					else {
						secureDo.setPresentMonthCount(presentMonthCount+1L);
					}
					// hitting the central secure api
					saveToCentralFlag = saveCountToCentralSecureProject(secureDo);
				}
				else {
					secureDo.setTodayCount(todayCount+1L);
					secureDo.setPresentMonthCount(presentMonthCount+1L);
				}
				secureDo.setUpdatedOn(timeStamp);
				secureDao.saveOrUpdate(secureDo);
				flag = true;
			}
			else {
				flag = false;
			}
			
		} 	catch (Exception e) {
			logger.error("[Secure][saveCount] API Key : " + apiKey + " | Exception : " + e);
		}	
		return flag;	
	}

	public boolean saveCountToCentralSecureProject(SecureDo secureDo) {
		boolean flag = false;
		String apiKey = "";
		String yesterdayCount = "";
		String presentMonthCount = "";
		String lastMonthCount = "";
		String totalCount = "";
		try {
			apiKey = secureDo.getApiKey();
			yesterdayCount = String.valueOf(secureDo.getYesterdayCount());
			presentMonthCount = String.valueOf(secureDo.getPresentMonthCount());
			lastMonthCount = String.valueOf(secureDo.getLastMonthCount());
			totalCount = String.valueOf(secureDo.getTotalCount());
			if(apiKey != null && !apiKey.trim().isEmpty()) {
				String url = "http://uahoy.com/secure/updatecounters?apikey=" + apiKey +
				"&yesterday=" + yesterdayCount + "&presentmonth=" + presentMonthCount + 
				"&lastmonth=" + lastMonthCount + "&total=" + totalCount;
				boolean postResponse = this.post(url);
				if(postResponse) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
		} catch (Exception e) {
			logger.error("[Secure][saveCountToCentralSecureProject] API Key : " + apiKey + " | Exception : " + e);
		} finally {
			logger.info("[Secure][saveCountToCentralSecureProject] API Key : " + apiKey + 
			" | Yesterday's Count : " + yesterdayCount + 
			" | Present Month's Count : " + presentMonthCount + 
			" | Last Month's Count : " + lastMonthCount + 
			" | Total Count : " + totalCount);
		}
		return flag;
	}
	
	private boolean post(String address) {
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		HttpURLConnection conn = null;
		try{
		 	URL url = new URL(address);
		
	        conn = (HttpURLConnection)url.openConnection();
	        conn.setConnectTimeout(10 * 1000);
	        conn.setReadTimeout(10 * 1000);
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	       
	        String inputLine;
	
	        while ((inputLine = br.readLine()) != null) {
	                result.append(inputLine);
	        }
	        br.close();
	        flag = true;
		}catch (IOException e) {
			try{
				logger.error("[Secure][post] Response Message : " +
			conn.getResponseMessage() +	" | Response code : " + conn.getResponseCode());
			}catch (Exception e2) {
				logger.error("[Secure][post] Exception : " + e2);
			}
			logger.error("[Secure][post] Exception : " + e);
			
		}
		return flag;
	}
}
