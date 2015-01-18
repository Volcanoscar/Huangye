package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nuo.adapter.SmsCursor;
import com.nuo.cursor.ContactsCursor;

/**
 * Created by zxl on 14-8-16.
 */
public class ContactsDetailActivity extends Activity implements View.OnClickListener {

    private ImageButton imageBtn;
    private TextView telTextView;
    private TextView deviceNameTextView;
    private ImageButton backBtn;
    private String number;
    private SmsCursor.Person_Sms person_sms;
    private ContactsCursor.SortEntry contact;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.contacts_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("person");
        if(bundle.getBoolean("hasSms")) {
            person_sms = (SmsCursor.Person_Sms)bundle.getSerializable("sms");
        }
        contact =(ContactsCursor.SortEntry) bundle.getSerializable("contact");
        backBtn = (ImageButton)findViewById(R.id.IB_back);
        deviceNameTextView = (TextView)findViewById(R.id.deviceName);
        telTextView = (TextView)findViewById(R.id.telTextView);
        imageBtn = (ImageButton) findViewById(R.id.imageButton);
        number =bundle.getString("number");
        deviceNameTextView.setText(bundle.getString("name"));
        telTextView.setText(number);
        imageBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        telTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(R.id.IB_back==view.getId()){
            finish();
        }else if(R.id.telTextView==view.getId()){
            Intent  intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + number));
            startActivity(intent);
        }else if(R.id.imageButton==view.getId()){
            Intent intent = new Intent(ContactsDetailActivity.this,ChatActivity.class);
            Bundle mBundle = new Bundle();
            int index = -1;
            if(person_sms==null){
                Intent newSmsIntent = new Intent(ContactsDetailActivity.this, NewSmsActivity.class);
                newSmsIntent.putExtra("contact", contact);
                startActivity(newSmsIntent);
              /*  Intent smsIntent = new Intent("android.huahua.BatchSendSms");
                Bundle smsMBundle = new Bundle();
                ArrayList<ContactsCursor.SortEntry> mSortList = new ArrayList<ContactsCursor.SortEntry>();
                mSortList.add(contact);
                smsMBundle.putSerializable("BatchSendSms", mSortList);
                smsIntent.putExtras(mBundle);
                sendBroadcast(smsIntent);*/
            }else{
                mBundle.putSerializable("chatperson",person_sms);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        }
    }
}