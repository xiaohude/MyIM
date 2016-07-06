package com.smarttiger.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.smarttiger.bean.Chat;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyDBUtil {
	
	private static final String dbName = "MyAppIM";
	
	private Context context;
	private DB snappydb;
	
	public MyDBUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
		try {
		    snappydb = DBFactory.open(context, dbName); //create or open an existing databse using the default name
		} catch (SnappydbException e) {
			Toast.makeText(context, "初始化SnappyDB异常！", 1).show();
		}
	}
	
	private void test()
	{
		try {
		    snappydb.put("name", "Jack Reacher"); 
		    snappydb.putInt("age", 42);  
		    snappydb.putBoolean("single", true);
		    snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"}); 

		    String   name   =  snappydb.get("name");
		    int        age    =  snappydb.getInt("age");
		    boolean  single =  snappydb.getBoolean("single");
		    String[] books  =  snappydb.getArray("books", String.class);// get array of string

		    Toast.makeText(context, "SnappyDB=="+name+age+single+books[0]+books[1]+books[2], 1).show();

		} catch (SnappydbException e) {
			Toast.makeText(context, "test异常！", 1).show();
		}
	}
	
	public Boolean putChat(Chat chat)
	{
		try {
			snappydb.put("chat", chat);
			return true ;
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MyDBUtil", "putChat异常！");
			return false ;
		}
	}
	
	public Chat getChat()
	{
		try {
			Chat chat = snappydb.getObject("chat", Chat.class);
			return chat;
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MyDBUtil", "getChat异常！");
			return null;
		}
	}
	
	
	public Boolean putChatArray(ArrayList<Chat> mChats)
	{
		try {
			snappydb.put("chats", mChats.toArray());
			return true ;
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MyDBUtil", "putChatArray异常！");
			return false ;
		}
	}
	
	public ArrayList<Chat> getChatArray()
	{
		try {
			Chat[] chats = snappydb.getObjectArray("chats", Chat.class);
			ArrayList<Chat> mChats = new ArrayList<Chat>(Arrays.asList(chats));		
			return mChats;
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			destroyDB();
			try {
				snappydb.del("chats");
			} catch (SnappydbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("MyDBUtil", "getChatArray异常！");
			return null;
		}
	}
	
	//关闭数据库
	public void closeDB()
	{
		try {
			snappydb.close();
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//销毁数据
	public void destroyDB()
	{
		try {
			snappydb.destroy();
		} catch (SnappydbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
