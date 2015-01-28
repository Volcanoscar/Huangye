package com.nuo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.util.Linkify;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.adapter.SmsCursor.SMSs;
import com.nuo.adapter.SmsCursor.Person_Sms;
import com.nuo.task.AddSmsTask;
import com.nuo.task.DeleteSmsTask;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity implements OnClickListener{
    //发送信息的按钮
    private Button m_SendBtn;
    //编辑信息框
    private EditText m_MsgEditText;
    //聊天记录列表
    private ListView m_ChatLogList;
    //聊天记录列表的适配器
    private ChatLogAdapter m_ChatLogAdapter;
    //聊天对象
    Person_Sms m_chat_person;
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
    private String defaultMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.chat_activtity);
        ViewUtils.inject(this);

        m_SendBtn = (Button) findViewById(R.id.btn_chat_send);
        m_SendBtn.setOnClickListener(this);
        m_MsgEditText = (EditText) findViewById(R.id.et_chat_msg);
        defaultMsg = (String)getIntent().getSerializableExtra("msg");
        if (defaultMsg != null) {
            m_MsgEditText.setText(defaultMsg);
        }
        m_ChatLogList = (ListView) findViewById(R.id.chat_list);
        m_chat_person = (Person_Sms)getIntent().getSerializableExtra("chatperson");

        initTitleView();
        Collections.reverse(m_chat_person.person_smss);
        m_ChatLogAdapter = new ChatLogAdapter();
        m_ChatLogList.setAdapter(m_ChatLogAdapter);
        m_ChatLogList.setSelection(m_ChatLogAdapter.getCount() - 1);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.huahua.SMS_RECEIVED");
        registerReceiver(mReceiver , filter);

    }

    private void initTitleView() {
        mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
        mTitleBarView.setTitleText(m_chat_person.Name);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBarView.getBtnRight().setPadding(8, 0, 8, 0);
        mTitleBarView.setBtnRight(R.drawable.ic_call_hollow, R.string.add_sms);
        mTitleBarView.setBtnRightBg(R.drawable.login_btn);
        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + m_chat_person.Number));
                startActivity(intent);
            }
        });
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")
                    || intent.getAction().equals("android.huahua.SMS_RECEIVED"))
            {
                StringBuilder sb = new StringBuilder();
                Bundle bundle = intent.getExtras();

                SMSs new_sm =  new SMSs();
                new_sm.SMSContent = "";
                if (bundle != null)
                {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msg = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)
                    {
                        msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    for (SmsMessage currMsg : msg)
                    {
                        m_chat_person.Number = currMsg.getDisplayOriginatingAddress();
                        new_sm.SMSDate = currMsg.getTimestampMillis();
                        new_sm.SMSContent = new_sm.SMSContent + currMsg.getDisplayMessageBody();
                        new_sm.SMSType = 1;
                    }

                    m_chat_person.person_smss.add(new_sm);
                    m_ChatLogAdapter.notifyDataSetChanged();
                    m_ChatLogList.setSelection(m_ChatLogList.getCount() - 1);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_chat_send)
        {
            if(!"".equals(m_MsgEditText.getText().toString().trim()))
            {
                SendSMS(m_MsgEditText.getText().toString(), m_chat_person.Number, System.currentTimeMillis());
            }
        }
    }

    private class ChatLogAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return m_chat_person.person_smss.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return m_chat_person.person_smss.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView tvContent = null;
            final int index = position;
            final int SmsId = m_chat_person.person_smss.get(position).SMSId;
            final String SmsContent = m_chat_person.person_smss.get(position).SMSContent;
            if (m_chat_person.person_smss.get(position).SMSType == 1)
            {
                convertView = LayoutInflater.from(ChatActivity.this).inflate(
                        R.layout.chat_list_item_receive, null);

                tvContent = (TextView) convertView
                        .findViewById(R.id.tv_chatcontent);
                tvContent.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        Vibrator vib = (Vibrator)ChatActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                        vib.vibrate(50);

                        AlertDialog ListDialog = new AlertDialog.Builder(ChatActivity.this).
                                setTitle("信息选项").
                                setItems(new String[] { "转发","删除"}, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            ForwardSMS(SmsContent);
                                        }
                                        else if(which == 1)
                                        {
                                            DeleteSMS(SmsId, index);
                                        }
                                    }
                                }).
                                create();
                        ListDialog.show();
                        return false;
                    }
                });
            }
            else
//			else if(m_chat_person.person_smss.get(position).SMSType == 2)
            {
                convertView = LayoutInflater.from(ChatActivity.this).inflate(
                        R.layout.chat_list_item_send, null);

                tvContent = (TextView) convertView
                        .findViewById(R.id.tv_chatcontent);
                tvContent.setAutoLinkMask(Linkify.ALL);
                tvContent.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        Vibrator vib = (Vibrator)ChatActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                        vib.vibrate(50);

                        AlertDialog ListDialog = new AlertDialog.Builder(ChatActivity.this).
                                setTitle("信息选项").
                                setItems(new String[] { "转发","重发","删除"}, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            ForwardSMS(SmsContent);
                                        }
                                        else if(which == 1)
                                        {
                                            SendSMS(SmsContent, m_chat_person.Number, System.currentTimeMillis());
                                        }
                                        else if(which == 2)
                                        {
                                            DeleteSMS(SmsId, index);
                                        }
                                    }
                                }).
                                create();
                        ListDialog.show();
                        return false;
                    }
                });
            }

            TextView tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);

            Date date = new Date(m_chat_person.person_smss.get(position).SMSDate);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String time = sfd.format(date);
            tvSendTime.setText(time);
            tvContent.setText(m_chat_person.person_smss.get(position).SMSContent);
            return convertView;
        }
    }

    //转发信息
    void ForwardSMS(String Content)
    {
        Intent intent = new Intent(this, NewSmsActivity.class);
        intent.putExtra("ForwardSMS", Content);
        startActivity(intent);
        finish();
    }

    //发送信息
    void SendSMS(String Content,String Number,Long Date)
    {
        SMSs new_sm =  new SMSs();
        new_sm.SMSContent = Content;
        new_sm.SMSDate = Date;
        new_sm.SMSType = 2;

        Intent itSend = new Intent("SMS_SEND_ACTIOIN");
        Intent itDeliver = new Intent("SMS_DELIVERED_ACTION");
       
       /* sentIntent参数为传送后接受的广播信息PendingIntent */
        PendingIntent mSendPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itSend, 0);
       /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
        PendingIntent mDeliverPI = PendingIntent.getBroadcast(getApplicationContext(), 0, itDeliver, 0);

        SmsManager smsManager = SmsManager.getDefault();
        if(m_MsgEditText.getText().toString().length() > 70)
        {
            List<String> contents = smsManager.divideMessage(Content);
            for(String sms : contents)
            {
                smsManager.sendTextMessage(Number, null, sms, mSendPI, mDeliverPI);
            }
        }
        else
        {
            smsManager.sendTextMessage(Number, null, Content, mSendPI, mDeliverPI);
        }

        m_chat_person.person_smss.add(new_sm);
        new AddSmsTask(Number, new_sm.SMSContent, new_sm.SMSDate, new_sm.SMSType).execute();
        m_ChatLogAdapter.notifyDataSetChanged();
        m_MsgEditText.setText("");// 清空编辑框数据
        m_ChatLogList.setSelection(m_ChatLogList.getCount() - 1);
    }

    //删除信息
    void DeleteSMS(final int SmsId, final int index)
    {
        AlertDialog DeleteDialog = new AlertDialog.Builder(this).
                setTitle("删除信息").
                setMessage("删除此信息?").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteSmsTask(SmsId).execute();

                        m_chat_person.person_smss.remove(index);
                        m_ChatLogAdapter.notifyDataSetChanged();
                        m_ChatLogList.setSelection(m_ChatLogList.getCount() - 1);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                create();
        DeleteDialog.show();
    }
}
