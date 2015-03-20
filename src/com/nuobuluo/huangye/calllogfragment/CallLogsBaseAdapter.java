package com.nuobuluo.huangye.calllogfragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nuobuluo.huangye.activity.AddContactsActivity;
import com.nuobuluo.huangye.activity.ChatActivity;
import com.nuobuluo.huangye.activity.ContactsDetailActivity;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.adapter.SmsCursor;
import com.nuobuluo.huangye.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallLogsBaseAdapter extends BaseAdapter{
    private Context context;

    public CallLogsBaseAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Utils.mPersonCallLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return Utils.mPersonCallLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate( R.layout.calllogs_list_item, parent, false);
        }

        //通话记录的号码
        TextView number = (TextView)convertView.findViewById(R.id.calllog_number);
        number.setText(Utils.mPersonCallLogList.get(position).Number);

        //通话记录的姓名
        TextView name = (TextView)convertView.findViewById(R.id.calllog_name);

        String CallLogName = Utils.mPersonCallLogList.get(position).Name + " ("+Utils.mPersonCallLogList.get(position).person_calllogs.size()+")";
        int start = CallLogName.indexOf('(');
        int end = CallLogName.length();
        Spannable word = new SpannableString(CallLogName);

        word.setSpan(new AbsoluteSizeSpan(28), start, end,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        name.setText(word);

        //通话记录的电话状态
        ImageView Type = (ImageView)convertView.findViewById(R.id.calllog_type);
        if(Utils.mPersonCallLogList.get(position).person_calllogs.get(0).CallLogType == CallLog.Calls.INCOMING_TYPE)
        {
            Type.setBackgroundResource(R.drawable.ic_calllog_incomming_normal);
        }
        else if(Utils.mPersonCallLogList.get(position).person_calllogs.get(0).CallLogType== CallLog.Calls.OUTGOING_TYPE)
        {
            Type.setBackgroundResource(R.drawable.ic_calllog_outgoing_nomal);
        }
        else if(Utils.mPersonCallLogList.get(position).person_calllogs.get(0).CallLogType== CallLog.Calls.MISSED_TYPE)
        {
            Type.setBackgroundResource(R.drawable.ic_calllog_missed_normal);
        }

        //通话记录的日期
        TextView data = (TextView)convertView.findViewById(R.id.calllog_data);

        Date date2 = new Date(Utils.mPersonCallLogList.get(position).person_calllogs.get(0).CallLogDate);
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = sfd.format(date2);
        data.setText(time);

        ImageButton dialBtn = (ImageButton)convertView.findViewById(R.id.calllog_dial);
        dialBtn.setTag(Utils.mPersonCallLogList.get(position).Number);
        dialBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + (String)arg0.getTag()));
                context.startActivity(intent);
            }
        });

        LinearLayout Toplayout = (LinearLayout)convertView.findViewById(R.id.toplayout);
        Toplayout.setOnClickListener(new LayoutClick(position));
        LinearLayout Diallayout = (LinearLayout)convertView.findViewById(R.id.calllog_dial_layout);
        Diallayout.setOnClickListener(new LayoutClick(position));
        LinearLayout Smslayout = (LinearLayout)convertView.findViewById(R.id.calllog_sms_layout);
        Smslayout.setOnClickListener(new LayoutClick(position));
        LinearLayout Recordlayout = (LinearLayout)convertView.findViewById(R.id.calllog_record_layout);
        Recordlayout.setOnClickListener(new LayoutClick(position));
        LinearLayout Infolayout = (LinearLayout)convertView.findViewById(R.id.calllog_info_layout);
        Infolayout.setOnClickListener(new LayoutClick(position));

        TextView TV_Info = (TextView)convertView.findViewById(R.id.info_TV);
        ImageView IV_Info = (ImageView)convertView.findViewById(R.id.info_IV);
        if(Utils.mPersonCallLogList.get(position).Name.equals(Utils.mPersonCallLogList.get(position).Number))
        {
            TV_Info.setText("添加");
            IV_Info.setImageResource(R.drawable.ic_calllog_add_contact_new);
        }
        else
        {
            TV_Info.setText("详情");
            IV_Info.setImageResource(R.drawable.ic_calllog_more_new);
        }

        LinearLayout Detaillayout = (LinearLayout)convertView.findViewById(R.id.bottomlayout);
        if(Utils.mPersonCallLogList.get(position).choose)
        {
            Detaillayout.setVisibility(View.VISIBLE);
        }
        else
        {
            Detaillayout.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class LayoutClick implements View.OnClickListener{

        private int position;
        public LayoutClick(int index) {
            position = index;
        }

        @Override
        public void onClick(View v) {
            if(R.id.toplayout == v.getId())
            {
                if(Utils.mPersonCallLogList.get(position).choose)
                {
                    Utils.mPersonCallLogList.get(position).choose = false;
                }
                else
                {
                    for(int i=0;i<Utils.mPersonCallLogList.size();i++)
                    {
                        if(Utils.mPersonCallLogList.get(i).choose)
                        {
                            Utils.mPersonCallLogList.get(i).choose = false;
                            break;
                        }
                    }
                    Utils.mPersonCallLogList.get(position).choose = true;
                }
                Utils.m_calllogsadapter.notifyDataSetChanged();
            }
            else if(R.id.calllog_dial_layout == v.getId())
            {
                Utils.mPersonCallLogList.get(position).choose = false;
                Intent  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + Utils.mPersonCallLogList.get(position).Number));
                context.startActivity(intent);
                Utils.m_calllogsadapter.notifyDataSetChanged();
            }
            else if(R.id.calllog_sms_layout == v.getId())
            {
                Utils.mPersonCallLogList.get(position).choose = false;

                Boolean HaveSmsRecord=false;
                for(int i=0;i<Utils.mPersonSmsList.size();i++)
                {
                    if(Utils.mPersonSmsList.get(i).Name.equals(Utils.mPersonCallLogList.get(position).Name))
                    {
                        HaveSmsRecord = true;
                        Intent intent = new Intent(context,ChatActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("chatperson", Utils.mPersonSmsList.get(i));
                        intent.putExtras(mBundle);
                        context.startActivity(intent);

                        break;
                    }
                }

                if(!HaveSmsRecord)
                {
                    SmsCursor.Person_Sms person_sms = new SmsCursor.Person_Sms();
                    person_sms.Name = Utils.mPersonCallLogList.get(position).Name;
                    person_sms.Number = Utils.mPersonCallLogList.get(position).Number;

                    Intent intent = new Intent(context,ChatActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("chatperson", person_sms);
                    intent.putExtras(mBundle);
                    context.startActivity(intent);
                }
                Utils.m_calllogsadapter.notifyDataSetChanged();
            }
            else if(R.id.calllog_record_layout == v.getId())
            {
                AlertDialog ListDialog = new AlertDialog.Builder(context).
                        setTitle("全部通话").
                        setAdapter(new RerordAdapter(position), new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).
                        setNegativeButton("关闭", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).
                        create();
                ListDialog.show();
            }
            else if(R.id.calllog_info_layout == v.getId())
            {
                if(Utils.mPersonCallLogList.get(position).Name.equals(Utils.mPersonCallLogList.get(position).Number))//如果是未知号码
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("tpye", 0);
                    bundle.putString("name", "");

                    bundle.putString("number", Utils.mPersonCallLogList.get(position).Number);

                    Intent intent = new Intent(context, AddContactsActivity.class);
                    intent.putExtra("person", bundle);
                    context.startActivity(intent);
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("tpye", 0);
                    bundle.putString("name", Utils.mPersonCallLogList.get(position).Name);
                    bundle.putString("number", Utils.mPersonCallLogList.get(position).Number);
                   for(int i=0;i<Utils.mPersons.size();i++) {
                       if (Utils.mPersonCallLogList.get(position).Number.equals(Utils.mPersons.get(i).getmNum())) {
                           bundle.putSerializable("contact", Utils.mPersons.get(i));
                       }
                   }
                    int index =-1;
                    for(int i=0;i<Utils.mPersonSmsList.size();i++) {
                        if (Utils.mPersonCallLogList.get(position).Number.equals(Utils.mPersonSmsList.get(i).Number)) {
                            index = i;
                        }
                    }
                   if(index!=-1){
                       bundle.putSerializable("sms",Utils.mPersonSmsList.get(index));
                       bundle.putBoolean("hasSms",true);
                   }else{
                       bundle.putBoolean("hasSms",false);
                   }
                    Intent intent = new Intent(context, ContactsDetailActivity.class);
                    intent.putExtra("person", bundle);
                    context.startActivity(intent);
                }
            }
        }

    }

    private class RerordAdapter extends BaseAdapter{
        private int index;
        public RerordAdapter(int position) {
            index = position;
        }

        @Override
        public int getCount() {
            return Utils.mPersonCallLogList.get(index).person_calllogs.size();
        }

        @Override
        public Object getItem(int position) {
            return Utils.mPersonCallLogList.get(index).person_calllogs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.calllogs_record_list_item, parent, false);
            }

            //通话记录的电话状态
            ImageView Type = (ImageView)convertView.findViewById(R.id.record_tpye);
            if(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogType == CallLog.Calls.INCOMING_TYPE)
            {
                Type.setBackgroundResource(R.drawable.ic_calllog_incomming2);
            }
            else if(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogType== CallLog.Calls.OUTGOING_TYPE)
            {
                Type.setBackgroundResource(R.drawable.ic_calllog_outgoing2);
            }
            else if(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogType== CallLog.Calls.MISSED_TYPE)
            {
                Type.setBackgroundResource(R.drawable.ic_calllog_missed2);
            }

            //通话记录的日期
            TextView data = (TextView)convertView.findViewById(R.id.record_date);

            Date date2 = new Date(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogDate);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String time = sfd.format(date2);
            data.setText(time);

            //通话记录时长
            TextView duration = (TextView)convertView.findViewById(R.id.record_info);
            int timeduration;
            timeduration = Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogDuration;
            if(timeduration > 0 && timeduration < 60)
            {
                if(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogType== CallLog.Calls.MISSED_TYPE)
                {
                    duration.setText("响铃"+timeduration+"秒");
                }
                else
                {
                    duration.setText("通话"+timeduration+"秒");
                }
            }
            else if(timeduration >= 60 && timeduration < 3600)
            {
                if(Utils.mPersonCallLogList.get(index).person_calllogs.get(position).CallLogType== CallLog.Calls.MISSED_TYPE)
                {
                    duration.setText("响铃"+timeduration/60+"分"+timeduration%60+"秒");
                }
                else
                {
                    duration.setText("通话"+timeduration/60+"分"+timeduration%60+"秒");
                }

            }
            else if(timeduration >= 3600)
            {
                duration.setText("通话"+timeduration/3600+"小时"+(timeduration%3600)/60+"分"+timeduration%60+"秒");
            }
            else
            {
                duration.setText("未接通");
            }


            return convertView;
        }

    }

}
