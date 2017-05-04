package com.ahoy.udr.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.api.impl.ProcessPostback;
import com.ahoy.udr.dao.DeviceDao;
import com.ahoy.udr.dao.impl.DeviceDaoImpl;
import com.ahoy.udr.domain.DeviceDo;
import com.ahoy.udr.util.Secure;
import com.ahoy.udr.util.UtilConstants;



public class RegisterDevice extends HttpServlet {
	
	private static final long serialVersionUID = -8065142036945220740L;
	private static Logger logger = LoggerFactory.getLogger(RegisterDevice.class);

	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		/* 
		 * Mobile App will hit this API at the time of App opening (after App installation)
		http://uahoy.com/dr/register?
		did=<DEVICE_ID>
		&platform=<PLATFORM>
		&src=<PACKAGE_NAME>
		&testmode=<TEST_MODE>
		&apikey=<API_KEY>
		&referrer=<U_AD_RELATED_REFERRER>
		*/
		long startTime = System.currentTimeMillis();
		long validKeyTimeTaken = 0;
		String apiResponse ="";
		
		String apiKey = request.getParameter("apikey");
		String testMode = request.getParameter("testmode");
		String apiName = request.getServletPath();
		
		String deviceId = request.getParameter("did");
		String platform = request.getParameter("platform");
		String appName = request.getParameter("src");
		String referrer = request.getParameter("referrer");
		PrintWriter out = response.getWriter();
		
		
		try {
			if(apiKey != null && !apiKey.trim().equals("")) {
				long startValidkeyTime = System.currentTimeMillis();
//				Secure secure = new Secure();
//				boolean valid = secure.validateApi(apiKey, apiName);
				boolean valid = true;
				validKeyTimeTaken = System.currentTimeMillis() - startValidkeyTime;
				if(valid) {
					String isValid = this.validateDeviceDetail(request);
					
					if(isValid.equalsIgnoreCase("valid")) {
						if(testMode == null || testMode.trim().equalsIgnoreCase("true")) {
							apiResponse = "Test Mode is : " + testMode;
						}
						else {
							logger.info("inside "+testMode);
							apiResponse = this.register(deviceId, referrer, platform, appName);
							
							if(referrer != null && 
									!referrer.trim().isEmpty() &&
									!referrer.trim().equalsIgnoreCase("NA") &&
									!apiResponse.isEmpty() &&
									apiResponse.equals(UtilConstants.SUCCESS_MESSAGE)) {
								apiResponse = ProcessPostback.start(referrer, deviceId, appName);	
							}
						}	
					}
					else {
						apiResponse = "Invalid Request Parameters! " + isValid;
					}
				}
				else {						
					apiResponse = "Invalid API Key";
				}
			}else {
				apiResponse = "API Key Parameter Missing or Empty";
			}
		}catch(Exception e) {
			apiResponse = "Internal Server Error!";
			logger.error("["+deviceId+"][RegisterDevice][process] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
			if(apiResponse.length() > 200) {
				apiResponse = apiResponse.substring(0, apiResponse.indexOf(")")+1);
			}
			logger.info("["+deviceId+"][RegisterDevice][process] API Response : " + apiResponse + 
					" | Total Time Taken : " + (System.currentTimeMillis()-startTime) + 
					" | ValidKeyCheckTimeTaken: " + validKeyTimeTaken + 
					" | Device Id : " + deviceId +
					" | Platform : " + platform +
					" | App Name : " + appName +
					" | Api Name : " + apiName +
					" | API Key : " + apiKey +
					" | Test Mode : " + testMode +
					" | Referrer : " + referrer);
		}	
		out.println(apiResponse);
	}
	
	private String validateDeviceDetail(HttpServletRequest request) {
		
		String errorMessage = "";
		
		boolean errFlag = false;
		
		if(request.getParameter("did") == null || request.getParameter("did").trim().isEmpty()) {
			errorMessage += " Device Id, ";
			errFlag = true;
		}
		
		if(request.getParameter("platform") == null || request.getParameter("platform").trim().isEmpty()) {
			errorMessage += " Platform, ";
			errFlag = true;
		}
		
		if(request.getParameter("src") == null || request.getParameter("src").trim().isEmpty()) {
			errorMessage += " App Name, ";
			errFlag = true;
		}
		
		if(errFlag) {
			errorMessage += " is/are blank or missing!";
		}
		else {
			errorMessage = "valid";
		}
		
		return errorMessage;
		
	}
	
	private String register(String deviceId, String referrer, String platform,
			String appName) {
		
		String resp = "";
		DeviceDao deviceDao = new DeviceDaoImpl();
		try {
			@SuppressWarnings("unused")
			BigInteger bi = new BigInteger(deviceId, 16);
		} catch(NumberFormatException nfe) {
			logger.info("["+deviceId+"][RegisterDevice][register] Device Id : " + deviceId +
					" | IMPROPER DEVICE ID.");
			logger.error("Exception : " + UtilConstants.getStackTrace(nfe));
			return "IMPROPER DEVICE ID";
		} catch(Exception ex) {
			logger.info("["+deviceId+"][RegisterDevice][register] Device Id : " + deviceId +
					" | IMPROPER DEVICE ID.");
			logger.error("Exception : " + UtilConstants.getStackTrace(ex));
			return "IMPROPER DEVICE ID";
		}
		
		/* Hex Value Check End */
		
		try {
			DeviceDo deviceDo = deviceDao.getByDeviceId(deviceId, platform, appName);
			
			if(deviceDo == null) {  // Register a new device
					logger.info("["+deviceId+"][RegisterDevice][register] Device Id : " + deviceId +
							" | DOES NOT Exist In Database.");				
					
					//Insert Device Information
					deviceDo = new DeviceDo();
					deviceDo.setDeviceId(deviceId);
					if(referrer != null && 
						!referrer.trim().isEmpty() &&
						!referrer.trim().equalsIgnoreCase("NA")) {
						deviceDo.setReferrer(referrer);
					}
					deviceDo.setPlatform(platform);
					deviceDo.setAppName(appName);
					deviceDo.setCreatedBy("ADMIN");
					deviceDo.setCreatedOn(new java.sql.Timestamp(new Date().getTime()));
					deviceDo.setUpdatedBy("ADMIN");
					deviceDo.setUpdatedOn(new java.sql.Timestamp(new Date().getTime()));
					deviceDao.save(deviceDo);
					resp = UtilConstants.SUCCESS_MESSAGE;	
			}
			else { // Case when - the customer is trying to re-install an app OR referrer is NA when in fact there should have been a token
				int hoursUpdateOn = Hours.hoursBetween(new DateTime(),new DateTime(deviceDo.getUpdatedOn()))
						.getHours();
				int hoursCreatedOn = Hours.hoursBetween(new DateTime(),new DateTime(deviceDo.getCreatedOn()))
						.getHours();
				if(Math.abs(hoursUpdateOn) <= 24 &&
						Math.abs(hoursCreatedOn) <= 100 &&
					(deviceDo.getReferrer() == null 
						|| deviceDo.getReferrer().trim().isEmpty()
						|| deviceDo.getReferrer().trim().equalsIgnoreCase("NA"))) {
					
					if(referrer != null && 
						!referrer.trim().isEmpty() &&
						!referrer.trim().equalsIgnoreCase("NA")) {
						deviceDo.setReferrer(referrer);
					}
					deviceDo.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
					deviceDao.saveOrUpdate(deviceDo);
					resp = UtilConstants.SUCCESS_MESSAGE;	
				} else {
					logger.info("["+deviceId+"][RegisterDevice][register] Device Id : " + deviceId +
							" | ALREADY EXISTS In Database.");
					resp = "ALREADY EXISTS";
				}
			}
		} catch (Exception ex) {
			resp = "Internal Server Error";
			logger.error("Exception : " + UtilConstants.getStackTrace(ex));
		}
		return resp;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
