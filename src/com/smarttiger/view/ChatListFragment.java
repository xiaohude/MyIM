package com.smarttiger.view;

import java.util.ArrayList;

import com.smarttiger.bean.Chat;
import com.smarttiger.bean.ChatLab;
import com.smarttiger.im.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class ChatListFragment extends Fragment {

	private Context context;
	private Chat chat;
	private TextView headText;
	private ArrayList<Chat> mChats;
	private ListView chatList;
	private MyListAdapter adapter;
	
	public ChatListFragment(Context context)
	{
		this.context = context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		chat = new Chat("1");
		mChats = ChatLab.get(context).getChats();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_tab_weixin, parent , false);
		
		headText = (TextView) view.findViewById(R.id.name);
		headText.setText(chat.getqid());
		
		chatList = (ListView) view.findViewById(R.id.chatList);
		adapter = new MyListAdapter(context);
		chatList.setAdapter(adapter);

		chatList.setOnItemClickListener(new ClickEvent());
		
		return view;
	}
	

	public class MyListAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MyListAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mChats.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mChats.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 if (convertView == null) {
				 convertView = inflater.inflate(R.layout.chat_list_item, null);
			 }
			return convertView;
		}
	}
	
	private class ClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

//			TextView q_idText = (TextView) view.findViewById(R.id.q_idText);
			
			Toast.makeText(context, "点击"+position, 1).show();
			
//			Intent intent = new Intent (context, ChatActivity.class);			
//			context.startActivity(intent);
		}
	}
	
}
