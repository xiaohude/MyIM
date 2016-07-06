package com.smarttiger.utils;

import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 异步获取Bitmap图片类。
 * @param url : 图片下载地址
 * @param imageView : 图片下载成功后，自动显示在这个imageView上。
 * @param isSaveCache:是否将图片缓存到本地。
 * 
 * @author 朱小虎
 * @since 2014-12-01
 * */
public class DownloadBitmapTask  extends AsyncTask<Void, Void, String>{

	private static final String LogTAG = "DownloadBitmapTask";

	String url;
	ImageView imageView;
	Bitmap bitmap;
	HttpRequest httprequest ;
	String Psession;
	
	MySDcardFile mySDcardFile;
	Boolean isSaveCache = false;
	
	/**
	 * 异步获取Bitmap图片。
	 * @param url : 图片下载地址
	 * @param imageView : 图片下载成功后，自动显示在这个imageView上。
	 * */
	public DownloadBitmapTask(String url, String Psession, ImageView imageView) {
		// TODO Auto-generated constructor stub
		
		httprequest = new HttpRequest();
		this.url = url;
		this.Psession = Psession;
		this.imageView = imageView;
	}
	/**
	 * 异步获取Bitmap图片。
	 * @param url : 图片下载地址
	 * @param imageView : 图片下载成功后，自动显示在这个imageView上。
	 * @param isSaveCache:是否将图片缓存到本地。
	 * @param mySDcardFile:缓存所用到的mySDcardFile
	 * */
	public DownloadBitmapTask(String url, String Psession, ImageView imageView, Boolean isSaveCache, MySDcardFile mySDcardFile) {
		// TODO Auto-generated constructor stub
		
		httprequest = new HttpRequest();
		this.mySDcardFile = mySDcardFile;
		this.url = url;
		this.Psession = Psession;
		this.imageView = imageView;
		this.isSaveCache = isSaveCache;
	}
	@Override
	protected String doInBackground(Void... params) {
		
		if(isSaveCache)
		{
			String key = url.substring(url.lastIndexOf(".")+1).replace('/', '_');
			MyDebugUtil.show(LogTAG, "key==="+key);
			if(mySDcardFile.hasFile(key))
				try {
					bitmap = mySDcardFile.getBitmap(key);
				} catch (FileNotFoundException e) {}
			else
			{
				bitmap = httprequest.GetImageBitmap(url, Psession);
				mySDcardFile.saveBitMap(key, bitmap);
			}
		}
		else
			bitmap = httprequest.GetImageBitmap(url, Psession);
		
		return "OK";
	}

	@Override
	protected void onPostExecute(String result) {

	// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
	// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"

//		System.out.println("DownloadBitmapTask------bitmap======"+bitmap);
		if(bitmap != null && imageView != null)
			imageView.setImageBitmap(bitmap);
		
		
		super.onPostExecute(result);
	}
}
