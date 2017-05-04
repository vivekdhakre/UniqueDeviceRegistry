package com.ahoy.udr.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UtilConstants {

	public final static Boolean DEVICE_ENABLED = true;
	public final static Boolean DEVICE_DISABLED = false;
	
	public final static Boolean ENABLED = true;
	public final static Boolean DISABLED = false;
	
	public final static String ANDROID = "android";
	public final static String WINDOWS = "windows";
	public final static String APPLE = "apple";
	public final static String NOKIA_ASHA = "NokiaAsha";
	
	public final static String SUCCESS_MESSAGE = "SUCCESS";
	
	public final static String ANDROID_APP_PKG_UAHOY = "com.org.Ahoy";
	public final static String ANDROID_APP_PKG_CHD = "com.ahoy.couponsandhotdeals";
	public final static String ANDROID_APP_PKG_UREWARD = "com.ahoy.ureward";
	public final static String ANDROID_APP_PKG_WIFI_FINDER = "com.ahoy.wififinder";
	public final static String ANDROID_APP_NGO = "tushar.ngocommunity";
	
	
	public final static String REF_DELIMITER_HEX_STR_1 = "afdc";
	public final static String REF_DELIMITER_HEX_STR_2 = "ac";
	public final static String REF_DELIMITER_HEX_STR_3 = "bfe";
	public final static String REF_DELIMITER_HEX_STR_4 = "aeaff";
	
	public final static String WINDOWS_APP_PKG = "UAhoyWindows";
	public final static String APPLE_APP_PKG = "Ahoy.com.Ahoy";
	public final static String NOKIA_ASHA_APP_PKG = "com.org.ahoy.main.UAhoyNokiaAsha";
	
	
	public final static String PLAY_STORE_URL_UAHOY = "https://play.google.com/store/apps/details?id=com.org.Ahoy";
	public final static String PLAY_STORE_URL_CHD = "https://play.google.com/store/apps/details?id=com.ahoy.couponsandhotdeals";
	public final static String PLAY_STORE_URL_UREWARD = "https://play.google.com/store/apps/details?id=com.ahoy.ureward";
	public final static String PLAY_STORE_URL_WIFI_FINDER = "https://play.google.com/store/apps/details?id=com.ahoy.wififinder";
	public final static String PLAY_STORE_URL_NGO = "https://play.google.com/store/apps/details?id=tushar.ngocommunity";
	
	
	public static String getStackTrace(Throwable th) {
		StringWriter sw = new StringWriter();
		th.printStackTrace(new PrintWriter(sw));
		return sw.toString().length() > 600 
				? sw.toString().substring(0, 600).concat(" ...more")
				: sw.toString();
	}
	
}
