package com.smarttiger.service;



import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.smarttiger.im.ChatActivity;
import com.smarttiger.im.ChatMsgEntity;
import com.smarttiger.utils.MyDebugUtil;
import com.smarttiger.utils.MyNotification;
import com.smarttiger.utils.MyVoiceVibrator;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PushServer2 extends Service{
	private static final String LogTAG = "PushServer2";
	private Context context;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		MyDebugUtil.show(LogTAG, "onCreate()");
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		MyDebugUtil.show(LogTAG, "onCreate()");
		super.onCreate();
		context = this;
		
		new Thread() {
			public void run() {
				int ii = 0;
				while(true)
				{
					MyDebugUtil.show(LogTAG, "onHandleIntent()--ii=" + ii++);
					
					String jsonS = "{type:'chat'," +
									"message:{time:'2015-04-22 10:14', name:'张三', q_id:'3', unread:"+ii+", message:'消息推送的单条消息"+ii+"'}," +
									"q_id:'3'}";
					if(ii%2==0)
						jsonS = "{type:'chat'," +
								"message:{time:'2015-04-22 19:23', name:'李四', q_id:'4', unread:"+ii+", message:'消息推送的单条消息"+ii+"'}," +
								"q_id:'4'}";
					getPushMessage(jsonS);
					
					
					try {
						TimeUnit.SECONDS.sleep(3);////停顿时间。
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}
	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		MyDebugUtil.show(LogTAG, "onStartCommand()"+"flags="+flags+"startId="+startId);
		
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MyDebugUtil.show(LogTAG, "onDestroy()");
		super.onDestroy();
	}
	
	//发送广播
	private void sendBroadcast()
	{
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("com.smarttiger.im.NewMessage");
		intent.putExtra("message", "收到新的消息");
		MyVoiceVibrator.play(context);
		sendBroadcast(intent);// 发送广播
	}

	
	//判断当前Activity是否显示在手机上。
	//记得添加权限：<uses-permission android:name="android.permission.GET_TASKS" />
	private boolean isAPPShow()
	{
		return isAPPShow(context.getPackageName());
	}
	/**
	 * 判断当前手机显示的界面是否为此Activity，或此应用getPackageName()
	 * @param className Activity全名。例：ChatActivity.class.getName()
	 * @return
	 */
	private boolean isAPPShow(String className)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
		MyDebugUtil.show(LogTAG, "当前显示的Activity为："+cn.getClassName());
		if (cn != null) {
		    if (cn.getClassName().contains(className)) {
		        return true;
		    }
		}
		return false;
	}
	
	private void getPushMessage(String jsonS)
	{
		try {
			JSONObject jsonObject = new JSONObject(jsonS);
			String type = jsonObject.getString("type");
			String message = jsonObject.getString("message");
			
			
			if("chat".equals(type))
			{
				String q_id = jsonObject.getString("q_id");
				
				if(isAPPShow())
				{
					Intent intent = new Intent();// 创建Intent对象
					intent.setAction("com.smarttiger.im.NewMessage");
					intent.putExtra("q_id", q_id);
					intent.putExtra("message", message);
					MyVoiceVibrator.play(context);
					sendBroadcast(intent);// 发送广播
				}
				else
				{
					JSONObject jsonM = new JSONObject(message);
					String time = jsonM.getString("time");
					String name = jsonM.getString("name");
					String messagetext = jsonM.getString("message");
					
					Intent contentIntent = new Intent(context, ChatActivity.class);
					contentIntent.putExtra("q_id", q_id);
					MyNotification myNotification = new MyNotification(context, name, messagetext, contentIntent);
					myNotification.show(Integer.parseInt(q_id));
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
