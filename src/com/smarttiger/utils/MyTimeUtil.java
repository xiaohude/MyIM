package com.smarttiger.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;


public class MyTimeUtil {

	/**
	 * 获取时间戳
	 * @return 毫秒级别的时间戳
	 */
	public static String getTimeMillis()
	{
		Long tsLong = System.currentTimeMillis()/1000;
        return tsLong.toString();
	}
	
	/**
	 * 获取当前时间
	 * @return 2015-04-07 21:13:04
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeNow()
	{
		//hh表示的是12小时制，HH才是24小时制
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     	
		String time = sDateFormat.format(new java.util.Date()); 
		//要想添加上星期：SimpleDateFormat("E HH:mm:ss");E 为周四，EEEE为星期四，
		//要想换语言：SimpleDateFormat("yyyy年MM月dd日 E",Locale.JAPAN);
		return time;
	}
	
	/**
	 * 获取当前时间
	 * @return 2015-04-07
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDateNow()
	{
		//hh表示的是12小时制，HH才是24小时制
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");     	
		String time = sDateFormat.format(new java.util.Date()); 
		return time;
	}
	
	/**
	 * 获取当前时间
	 * @param dateFormat 自定义时间格式，例如：yyyy年MM月dd日  HH:mm:ss
	 * hh表示的是12小时制，HH才是24小时制
	 * 要想添加上星期：SimpleDateFormat("E HH:mm:ss");E 为周四，EEEE为星期四，
	 * @return 自定义格式时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeNow(String dateFormat)
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat(dateFormat);     	
		String time = sDateFormat.format(new java.util.Date()); 
		return time;
	}
	

}
