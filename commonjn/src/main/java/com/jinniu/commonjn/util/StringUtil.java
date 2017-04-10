package com.jinniu.commonjn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class StringUtil {
	static Logger log = Logger.getLogger(StringUtil.class);
	
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd hh:mm:ss";
	public static final String YYYY_MM_DD_HH_mm_ss_fff = "yyyy-MM-dd hh:mm:ss.SSS";
	
	public static String format(String pattern, Date date){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static Date parse(String pattern, String dateStr){
		try {
			return new SimpleDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			log.error("parse date failed, str:" + dateStr, e);
		}
		return null;
	}
	
	public static boolean isBlank(String str){
		return str == null || "".equals(str.trim());
	}
	public static boolean isBlank(Integer str){
		return str == null;
	}
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	public static boolean isValid(String str, String pattern){
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static boolean isNotValid(String str, String pattern){
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return !m.matches();
	}
	
	public static boolean isNotValidDate(String str){
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(str);
		return !m.matches();
	}
	
}
