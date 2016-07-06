package com.smarttiger.utils;

import java.io.IOException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
/**
 * 调试工具类
 * @author xiaohu
 *
 */
public class MyDebugUtil {

	/**
	 * debug信息输出方式:
	 * 0:不输出
	 * 1:System.out.println()
	 * 2:Log.d()
	 * 3:showOutSDcard(),将debug信息保存到手机sd卡
	 * 4:Toast.makeText(),这个需要context，在没有context的地方默认用System.out.println()
	 */
	public static int isShowDebugMessage = 1;
	private static MySDcardFile mySDcardFile = null;
	
	public static void show(String tag, String message)
	{
		if(isShowDebugMessage == 0)
			return;
		else if(isShowDebugMessage == 1)
			showSystem(tag+"----"+message);
		else if(isShowDebugMessage == 2)
			showLogD(tag, message);
		else if(isShowDebugMessage == 3)
			showOutSDcard(tag, message);
		else
			showSystem(tag+"----"+message);
	}
	
	public static void show(Context context, String tag, String message)
	{
		if(isShowDebugMessage < 4)
			show(tag, message);
		else
			showToast(context, tag+"--"+message);
	}
	
	public static void showSystem(String message)
	{
		System.out.println(message);
	}
	
	public static void showLogD(String tag, String message)
	{
		Log.d(tag, message);
	}
	
	public static void showOutSDcard(String tag, String message)
	{
		if(mySDcardFile == null)
			mySDcardFile = new MySDcardFile("MyLog");
		String logS = "";
		String fileName = MyTimeUtil.getDateNow();
		try {
			if(mySDcardFile.hasFile(fileName))
				logS = mySDcardFile.readSDFile(fileName);
			logS = logS + MyTimeUtil.getTimeNow("HH:mm:ss") + "----";
			mySDcardFile.writeSDFile(fileName, logS + tag+"----"+message+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 通过Toast显示消息
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		
		//第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶 
		//第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移 
		//第三个参数：同的第二个参数道理一样 
		//如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示 
		//toast.setGravity(Gravity.TOP|Gravity.CENTER, -50, 100); 
	}
	
	/**
	 * 更改debug信息输出方式。
	 */
	public static void changeShowMessageWay(Context context)
	{
		MyDebugUtil.isShowDebugMessage = (MyDebugUtil.isShowDebugMessage + 2)% 5;
		MyDebugUtil.show(context, "设置调试格式", "调试信息输出格式："+MyDebugUtil.isShowDebugMessage);
		ShowMessageUtil.showToast(context, "调试信息输出格式："+MyDebugUtil.isShowDebugMessage);
	}
	
	
}
