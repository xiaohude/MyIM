package com.smarttiger.im;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smarttiger.im.R;
import com.smarttiger.utils.MyDebugUtil;


public class ChatActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private static final String Tag = "ChatActivity";
	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private MyReceiver myReceiver;
	private TextView titleText;
	
	private String my_q_id = "4";
	private String q_id = "1";
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_xiaohei);
        //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        initView();
        
        Intent intent = getIntent();
        q_id = intent.getStringExtra("q_id");
        titleText.setText(q_id);
        
        initData();
        
        //动态注册BroadcastReceiver
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("com.smarttiger.im.NewMessage");
		registerReceiver(myReceiver, filter);
    }
    
    private class MyReceiver extends BroadcastReceiver {// 继承自BroadcastReceiver的子类
		@Override
		public void onReceive(Context context, Intent intent) {// 重写onReceive方法
			String from_q_id = intent.getStringExtra("q_id");
			String data = intent.getStringExtra("message");
			MyDebugUtil.show(Tag, data);
			
			if(q_id.equals(from_q_id))
				getSingleMessage(data);
			
		}
	}

    
    public void initView()
    {
    	mListView = (ListView) findViewById(R.id.listview);
    	mBtnSend = (Button) findViewById(R.id.btn_send);
    	mBtnSend.setOnClickListener(this);
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	titleText = (TextView) findViewById(R.id.titleText);
    	
    	mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }
    
    public void initData()
    {
		
		String jsonA = 	"[" +
						"{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'你好啊'}," +
						"{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'最近怎么样'}," +
						"{time:'2015-04-22 10:14', name:'李四', q_id:'4', message:'嗯，还不错'}," +
						"{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'吃饭了不？'}," +
						"{time:'2015-04-22 10:14', name:'李四', q_id:'4', message:'刚吃完'}," +
						"{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'哦'}," +
						"{time:'2015-04-22 10:14', name:'李四', q_id:'4', message:'你最近干嘛呢？'}," +
						"{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'呆着'}," +
						"{time:'2015-04-22 10:14', name:'李四', q_id:'4', message:'好吧'}," +
						"{time:'2015-04-22 10:14', name:'李四', q_id:'4', message:'有事回聊。'}" +
						"]";
		getMessageList(jsonA);
		
		
		String jsonS = "{time:'2015-04-22 10:14', name:'张三', q_id:'3', message:'获取单条消息'}";
		getSingleMessage(jsonS);
		
    }
    
    private void getMessageList(String jsonA)
    {
    	try {
			JSONArray jsonArray = new JSONArray(jsonA);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ChatMsgEntity entity = new ChatMsgEntity();
				
				String time = jsonObject.getString("time");
				String name = jsonObject.getString("name");
				boolean isMy = jsonObject.getString("q_id").equals(my_q_id);
				String message = jsonObject.getString("message");
				
				entity.setDate(time);
				entity.setName(name);
    			entity.setMsgType(isMy);
				entity.setText(message);
				
				mDataArrays.add(entity);
			}
			
			mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
			mListView.setAdapter(mAdapter);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void getSingleMessage(String jsonS)
    {
    	try {
	    	JSONObject jsonObject = new JSONObject(jsonS);
			ChatMsgEntity entity = new ChatMsgEntity();
			
			String time = jsonObject.getString("time");
			String name = jsonObject.getString("name");
			boolean isMy = jsonObject.getString("q_id").equals(my_q_id);
			String message = jsonObject.getString("message");
			
			entity.setDate(time);
			entity.setName(name);
			entity.setMsgType(isMy);
			entity.setText(message);
			
			mDataArrays.add(entity);
			
	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
    }


	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}
	
	private void send()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("李四");
			entity.setMsgType(true);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }
    
    
    public void head_xiaohei(View v) {     //标题栏 返回按钮
    	Intent intent = new Intent (ChatActivity.this,InfoXiaohei.class);			
		startActivity(intent);	
      } 
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(myReceiver);
    }
}