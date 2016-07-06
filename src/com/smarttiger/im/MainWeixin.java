package com.smarttiger.im;


import java.util.ArrayList;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.smarttiger.bean.Chat;
import com.smarttiger.bean.ChatLab;
import com.smarttiger.im.R;
import com.smarttiger.utils.MyDebugUtil;
import com.smarttiger.utils.MyTimeUtil;

public class MainWeixin extends FragmentActivity {
	
	private final String Tag = "MainWeixin";
	
	public static MainWeixin instance = null;
	private Context context;
	 
	private ViewPager mTabPager;	
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;//单个水平动画位移
	private int two;
	private int three;
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	//private Button mRightBtn;
	
	private View view1;
	private View view2;
	private View view3;
	private View view4;
	
	private TextView setTitleText;
	private MyReceiver myReceiver;
	
	
	// TODO 每条消息的JSON设计："{q_id:'4', name:'李四', message:'昨天吃饭怎么样', time:'2015-04-22 10:14', unread:0}" 
	// 发送者，Id，姓名，内容，发送时间,未读条数。 unread可以没有，opt获取默认为0
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_weixin);
         //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        context = this;
        /*
        mRightBtn = (Button) findViewById(R.id.right_btn);
        mRightBtn.setOnClickListener(new Button.OnClickListener()
		{	@Override
			public void onClick(View v)
			{	showPopupWindow (MainWeixin.this,mRightBtn);
			}
		  });*/
        
        
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTab1 = (ImageView) findViewById(R.id.img_weixin);
        mTab2 = (ImageView) findViewById(R.id.img_address);
        mTab3 = (ImageView) findViewById(R.id.img_friends);
        mTab4 = (ImageView) findViewById(R.id.img_settings);
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        one = displayWidth/4; //设置水平动画平移大小
        mTabImg.setMinimumWidth(one); 
        two = one*2;
        three = one*3;
        //Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);
        
//        ChatListFragment chatListFragment = new ChatListFragment(getApplicationContext());
//        chatListFragment.onCreate(savedInstanceState);
        

        //InitImageView();//使用动画
      //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
//        View view1 = chatListFragment.onCreateView(mLi, mTabPager, savedInstanceState);
        view1 = mLi.inflate(R.layout.main_tab_weixin, null);
        view2 = mLi.inflate(R.layout.main_tab_address, null);
        view3 = mLi.inflate(R.layout.main_tab_friends, null);
        view4 = mLi.inflate(R.layout.main_tab_settings, null);
        
        setTitleText = (TextView) view4.findViewById(R.id.setTitleText);
        setTitleText.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyDebugUtil.changeShowMessageWay(context);
			}
		});
        
        initView1();
        
      //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
      //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			//@Override
			//public CharSequence getPageTitle(int position) {
				//return titles.get(position);
			//}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mTabPager.setAdapter(mPagerAdapter);
		
		
//		testGetChats();
		
		//动态注册BroadcastReceiver
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("com.smarttiger.im.NewMessage");
		registerReceiver(myReceiver, filter);
    }
    /**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};
	
	public void OnTab1Click(View view)
	{
		mTabPager.setCurrentItem(0);
	}
	public void OnTab2Click(View view)
	{
		mTabPager.setCurrentItem(1);
	}
	public void OnTab3Click(View view)
	{
		mTabPager.setCurrentItem(2);
	}
	public void OnTab4Click(View view)
	{
		mTabPager.setCurrentItem(3);
	}
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
	
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
	
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

	
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
    		
        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MainWeixin.this,Exit.class);
            	startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //获取 Menu键			
			if(!menu_display){
				//获取LayoutInflater实例
				//inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				inflater= (LayoutInflater)LayoutInflater.from(this);
				
				//这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				//该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //后两个参数是width和height
				//menuWindow.showAsDropDown(layout); //设置弹出效果
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				//如何获取我们main中的控件呢？也很简单
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//下面对每一个Layout进行单击事件的注册吧。。。
				//比如单击某个MenuItem的时候，他的背景色改变
				//事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					
					public void onClick(View arg0) {						
						//Toast.makeText(Main.this, "退出", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
			        	intent.setClass(MainWeixin.this,Exit.class);
			        	startActivity(intent);
			        	menuWindow.dismiss(); //响应点击事件之后关闭Menu
					}
				});				
				menu_display = true;				
			}else{
				//如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
    }
	//设置标题栏右侧按钮的作用
	public void btnmainright(View v) {  
		Intent intent = new Intent (MainWeixin.this,MainTopRightDialog.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "点击了功能按钮", Toast.LENGTH_LONG).show();
      }  	 
	public void exit_settings(View v) {                           //退出  伪“对话框”，其实是一个activity
		Intent intent = new Intent (MainWeixin.this,ExitFromSettings.class);			
		startActivity(intent);	
	 }
	public void startchat(View v) {      //小黑  对话界面
		Intent intent = new Intent (MainWeixin.this, ChatActivity.class);			
		startActivity(intent);	
      } 
	public void btn_shake(View v) {                                   //手机摇一摇
		Intent intent = new Intent (MainWeixin.this,ShakeActivity.class);			
		startActivity(intent);	
	}
	
	

	private ArrayList<Chat> mChats;
	private ListView chatList;
	private ChatListAdapter chatListAdapter;

	private final class ViewHolder {
		public String q_id;
		public TextView q_idText;
		public TextView nameText;
		public TextView messageText;
		public TextView timeText;
		public TextView unReadText;
	}
	private ViewHolder holder = null;
	
	private void initView1()
	{

		mChats = ChatLab.get(this).getChats();
		
		chatList = (ListView) view1.findViewById(R.id.chatList);
		chatListAdapter = new ChatListAdapter(this);
		chatList.setAdapter(chatListAdapter);

		chatList.setOnItemClickListener(new ClickEvent());
	}
	// 自定义适配器生成列表
	public class ChatListAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public ChatListAdapter(Context context) {
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
				 holder = new ViewHolder();
				 holder.q_idText = (TextView) convertView.findViewById(R.id.q_idText);
				 holder.nameText  = (TextView) convertView.findViewById(R.id.nameText);
				 holder.messageText  = (TextView) convertView.findViewById(R.id.messageText);
				 holder.timeText  = (TextView) convertView.findViewById(R.id.timeText);
				 holder.unReadText = (TextView) convertView.findViewById(R.id.unReadText);
				 convertView.setTag(holder);
			 }else {
				 holder = (ViewHolder) convertView.getTag();
			 }
			
//			 Chat chat = mChats.get(getCount()-position-1);//倒序一下,不需要了。
			 Chat chat = mChats.get(position);
//			 holder.q_idText.setText(chat.getqid());
			 holder.nameText.setText(chat.getName());
			 holder.messageText.setText(chat.getMessage());
			 holder.timeText.setText(chat.getTime());
			 
			 if(chat.getUnReadNum() == 0)
				 holder.unReadText.setVisibility(View.GONE);
			 else
			 {
				 holder.unReadText.setVisibility(View.VISIBLE);
				 holder.unReadText.setText(""+chat.getUnReadNum());
			 }
			 
			 holder.q_id = chat.getqid();
			 convertView.setTag(holder);
			return convertView;
		}
	}
	private class ClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			holder = (ViewHolder) view.getTag();
			
			String q_id = holder.q_id;
			
//			testAddNewChat(q_id);
			
			Intent intent = new Intent (MainWeixin.this, ChatActivity.class);	
			intent.putExtra("q_id", q_id);
			startActivity(intent);
			
			if(holder.unReadText.getVisibility() == View.VISIBLE)
			{
				ChatLab.get(context).changeRead(q_id);
				chatListAdapter.notifyDataSetChanged();
			}
				
			
//			testGetChats();
			
//			if(myDBUtil.putChatArray(mChats))
//			{
//				ArrayList<Chat> chats = myDBUtil.getChatArray();
//				if(chats !=null)
//					Toast.makeText(context, chats.toString(), 1).show();
//			}
		}
	}
	
	
	private class MyReceiver extends BroadcastReceiver {// 继承自BroadcastReceiver的子类
		@Override
		public void onReceive(Context context, Intent intent) {// 重写onReceive方法
			String jsonS = intent.getStringExtra("message");
			MyDebugUtil.show(Tag, jsonS);
			
			addNewChat(jsonS);
			
		}
	}
	
	private void addNewChat(String jsonS)
	{
//		String jsonS = "{q_id:'4',name:'姓名4',message:'123234',time:'12:00'}";
		mChats = ChatLab.get(context).addChatFromJson(jsonS);
		chatListAdapter.notifyDataSetChanged();
	}
	private void testAddNewChat(int q_id)
	{
		String jsonS = "{q_id:'"+q_id+"',name:'姓名"+q_id+"',message:'123234',time:'"+MyTimeUtil.getTimeNow()+"'}";
		mChats = ChatLab.get(context).addChatFromJson(jsonS);
		chatListAdapter.notifyDataSetChanged();
	}
	private void testGetChats()
	{
		String jsonArrayS = 
				"[" +
				"{q_id:'3',name:'张三',unread:3,message:'你好',time:'3分钟前'}," +
				"{q_id:'4',name:'李四',unread:0,message:'昨天吃饭怎么样',time:'15分钟前'}," +
				"{q_id:'5',name:'王五',unread:0,message:'睡觉好吗？',time:'18:34'}," +
				"{q_id:'6',name:'赵六',unread:0,message:'刚才找你',time:'16:56'}," +
				"{q_id:'7',name:'孙七',unread:0,message:'你的电话是多少',time:'14:25'}," +
				"{q_id:'8',name:'周八',unread:0,message:'今天天气不错',time:'12:05'}," +
				"{q_id:'9',name:'吴九',unread:0,message:'那有阳光沙滩',time:'09:09'}," +
				"{q_id:'1',name:'刘一',unread:0,message:'表不错',time:'昨天'}," +
				"{q_id:'2',name:'陈二',unread:0,message:'嗯，挺好',time:'04月09日'}" +
				"]";
		mChats = ChatLab.get(context).getChatsFromJson(jsonArrayS);
		chatListAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ChatLab.get(context).SaveChatsDB();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}
}
    
    

