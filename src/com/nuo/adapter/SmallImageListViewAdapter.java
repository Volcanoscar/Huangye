package com.nuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nuo.activity.R;

public class SmallImageListViewAdapter extends BaseAdapter{

	public SmallImageListViewAdapter(Context con){
		mInflater=LayoutInflater.from(con);
	}
	@Override
	public int getCount() {
		return 5;
	}
	private LayoutInflater mInflater;
	@Override
	public Object getItem(int position) {
		return position;
	}
	private ViewHolder vh 	 =new ViewHolder();
	private static class ViewHolder {
		private ImageView im;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.small_image_item, null);
			vh.im=(ImageView)convertView.findViewById(R.id.iv_pic);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		return convertView;
	}
}