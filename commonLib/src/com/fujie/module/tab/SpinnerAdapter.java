package com.fujie.module.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fujie.module.titlebar.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter<T> extends BaseAdapter{

	
	
	private Context context;
	List <T>mObject  = new ArrayList<T>();
	
	
	public SpinnerAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mObject.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mObject.get(position).toString();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void refreshData(List<T> data)
	{
		mObject = data;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//findViewById比较耗时所以选择ViewHolder来使用只调用此find..之后用getTag来获取
		ViewHolder viewHolder = null  ;
		if (convertView==null) {                                                                                                                                                                                                                                                                                            
			convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Object obj = getItem(position);
		
		viewHolder.tv.setText(obj.toString());
		return convertView;
	}
	
	
	public static class ViewHolder
	{
		public TextView tv ;
	}

	
	
	
	

	
}
