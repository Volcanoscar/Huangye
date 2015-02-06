package com.nuo.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nuo.activity.ChatActivity;
import com.nuo.activity.NewSmsActivity;
import com.nuo.activity.R;
import com.nuo.cursor.ContactsCursor;
import com.nuo.utils.Utils;

import java.util.ArrayList;

/**
 * 电话-详情 里的 电话列表 (一个联系人对应多个电话)
 */
public class ContactDetailAdapter extends BaseAdapter{
    private Context context;
    private ContactsCursor.SortEntry contact;

    public ContactDetailAdapter(Context context)
    {
        this.context = context;
    }

    public ContactDetailAdapter(Context context, ContactsCursor.SortEntry contact) {
        this.context = context;
        this.contact= contact;

    }

    @Override
    public int getCount() {
        return contact.getmNums().size();
    }

    @Override
    public Object getItem(int position) {
        return contact.getmNums().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.contacts_detail_item, parent, false);
        }
        TextView number = (TextView) convertView.findViewById(R.id.telTextView);
        ImageButton sms = (ImageButton) convertView.findViewById(R.id.imageButton);
        number.setText(contact.getmNums().get(position));
        //设置动作
        number.setOnClickListener(new MyOnClickListener(contact.getmNums().get(position)));
        sms.setOnClickListener(new MyOnClickListener(contact.getmNums().get(position)));
        return convertView;
    }
    class MyOnClickListener implements View.OnClickListener{
        private String number;

        public MyOnClickListener(String number) {
            this.number = number;
        }
        @Override
        public void onClick(View view) {

            if(R.id.telTextView==view.getId()){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + number));
                context.startActivity(intent);
            }else if(R.id.imageButton==view.getId()){
                Intent intent = new Intent(context,ChatActivity.class);
                Bundle mBundle = new Bundle();
                int index = -1;
                for(int i=0;i< Utils.mPersonSmsList.size();i++) {
                    if (number.equals(Utils.mPersonSmsList.get(i).Number)) {
                        index = i;
                    }
                }
                SmsCursor.Person_Sms person_sms=null;
                if(index!=-1){
                  person_sms= Utils.mPersonSmsList.get(index);
                }

                if(person_sms==null){
                    Intent newSmsIntent = new Intent(context, NewSmsActivity.class);
                    newSmsIntent.putExtra("contact", contact);
                    context.startActivity(newSmsIntent);
                    Intent smsIntent = new Intent("android.huahua.BatchSendSms");
                    Bundle smsMBundle = new Bundle();
                    ArrayList<ContactsCursor.SortEntry> mSortList = new ArrayList<ContactsCursor.SortEntry>();
                    mSortList.add(contact);
                    smsMBundle.putSerializable("BatchSendSms", mSortList);
                    smsIntent.putExtras(mBundle);
                    context.sendBroadcast(smsIntent);
                }else{
                    mBundle.putSerializable("chatperson",person_sms);
                    intent.putExtras(mBundle);
                    context. startActivity(intent);
                }
            }
        }
    }
}
