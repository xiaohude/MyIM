package com.smarttiger.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.smarttiger.im.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 弹出文件选择窗口，可选择保存路径（可新建文件夹），也可选择文件（可按文件类型筛选）。根据传递进来的参数决定。
 * @author 朱小虎
 * @since 2015-04-14
 * 这是一个Dialog风格的Activity，记得在Manifest中注册。
 * 传递参数通过startActivity（intent），获取参数通过onActivityResult；
 * 
 */
/*
 * 使用方法：
 * public void onFileChoose(View view)
	{
		Intent intent = new Intent(context, MyFilePathChooser.class);
		intent.putExtra("path", "目录全名");//默认为SmartTiger目录
		intent.putExtra("type", ".doc");//默认显示所有格式
		intent.putExtra("hasDirectory", false);//默认显示文件夹
		intent.putExtra("isChooseFile", false);//默认为选择文件，不是选择保存路径。
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
 */
public class MyFilePathChooser extends ListActivity {
	public static final int OK_CODE = 102;
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "";
	private String curPath = "/";//当前目录路径，或被点击的文件路径
	private TextView mPath;
	
	private String path = "";//初始目录路径
	private String type = "";//筛选显示文件类型
	private boolean hasDirectory = true;//是否显示文件夹
	private boolean isChooseFile = true;//区别是选择文件，还是选择目录。
	private boolean isMyWebView = false;
	
	Button btnOK;
	Button btnNew;
	Button btnCancle;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myfilepathchooser);
		
		mPath = (TextView) findViewById(R.id.mPath);
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();//这个目录是SD卡目录
		
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		type = intent.getStringExtra("type");
		hasDirectory = intent.getBooleanExtra("hasDirectory", true);
		isChooseFile = intent.getBooleanExtra("isChooseFile", true);
		isMyWebView = intent.getBooleanExtra("isMyWebView", false);
		
		if(path==null || path.equals(""))
			path = rootPath + "/SmartTiger/";
		
		File pathDirectory = new File(path);
		if(!pathDirectory.exists())
			pathDirectory.mkdir();	
		
		if(type==null)
			type="";
			
		curPath = path;
		
		btnOK = (Button) findViewById(R.id.buttonOK);
		btnNew = (Button) findViewById(R.id.buttonNew);
		btnCancle = (Button) findViewById(R.id.buttonCancle);
		
		if(isChooseFile)
		{
			btnOK.setVisibility(View.GONE);
			btnNew.setVisibility(View.GONE);
		}
		else
			btnCancle.setVisibility(View.GONE);
		
		btnOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isMyWebView)
				{
//					Intent intent = new Intent(MyFilePathChooser.this,MyWebView.class);
//					intent.putExtra("path", curPath);
//					startActivity(intent);
				}
				
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("path", curPath);
				data.putExtras(bundle);
				setResult(OK_CODE, data);
				finish();
			}
		});	
		btnNew.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				System.out.println("新建文件夹--------------");
				newCreat(curPath);
			}
		});		
		btnCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		getFileDir(path);
	}

	public void OnClickClose(View view)
	{
		finish();
	}
	
	private void newCreat(final String path)
	{
		final EditText editText = new EditText(this);
		editText.setText("新建文件夹");
		editText.selectAll();
		new AlertDialog.Builder(this)
			.setTitle("请输入文件夹名：")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(editText)
			.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						String name = editText.getText().toString();
						System.out.println("新建文件夹名为：=======" + name);
						File newDirectory = new File(path +"/"+ name +"/");
						if(!newDirectory.exists())
							newDirectory.mkdir();	
						else
							System.out.println("已存在此文件夹");
						getFileDir(path);
					}
				})
			.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
					}
				})
			.create()
			.show();
	}
	
	private void getFileDir(String filePath) {
		mPath.setText("/sdcard"+filePath.substring(19));
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();
		sortFiles(files);
		if(hasDirectory)
		{
			if (!filePath.equals(rootPath)) {
				items.add("b1");
				paths.add(rootPath);
				items.add("b2");
				paths.add(f.getParent());
			}
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				
				if(file.isDirectory() || file.getName().endsWith(type))
				{		
					items.add(file.getName());
					paths.add(file.getPath());
				}
			}
		}
		else
		{
			for (int i = 0; i < files.length; i++) {
				File file = files[i];				
				if(file.getName().endsWith(type))
				{		
					items.add(file.getName());
					paths.add(file.getPath());
				}
			}
		}

		setListAdapter(new MyAdapter(this, items, paths));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(paths.get(position));
		if (file.isDirectory()) {
			curPath = paths.get(position);
			getFileDir(paths.get(position));

		} else if(isChooseFile){
			curPath = paths.get(position);
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("path", curPath);
			data.putExtras(bundle);
			setResult(OK_CODE, data);
			finish();
			// openFile(file);
		}
	}

	private void sortFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				if (f1 == null || f2 == null) {// 先比较null
					if (f1 == null) {
						{
							return -1;
						}
					} else {
						return 1;
					}
				} else {
					if (f1.isDirectory() == true && f2.isDirectory() == true) { // 再比较文件夹
						return f1.getName().compareToIgnoreCase(f2.getName());
					} else {
						if ((f1.isDirectory() && !f2.isDirectory()) == true) {
							return -1;
						} else if ((f2.isDirectory() && !f1.isDirectory()) == true) {
							return 1;
						} else {
							return f1.getName().compareToIgnoreCase(f2.getName());// 最后比较文件
						}
					}
				}
			}
		});
	}

}