package com.ahoy.udr.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CheckConcurrency {
	
	private static Logger logger = LoggerFactory.getLogger(CheckConcurrency.class);

	/*
	private static  List<String> pendingDeviceList;
	static {
		pendingDeviceList=Collections.synchronizedList(new ArrayList<String>());
	}
	public static boolean isDuplicateDevice(String deviceId){
		return pendingDeviceList.contains(deviceId);
	}
	public static  void addDeviceFlag(String deviceId){
		pendingDeviceList.add(deviceId);	
	}
	public static void removeDeviceFlag(String deviceId){
		pendingDeviceList.remove(deviceId);
	}
	
	public static List<String> getpendingDeviceList() {
		return pendingDeviceList;
	}
	*/
	
	// private static Long TIME_CHECK = 1000 * 5L;
	
	private static  Map<String,Long> pendingDeviceMap;
	
	static {
		//pendingDeviceMap = Collections.synchronizedMap(new HashMap<String,Long>());
		pendingDeviceMap = new ConcurrentHashMap<String,Long>(8, 0.9f, 1);
	}
	public static boolean isDuplicateDevice(String deviceId) {
		
		if(pendingDeviceMap.containsKey(deviceId)) {
			return true;
		}
		else {
			addDeviceFlag(deviceId);
			return false;
		}
	}
	public static  void addDeviceFlag(String deviceId) {
		if(pendingDeviceMap.size() > 50000) {
			logger.info("[CheckConcurrency][addDeviceFlag] Device Map Size Exceeded 500! Clearing Map");
			pendingDeviceMap.clear();
		}
		pendingDeviceMap.put(deviceId, System.currentTimeMillis());
	}
	public static void removeDeviceFlag(String deviceId){
		pendingDeviceMap.remove(deviceId);
	}
	
	public static Map<String,Long> getpendingDeviceMap() {
		return pendingDeviceMap;
	}
	
}
