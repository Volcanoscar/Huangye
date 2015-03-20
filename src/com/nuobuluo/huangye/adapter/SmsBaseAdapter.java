package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsBaseAdapter extends BaseAdapter{
	private Context context;
	
    public SmsBaseAdapter(Context context) 
    {
	    this.context = context;
    }
	@Override
	public int getCount() {
		return Utils.mPersonSmsList.size();
	}

	@Override
	public Object getItem(int position) {
		return Utils.mPersonSmsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.sms_list_item, parent, false);
		}

		 TextView name = (TextView)convertView.findViewById(R.id.sms_name);
		 
	      String SmsName = Utils.mPersonSmsList.get(position).Name + " ("+Utils.mPersonSmsList.get(position).person_smss.size()+")";  
	      int start = SmsName.indexOf('(');  
	      int end = SmsName.length();  
	      Spannable word = new SpannableString(SmsName);  
	  
	      word.setSpan(new AbsoluteSizeSpan(28), start, end,   
	                Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
	      name.setText(word); 
		 

		 TextView sms_content = (TextView)convertView.findViewById(R.id.sms_content);
		 sms_content.setText(Utils.mPersonSmsList.get(position).person_smss.get(0).SMSContent);
		 

		 TextView data = (TextView)convertView.findViewById(R.id.sms_date);
		 
		 Date date2 = new Date(Utils.mPersonSmsList.get(position).person_smss.get(0).SMSDate);
		 SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
		 String time = sfd.format(date2);
		 data.setText(time);
		 
		return convertView;
	}

}
