package com.nuo.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.adapter.SmsCursor.SMSs;
import com.nuo.cursor.ContactsCursor.SortEntry;
import com.nuo.task.AddSmsTask;

import java.util.ArrayList;
import java.util.List;

public class NewSmsActivity extends Activity implements OnClickListener{
    //标题文本
    private TextView m_NewSmsTV;
    //发送信息的按钮
    private Button m_SendBtn;
    //返回按钮
    private ImageButton m_BackIB;
    //编辑信息框
    private EditText m_MsgEditText;
    //选择收件人EditText
    private EditText m_SelectEditText;
    //选择收件人ImageButton
    private ImageButton m_SelectImageButton;
    //群发联系人数组
    private ArrayList<SortEntry> m_BatchSmsList= new ArrayList<SortEntry>();
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.newsms_activtity);
        ViewUtils.inject(this);
        initTitleView();
        m_SendBtn = (Button) findViewById(R.id.btn_chat_send);
        m_SendBtn.setOnClickListener(this);
        m_MsgEditText = (EditText) findViewById(R.id.et_chat_msg);
        m_MsgEditText.setText(getIntent().getStringExtra("ForwardSMS"));
        m_SelectEditText = (EditText)findViewById(R.id.chatname_edit);
        m_SelectImageButton = (ImageButton)findViewById(R.id.selectcontacts_im);
        m_SelectImageButton.setOnClickListener(this);
        Intent intent = getIntent();
        SortEntry sortEntry = (SortEntry)intent.getSerializableExtra("contact");
        if(sortEntry!=null){
            m_BatchSmsList.add(sortEntry);
            m_SelectEditText.setText(sortEntry.mName);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.huahua.BatchSendSms");
        registerReceiver(mReceiver , filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.huahua.BatchSendSms")) {
                m_BatchSmsList= (ArrayList<SortEntry>) intent.getSerializableExtra("BatchSendSms");
                if (m_BatchSmsList != null) {
                    String str = null;
                    for (int i = 0; i < m_BatchSmsList.size(); i++) {
                        if (str != null) {
                            str = str + " ," + m_BatchSmsList.get(i).mName;
                        } else {
                            str = m_BatchSmsList.get(i).mName;
                        }
                    }
                    m_SelectEditText.setText(str);
                }
            }
        }
    };
    private void initTitleView(){
        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.add_msg);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
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
                if(m_BatchSmsList != null)
                {
                    ArrayList<ContentProviderOperation> ops =
                            new ArrayList<ContentProviderOperation>();

                    for(int i=0;i<m_BatchSmsList.size();i++)
                    {
                        SMSs new_sm =  new SMSs();
                        new_sm.SMSContent = m_MsgEditText.getText().toString();
                        new_sm.SMSDate = System.currentTimeMillis();
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
                            List<String> contents = smsManager.divideMessage(m_MsgEditText.getText().toString());
                            for(String sms : contents)
                            {
                                smsManager.sendTextMessage(m_BatchSmsList.get(i).mNum, null, sms, mSendPI, mDeliverPI);
                            }
                        }
                        else
                        {
                            smsManager.sendTextMessage(m_BatchSmsList.get(i).mNum, null, m_MsgEditText.getText().toString(), mSendPI, mDeliverPI);
                        }

                        //增加新信息到短信数据库,放在线程中处理
                        new AddSmsTask(m_BatchSmsList.get(i).mNum, new_sm.SMSContent, new_sm.SMSDate, new_sm.SMSType).execute();
                    }

                    finish();
                }
            }
        }
        else if(v.getId() == R.id.selectcontacts_im)
        {
            Intent intent = new Intent(this, BatchSendSmsActivity.class);
            if(m_BatchSmsList != null && m_BatchSmsList.size()>0)
            {
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("BatchSendSms", m_BatchSmsList);
                intent.putExtras(mBundle);
            }
            startActivity(intent);
        }
    }

}
