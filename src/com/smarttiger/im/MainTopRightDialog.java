package com.smarttiger.im;


import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import cn.pedant.SweetAlert.widget.SweetAlertDialog;

import com.smarttiger.im.R;
import com.smarttiger.service.PushServer;
import com.smarttiger.service.PushServer2;
import com.smarttiger.utils.MyDebugUtil;
import com.smarttiger.utils.MyFilePathChooser;
import com.smarttiger.utils.MySDcardFile;
import com.smarttiger.utils.ShowMessageUtil;

public class MainTopRightDialog extends Activity {
	//private MyDialog dialog;
	private final int GET_FILE_PATH = 101;
	private Context context;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;
	    getWindow().getAttributes().gravity = Gravity.RIGHT;
		setContentView(R.layout.main_top_right_dialog);
		context = this;
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	

	public void onScanning(View view)
	{
		ShowMessageUtil.showDialog(context, SweetAlertDialog.WARNING_TYPE, "提示", "点击扫描二维码", null, "取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ShowMessageUtil.showTimeProgress(context, "加载中", 7);
			}
		}, 
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ShowMessageUtil.showDialog(context,SweetAlertDialog.ERROR_TYPE, "失败", "点击取消按钮");
			}
		});
		

		
//		MySDcardFile mySDcardFile = new MySDcardFile();
//		try {
//			mySDcardFile.writeSDFile("1111111111111111111", "1111111111");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	

	public void onFileChoose(View view)
	{
		Intent intent = new Intent(context, MyFilePathChooser.class);
//		intent.putExtra("path", "目录全名");
//		intent.putExtra("type", ".doc");
//		intent.putExtra("hasDirectory", false);
		intent.putExtra("isChooseFile", false);
		startActivityForResult(intent, GET_FILE_PATH);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == GET_FILE_PATH)
		{
			if(resultCode == MyFilePathChooser.OK_CODE)
			{
				String path = data.getStringExtra("path");
				MyDebugUtil.show("path====", path);
			}
		}
	}
	
	public void onStartServer(View view)
	{
		Intent intent = new Intent(context, PushServer2.class);
		context.startService(intent);
		
		finish();
	}
	
	public void onStopServer(View view)
	{
		Intent intent = new Intent(context, PushServer2.class);
		context.stopService(intent);
	}
	
	
	
	/*
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
    	this.finish();
    	MainWeixin.instance.finish();//关闭Main 这个Activity
      }  
	*/
}
