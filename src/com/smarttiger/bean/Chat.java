package com.smarttiger.bean;


public class Chat {

	private String q_id;
	private int icon;
	private String name;
	private String message;
	private String time;
	private int unReadNum;
	
	//因为使用到SnappyDB来读取此类，所以必须有一个无参数的构造函数。
	public Chat() {
		// TODO Auto-generated constructor stub
	}
	public Chat(String q_id) {
		// TODO Auto-generated constructor stub
		this.q_id = q_id;
	}

	public String getqid() {
		return q_id;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public int getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}

	public String toString(){
		return q_id+","+name+","+message+","+time+","+unReadNum;
	}
}
