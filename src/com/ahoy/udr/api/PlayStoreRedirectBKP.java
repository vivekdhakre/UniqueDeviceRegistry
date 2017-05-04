package com.ahoy.udr.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahoy.udr.api.impl.ProcessPostback;
import com.ahoy.udr.dao.DeviceDao;
import com.ahoy.udr.dao.impl.DeviceDaoImpl;
import com.ahoy.udr.domain.DeviceDo;
import com.ahoy.udr.util.Secure;
import com.ahoy.udr.util.UtilConstants;



public class PlayStoreRedirectBKP extends HttpServlet {
	
	private static final long serialVersionUID = -6887456764626549460L;
	private static Logger logger = LoggerFactory.getLogger(PlayStoreRedirectBKP.class);

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
		
		String aff_sub = request.getParameter("aff_sub");
		String appName = request.getParameter("i");
		String clickId = request.getParameter("click_id");
		String apiName = request.getServletPath();
		PrintWriter out = response.getWriter();
		
		long adPkey, adplacePkey, propertyKey;
		adPkey = adplacePkey = propertyKey = 0;
		
		try {
			if(appName != null && !appName.trim().equals("")) {
				
				if (clickId != null && clickId.contains("AH")) {
					clickId = clickId.split("-AH")[0];
					adPkey = Long.valueOf(clickId.split("\\.")[2]);
					adplacePkey = Long.valueOf(clickId.split("\\.")[1]);
					propertyKey = Long.valueOf(clickId.split("\\.")[0]);

					referrer = "ai=" + 
							UtilConstants.REF_DELIMITER_HEX_STR_1 + adPkey +
							UtilConstants.REF_DELIMITER_HEX_STR_2 + adplacePkey + 
							UtilConstants.REF_DELIMITER_HEX_STR_3 + propertyKey +
							UtilConstants.REF_DELIMITER_HEX_STR_4 +
							"&aff_sub" + aff_sub +
							"&aff_sub2" + "aff_sub2" +
							"&aff_sub3" + "aff_sub3";
					
					referrer = URLEncoder.encode(referrer, "UTF-8");
				
				}
				
				if(!referrer.isEmpty()) {
					referrerToAppend = "&referrer=" + referrer;
				}
				
				
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
				
				apiResponse = UtilConstants.SUCCESS_MESSAGE;
				response.sendRedirect(redirectUrl);
			}
			else {
				apiResponse = "Identifier or Reference Token Missing or Empty";
			}
		}catch(Exception e) {
			apiResponse = "Internal Server Error!";
			logger.error("[PlayStoreRedirect][process] Exception : " + UtilConstants.getStackTrace(e));
		}finally {
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
