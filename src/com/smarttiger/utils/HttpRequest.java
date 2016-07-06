package com.smarttiger.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;



/**
 * 如果改动Psession参数传送。记得有些类为调用HttpRequest。还是原来的参数传递机制，需要一同更改，
 * 比如：humanchooselist，deptchoose等类。。。。。
 * */

public class HttpRequest {
	
	private static final String LogTAG = "HttpRequest";
	private static String phpsessidName = "GOASESSID";


	/**
	 * 获取服务器端的列表
	 * 第一个参数为全名URL，第二个参数为Cookie所需的参数，第三个参数为模块名
	 * */
	public String getDataList(String path, String Psession, String model) {
		Log.d(LogTAG, "getDataList---path=="+path+"--Psession=="+Psession);
		
		String result = "";
		HttpPost ContentRequest = new HttpPost(path);
		ContentRequest.setHeader("Cookie",phpsessidName + "="+ Psession);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("A", "loadList"));
		if(model.equals("diary_personal")){
			params.add(new BasicNameValuePair("FTYPE", "personal"));
		}
		else if(model.equals("diary_share")){
			params.add(new BasicNameValuePair("FTYPE", "share"));
		}
		else if(model.equals("diary_others")){
			params.add(new BasicNameValuePair("FTYPE", "other"));
//			params.add(new BasicNameValuePair("Q_ID", "0"));//可以直接添加到url中，所以这里注销了。
		}
		else
		{
			params.add(new BasicNameValuePair("FTYPE", model));
		}
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			ContentRequest.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(ContentRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpresult = EntityUtils.toString(httpResponse.getEntity());
				StringBuffer original = new StringBuffer(httpresult);
				result = original.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(LogTAG, "getDataList---result=="+result);
		
		if(result.startsWith("<!DOCTYPE html")||result.startsWith("<html"))
		{
			System.out.println("用户已经掉线，请重新登录！！！000000000");
			return "IsLost";
		}
		else if(result == "")
		{
			System.out.println("用户无网络，请检查网络连接");
//
//			Application context = new Application();
//			context.startActivity(new Intent(context,login.class));
			return "\"NODATA\"";
		}
		
		return result;
	}

	
	/**
	 * 简单的根据第一个参数所给的url发送请求，返回服务器返回的值。
	 * 第一个参数为完整的url，包括具体的id。第二个参数为Cookie所需的参数。
	 * */
	public String putRequest(String url, String Psession)
	{
		String result = "";

		try {
			// 1.得到HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			// 2.实例化一个HttpPut对象
			HttpPut httpPut = new HttpPut(url);
			
			// 3.添加Cookie，用来表明是哪个用户。
			httpPut.setHeader("Cookie", phpsessidName + "=" + Psession);
			
//			//可以用pairs添加参数。
//			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//			pairs.add(new BasicNameValuePair("USERNAME", "admin"));
//			pairs.add(new BasicNameValuePair("PASSWORD", ""));
//			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
					
			// 4.执行httpPost提交，得到返回响应。  
			HttpResponse response=httpClient.execute(httpPut);
			// 5.判断是否请求成功。
			if(response.getStatusLine().getStatusCode()==200)
			{
                // 6.取出应答字符串-----------这里的应答字符串result就是最终获取的信息，服务器发送来的。可以是一个网页，也可以是一个OK。
                HttpEntity entity=response.getEntity();
                result=EntityUtils.toString(entity, HTTP.UTF_8);
			}			
			System.out.println("HttpRequest--putRequest()--result=-=" + result );		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return result;
	}
	
	
	/**
	 * 获取数值,根据url和键值list,获取数值list
	 * @param url 地址全名
	 * @param Psession
	 * @param list Json的key值
	 * @return 返回Json中相对应key的value
	 */
	public List<String> getStringList(String url, String Psession, List<String> list)
	{
		List<String> resultList = new ArrayList<String>();
		String result = putRequest(url, Psession);
		System.out.println("HttpRequest---getMessageNum--result===" + result);
		try {
			JSONObject jsonobjectFather= new JSONObject(result);
			System.out.println("HttpRequest---getMessageNum--jsonobjectFather=="  + jsonobjectFather);
			for(int i = 0; i < list.size(); i ++)
			{
				resultList.add( jsonobjectFather.getString(list.get(i).toString()) );
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 检测是否需要更新。
	 * */
	public List<String> isNewVersions(String url)
	{
		String Psession = null;//检测版本更新不需要Psession因此直接为空就行。
		String result = null;
		List<String> resultList = new ArrayList<String>();		
		result = putRequest(url, Psession);
				
		try {
			System.out.println("HttpRequest---isNewVersions--result=="  + result);
			JSONObject jsonobjectFather= new JSONObject(result);	
			result = jsonobjectFather.get("code").toString();	
			
			resultList.add( jsonobjectFather.get("code").toString() );
			resultList.add( jsonobjectFather.get("url").toString() );
					
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	
	/**
	 * 根据图片的url通过数据流获取图片的Bitmap
	 * */
	public Bitmap GetImageBitmap(String picurl, String Psession) {

		Bitmap bitmap = null;
		
		try {
			URL url = new URL(picurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestProperty("Cookie", phpsessidName + "=" + Psession);// 设置通用的请求属性 		    
            conn.setDoInput(true);// 打开输入流
			conn.setRequestMethod("GET"); // 设置本次Http请求使用的方法
			conn.setConnectTimeout(5 * 1000);//// 设置连接网络的超时时间
			conn.connect();
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			byte[] dataImage = bos.toByteArray();
			bos.close();
			in.close();
			bitmap = BitmapFactory.decodeByteArray(dataImage, 0,
					dataImage.length);
			
			return bitmap;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 更新数据列表
	 * 第一个参数为全名URL，第二个参数为Cookie所需的参数，第三个参数为模块，第四个参数为当前列表最新数据的id
	 * */
	public String getDataRefresh(String path, String Psession, String model,
			String latest_id) {
		String result = "";
		HttpPost ContentRequest = new HttpPost(path);
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		ContentRequest.addHeader("Cookie", phpsessidName + "=" + Psession);
		param.add(new BasicNameValuePair("A", "getNew"));
		param.add(new BasicNameValuePair("LATEST_ID", latest_id));
		if(model.equals("diary_personal")){
			param.add(new BasicNameValuePair("FTYPE", "personal"));
		}
		else if(model.startsWith("diary_share")){
			String count_all=model.substring(11);
			param.add(new BasicNameValuePair("FTYPE", "share"));
			param.add(new BasicNameValuePair("FTYPE", count_all));
		}
		else if(model.equals("diary_others")){
			param.add(new BasicNameValuePair("FTYPE", "other"));
		}
		else
		{
			param.add(new BasicNameValuePair("FTYPE", model));
		}
		try {
			// 用Http Post请求发送参数给服务端，获取JSON格式的返回值
			HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
			ContentRequest.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(ContentRequest);
			StringBuilder str = new StringBuilder();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// String
				// result=EntityUtils.toString(httpResponse.getEntity());
				BufferedReader buffer = null;
				buffer = new BufferedReader(new InputStreamReader(httpResponse
						.getEntity().getContent()));
				for (String s = buffer.readLine(); s != null; s = buffer
						.readLine()) {
					str.append(s);
				}
				result = str.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取更多数据
	 * 第一个参数为全名URL，第二个参数为Cookie所需的参数，第三个参数为模块，第四个参数为当前列表数据个数
	 * */
	public String getDataMore(String path, String Psession, String model,
			String curr_items) {
		String result="";
		HttpPost ContentRequest = new HttpPost(path);
		ContentRequest.setHeader("Cookie",phpsessidName + "="+ Psession);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("A", "getMore"));
		if(model.equals("document_todo")){
			params.add(new BasicNameValuePair("ACTION", "index"));
		}
		else if (model.equals("document_over")) {
			params.add(new BasicNameValuePair("ACTION", "over"));
		}
		else if(model.equals("diary_share")){
			params.add(new BasicNameValuePair("FTYPE", "share"));
		}
		else
		{
			params.add(new BasicNameValuePair("FTYPE", model));
		}
		params.add(new BasicNameValuePair("CURRITERMS",curr_items));

		try {

			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			ContentRequest.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(ContentRequest);
			StringBuilder str = new StringBuilder();
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// String result=EntityUtils.toString(httpResponse.getEntity());
				BufferedReader buffer = null;
				buffer = new BufferedReader(new InputStreamReader(httpResponse
						.getEntity().getContent()));
				for (String s = buffer.readLine(); s != null; s = buffer
						.readLine()) {
					str.append(s);
				}	
				result=str.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result.equals("\"NODATA\""))
			return "\"NOMOREDATA\"";
		
		return result;	
	}
	
	public String getDetailAddress(String address){
		String result="";
		if(address.startsWith("http://")){
			String str1=address.substring(7);
			if(str1.contains("/")){
				String str2=str1.substring(0, str1.lastIndexOf("/"));
				result="http://"+str2;
			}
			else{
				result="http://"+str1;
			}
			
		}
		else if(address.startsWith("https://")){
			String str1=address.substring(8);
			if(str1.contains("/")){
				String str2=str1.substring(0, str1.lastIndexOf("/"));
				result="https://"+str2;
			}
			else{
				result="https://"+str1;
			}
		} 
		
		return result;
		
	}
	public void synCookies(Context context, String url, String cookie) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(url, phpsessidName + "=" + cookie);// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}
	
}
