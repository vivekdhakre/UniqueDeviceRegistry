package com.ahoy.udr.util;

import java.net.URLEncoder;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;

public class Assertions {

	public static void main(String[] args) throws Exception {
/*		Long diff = System.currentTimeMillis()-100;
		
		System.out.println(diff);
		System.out.println(System.currentTimeMillis());
		
		Timestamp ts = new Timestamp(System.currentTimeMillis()-1000000000);
		
		
		System.out.println(ts.getTime());
		
		int i = Hours
		.hoursBetween(new DateTime(),new DateTime(ts.getTime()))
		.getHours();
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis() - 1000000L);
		System.out.println("Hours Diff : " + i);
		
		System.out.println(ts.getTime());
		System.out.println(System.currentTimeMillis());
		
		Duration du = new Duration(System.currentTimeMillis(), ts.getTime());
		
		System.out.println(du.getStandardHours());
		*/
		
		String referrer = URLEncoder.encode("", "UTF-8");
		System.out.println(referrer);
	}

}
