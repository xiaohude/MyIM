package com.smarttiger.bean;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smarttiger.utils.MyDBUtil;

import android.content.Context;

/**
 * 聊天消息列表的数据管理类
 * */
public class ChatLab {
	
	private static ChatLab sChatLab;
	private Context context;
	private MyDBUtil myDBUtil;
	private ArrayList<Chat> mChats;
	
	
	public ChatLab(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
        myDBUtil = new MyDBUtil(context);
		mChats = myDBUtil.getChatArray();
		if(mChats == null)
			mChats = new ArrayList<Chat>();
		//测试数据
//		for (int i = 0; i < 10; i++) {
//            Chat c = new Chat(i);
//            c.setName("姓名" + i);
//            c.setMessage("最近消息"+i);
//            c.setTime((20-i)+"分钟前");
//            mChats.add(c);
//        }
	}
	/**
	 * 相当于封装一次构造函数，因为sChatLab是静态的，所以判断是否已存在，再决定是否调用构造函数。
	 * @param context
	 * @return
	 */
	public static ChatLab get(Context context)
	{
		if(sChatLab == null)
			sChatLab = new ChatLab(context);
		return sChatLab;
	}
	
	public ArrayList<Chat> getChatsFromJson(String jsonArrayS)
	{
		mChats = new ArrayList<Chat>();
		try {
			JSONArray jsonArray = new JSONArray(jsonArrayS);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				Chat chat = new Chat(json.getString("q_id"));
	            chat.setName(json.getString("name"));
	            chat.setMessage(json.getString("message"));
	            chat.setTime(json.getString("time"));
	            chat.setUnReadNum(json.optInt("unread",0));
	            mChats.add(chat);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mChats;
	}
	
	public ArrayList<Chat> getChats()
	{
		return mChats;
	}
	
	/**
	 * 添加新消息到列表，如果没有，直接添加，如果有则删除原来的，再添加。
	 * */
	public ArrayList<Chat> addChatFromJson(String jsonS)
	{
		try {
			JSONObject json = new JSONObject(jsonS);
			String q_id = json.getString("q_id");
			for (int i = 0; i < mChats.size(); i++)  {
				Chat chat = mChats.get(i);
				if(chat.getqid().equals(q_id))
					mChats.remove(i);
			}
			Chat chat = new Chat(json.getString("q_id"));
	        chat.setName(json.getString("name"));
	        chat.setMessage(json.getString("message"));
	        chat.setTime(json.getString("time"));
            chat.setUnReadNum(json.optInt("unread",0));
	        mChats.add(0, chat);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return mChats;
	}
	
	/**
	 * 更改此条q_id的未读状态。改变为已读，unread=0
	 * */
	public void changeRead(String q_id)
	{
		for (int i = 0; i < mChats.size(); i++)  {
			Chat chat = mChats.get(i);
			if(chat.getqid().equals(q_id))
				chat.setUnReadNum(0);
		}
	}
	
	/**
	 * 将回话列表保存到本地数据库中。
	 * */
	public void SaveChatsDB()
	{
		myDBUtil.putChatArray(mChats);
	}
	
}
