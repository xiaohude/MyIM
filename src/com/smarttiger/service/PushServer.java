package com.smarttiger.service;

import java.util.concurrent.TimeUnit;
import com.smarttiger.utils.MyDebugUtil;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;


/**
 * 消息推送服务，继承自IntentService，所以是新开了一个线程。不是主线程，所以使用Toast的时候会出问题。并且执行完onHandleIntent会自动destroy。
 * */
public class PushServer extends IntentService {
	private static final String LogTAG = "PushServer";

	public PushServer() {
		super(LogTAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		MyDebugUtil.show(LogTAG, "onHandleIntent()");
		//下面三行代码是用来监测，网络连接是否可用。
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo()!=null;
		if(!isNetworkAvailable)
			return;
		
		
		new Thread() {
			public void run() {
				int ii = 0;
				while(true)
				{
					MyDebugUtil.show(LogTAG, "onHandleIntent()--ii=" + ii++);
					try {
						TimeUnit.SECONDS.sleep(2);////停顿时间。
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
		
		
		
		int i = 0;
		while(true)
		{
			MyDebugUtil.show(LogTAG, "onHandleIntent()--i=" + i++);
			try {
				TimeUnit.SECONDS.sleep(2);////停顿时间。
			} catch (InterruptedException e) {
			}
		}
		
		
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyDebugUtil.show(LogTAG, "onCreate()");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		MyDebugUtil.show(LogTAG, "onStart()");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		MyDebugUtil.show(LogTAG, "onStartCommand()"+"flags="+flags+"startId="+startId);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyDebugUtil.show(LogTAG, "onDestroy()");
		
	}
}
