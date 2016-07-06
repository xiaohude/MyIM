package com.smarttiger.utils;

import java.io.File;
import java.util.List;

import com.smarttiger.im.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Bitmap mIcon1;
	private Bitmap mIcon2;
	private Bitmap mIcon3;
	private Bitmap mIcon4;
	private Bitmap mIcon5;
	private Bitmap mIcon6;
	private Bitmap mIcon7;
	private Bitmap mIcon8;
	private Bitmap mIcon9;

	private List<String> items;
	private List<String> paths;

	public MyAdapter(Context context, List<String> it, List<String> pa) {
		mInflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
		mIcon2 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_back01);
		mIcon1 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_back02);
		mIcon3 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.folder_64);
		mIcon4 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_64);
		mIcon5 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_image_64);
		mIcon6 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_excel_64);
		mIcon7 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_pdf_64);
		mIcon8 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_word_64);
		mIcon9 = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.file_powerpoint_64);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.file_row, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		File f = new File(paths.get(position).toString());
		
		String end=f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()).toLowerCase();
		if (items.get(position).toString().equals("b1")) {
			holder.text.setText(R.string.returns_root);
			holder.icon.setImageBitmap(mIcon1);
		} else if (items.get(position).toString().equals("b2")) {
			holder.text.setText(R.string.returns_layer);
			holder.icon.setImageBitmap(mIcon2);
		} else {
			holder.text.setText(f.getName());

			if (f.isDirectory()) {
				holder.icon.setImageBitmap(mIcon3);
			} else if(end.equals("jpg") || end.equals("gif") || end.equals("png")
					|| end.equals("jpeg") || end.equals("bmp")){
				holder.icon.setImageBitmap(mIcon5);
			}
			else if(end.equals("xls") ||end.equals("xlsx") ){
				holder.icon.setImageBitmap(mIcon6);
			}
			else if(end.equals("pdf") ){
				holder.icon.setImageBitmap(mIcon7);
			}
			else if(end.equals("doc") ||end.equals("docx") ){
				holder.icon.setImageBitmap(mIcon8);
			}
			else if(end.equals("ppt") ||end.equals("pptx") ){
				holder.icon.setImageBitmap(mIcon9);
			}
			else{
				holder.icon.setImageBitmap(mIcon4);

			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView icon;
	}
}
