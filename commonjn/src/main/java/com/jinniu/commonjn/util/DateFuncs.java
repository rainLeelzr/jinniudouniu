package com.jinniu.commonjn.util;

import org.apache.commons.beanutils.ConversionException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * 
 * @author Jan
 *
 */
@SuppressWarnings({"static-access","unused"})
public class DateFuncs {

	//以下验证日期是否符合：2014-04-17格式
	private static final Pattern pattern1 = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
	//以下验证日期是否符合：2014-04-17 22:30:00格式
	private static final Pattern pattern2 = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");
	private static final Pattern pattern3 = Pattern.compile("^\\d{1,2}-[A-Za-z]{3}-\\d{4}$");
	private static final Pattern pattern4 = Pattern.compile("^\\d{1,2}\\s[A-Za-z]{3}\\s\\d{4}$");
	
	////19-Apr-2013 09:34:57
	private static final Pattern pattern5 = Pattern.compile("^^\\d{1,2}-[A-Za-z]{3}-\\d{4}\\s+\\d{2}:\\d{2}:\\d{2}$");
	
	public static final String format1="yyyy-MM-dd";
	public static final String format2="yyyy-MM-dd HH:mm:ss";
	public static final String format3="yyyy-MM-dd HH:mm";
	
	private static final SimpleDateFormat dateformat1 = new SimpleDateFormat(format1);
	private static final SimpleDateFormat dateformat2 = new SimpleDateFormat(format2);
	private static final SimpleDateFormat dateformat3 = new SimpleDateFormat(format3);
	
	private static final Map<String, SimpleDateFormat> formats = new HashMap<String, SimpleDateFormat>(); 
	private static final Map<String, String> monthDic = new HashMap<String, String>(); 
	static{
		formats.put(format1, dateformat1);
		formats.put(format2, dateformat2);
		formats.put(format3, dateformat3);
		
		//JANUARY/FEBRUARY/MARCH/APRIL/MAY/JUNE/JULY/AUGUST/SEPTEMBER/OCTOBER/NOVEMBER/DECEMBER 
		String[] months = new String[]{"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
		for(int i = 0; i < months.length; i++){
			String month = months[i];
			String numStr = i < 10 ? "0" + i : "" + i;
			monthDic.put(month, numStr);
			monthDic.put(month.substring(0, 3), numStr);
		}
	}
	
	public static String toString(Date date){
		return date != null ? dateformat2.format(date) : null;
	}

/*	public static String toString(String date, String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		
		return dateformat.format(valueOf(date));
	}*/
	public static String toString(Date date, String formatStr) {
		if(formatStr == null){
			return toString(date);
		}
		else{
			SimpleDateFormat df = formats.get(formatStr);
			if(df == null){
				df = new SimpleDateFormat(formatStr);
				formats.put(formatStr, df);
			}
				
			return df.format(date);
		}
	}

	public static Date valueOf(Object obj) {
		if (obj == null)
			return null;
		else if (obj instanceof Date)
			return (Date) obj;
		else
			return valueOf(obj.toString());
	}
	
	public static Date valueOf(String dateStr) {
		if(StrFuncs.isEmpty(dateStr)){
			return null;
		}
		try{
			if (pattern1.matcher(dateStr).find()) {
				//example:2013-12-29
				return dateformat1.parse(dateStr);
			}
			else if(pattern2.matcher(dateStr).find()) {
				//example:2013-03-20 12:32:01
				return dateformat2.parse(dateStr);
			}
			else if(pattern3.matcher(dateStr).find()){
				//example:1-FEB-2012
				String[] strArr = dateStr.split("-");
				String year = strArr[2];
				String month = strArr[1].toUpperCase();
				String date = strArr[0];
				String newDateStr = year + "-" + monthDic.get(month) + "-"
					+ (date.length() == 2 ? date : "0" + date);
				
				return dateformat1.parse(newDateStr);
			}
			else if(pattern4.matcher(dateStr).find()){
				//example:1 FEB 2012
				String[] strArr = dateStr.split(" ");
				String year = strArr[2];
				String month = strArr[1].toUpperCase();
				String date = strArr[0];
				String newDateStr = year + "-" + monthDic.get(month) + "-"
					+ (date.length() == 2 ? date : "0" + date);
				
				return dateformat1.parse(newDateStr);
			}
			else if(pattern5.matcher(dateStr).find()){
				//19-Apr-2013 09:34:57
				String[] strArr = dateStr.split(" ");
				String[] dateArr = strArr[0].split("-");
				String year = dateArr[2];
				String month = dateArr[1].toUpperCase();
				String date = dateArr[0];
				
				String newDateStr = year + "-" + monthDic.get(month) + "-"
					+ (date.length() == 2 ? date : "0" + date) + " " + strArr[1];
				
				return dateformat2.parse(newDateStr);
			}else{
				//暂时返回一个最长的
				return dateformat3.parse(dateStr);
			}
		}
		catch(Exception e){
			System.out.println("[DateFuncs] function valueOf execute error, unexcepted date str:]" + dateStr);
			//e.printStackTrace();
		}
		return null;
	}
	public static Date addMinute(Date date, int minuteCount) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minuteCount);
		return cal.getTime();
	}
	public static Date addDay(Date date,int dayCount){
		GregorianCalendar gc=new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, dayCount);
		return gc.getTime();
	}

	public static Date addHour(Date date, int hourCount) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hourCount);
		return cal.getTime();
	}
	public static Date addMonth(Date nowDate ,int month){
		 Calendar calendar = Calendar.getInstance(); 
		 if(null == nowDate){
			 Date   myDate=new   Date(); 
			 calendar.setTime(myDate);
		 }
		 else 
			 calendar.setTime(nowDate);
		 
		 calendar.add(calendar.MONTH,month);   
		return calendar.getTime();
	}
	public static Date addYear(Date date, int yearCount) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, yearCount);
		return cal.getTime();
	}
	
	public static Date addSecond(Date date, int secondCount) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.SECOND, secondCount);
		return cal.getTime();
	}
	
	/* @update zhuangzhonglong
	 * @describe 获取星期几（０为星期天，１为星期一）
	 * @time 2008-9-22 2:21:23
	 */
	public static Integer getWeekNo(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	/* @update zhuangzhonglong
	 * @describe 从分钟转为时间(hh:mm)
	 * @time 2008-9-22 12:26
	 */
	public static String minuteToTime(Integer minute) {
		if(minute == null) {
			return "";
		}
		return String.format("%02d:%02d",
			(minute - minute % 60) / 60, minute % 60);
	}
	/* @update zhuangzhonglong
	 * @describe 获取当前分钟数(0点算起)
	 * @time 2008-9-23
	 */
	public static Integer getMinute(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
	}
	/* @update zhuangzhonglong
	 * @describe 截掉Date的时间部分
	 * @time 2008-9-24 9:52
	 */
	public static Date trunc(Date date) {
		if(date == null) {
			return null;
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}	

	public static boolean beforeNow(String dateStr){
		try{
			Date date = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date.before(df.parse(dateStr));
		}catch(ParseException e){
			return false;  
	     }    
	}
	/**
	 * 传入的时间是否小于当前的时间
	 * @param dateStr
	 * @return
	 */
	public static boolean afterNow(String dateStr){
		try{  
			Date date = new Date();  
	        DateFormat df = DateFormat.getDateTimeInstance();   
	        return date.after(df.parse(dateStr));   
		}
		catch(ParseException e){  
			return false;  
	    }  
	}
	
	/**    
	 * @版本信息：2014-6-30 pj
	 * 
	 * @return   获取到月份    
	 */ 
	public static int getMonth(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM");
		return Integer.valueOf(df.format(date));
	}
	 /**    
	  * @版本信息：2014-6-30 pj
	 * 
	 * @return   获取到天     
	 */ 
	public static int getDay(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("dd");
		return Integer.valueOf(df.format(date));
	}
	 /**    
	  * @版本信息：2014-6-30 pj
	 * 
	 * @return   获取到天     
	 */ 
	public static int getYear(Date date){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		return Integer.valueOf(df.format(date));
	}
	/**    
	 * 得到本月的第一天    
	 * @return    
	 */     
	public static String getMonthFirstDay(Date date) {      
	    Calendar calendar = Calendar.getInstance();     
	    calendar.setTime(date);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));      
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(calendar.getTime());  
	}
	/**    
	 * 得到本月的最后一天    
	 *     
	 * @return    
	 */     
	public static String getMonthLastDay(Date date) {      
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMaximum(Calendar.DAY_OF_MONTH));   
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(calendar.getTime());   
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dateSeparateDate(Date beginDate ,Date endDate){
		
		long time=endDate.getTime() - beginDate.getTime();
		
		long dayCount=time / 1000 / 60 / 60 / 24;
		
		return (int)dayCount;
	}
	
	public static Date stringToDate(String time) {
        if (time != null && !"".equals(time)) {
            SimpleDateFormat formatter;
            time = time.trim();
            if ((time.indexOf("-") > -1)) {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
            }
            else if ((time.indexOf("年") > -1)) {
                formatter = new SimpleDateFormat("yyyy年MM月dd日");
            }
            else if ((time.indexOf(".") > -1)) {
                formatter = new SimpleDateFormat("yyyy.MM.dd");
            }
            else if ((time.indexOf("\\") > -1)) {
                formatter = new SimpleDateFormat("yyyy\\MM\\dd");
            }
            else if ((time.indexOf("/") > -1)) {
                formatter = new SimpleDateFormat("dd/MM/yyyy");
            }
            else {
                formatter = new SimpleDateFormat("yyyyMMdd");
            }
            ParsePosition pos = new ParsePosition(0);
            java.util.Date ctime = formatter.parse(time, pos);

            return ctime;
        }
        return null;
    }
	
	public static void main(String[] args) throws ParseException {
		String format2="yyyy-MM-dd HH:mm";
		SimpleDateFormat dateformat1 = new SimpleDateFormat(format2);
		
		System.out.println(dateformat1.parse("2014-05-10 10:30"));
	}
	
	public static String dateDiff(Date startTime, Date endTime) {
		String str = "";
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		// 获得两个时间的毫秒时间差异
		try {
			diff = endTime.getTime() - startTime.getTime();
			day = diff / nd;// 计算差多少天
			hour = diff % nd / nh + day * 24;// 计算差多少小时
			min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
			sec = diff % nd % nh % nm / ns;// 计算差多少秒
			// 输出结果
			// System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
			// + (min - day * 24 * 60) + "分钟" + sec + "秒。");
			// System.out.println("hour=" + hour + ",min=" + min);
			if (day > 0) {
				str += "<em>"+String.valueOf(day) + "</em>天";
			}
			if ((hour - day * 24) > 0) {
				str += "<em>"+String.valueOf((hour - day * 24)) + "</em>小时";
			}
			if (((min - day * 24 * 60)) > 0) {
				str += "<em>"+String.valueOf((min - day * 24 * 60)) + "</em>分钟";
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date strToDate(String str) {
	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
}
