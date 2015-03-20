package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuobuluo.huangye.adapter.ContactDetailAdapter;
import com.nuobuluo.huangye.adapter.SmsCursor;
import com.nuobuluo.huangye.cursor.ContactsCursor;

/**
 * Created by zxl on 14-8-16.
 */
public class ContactsDetailActivity extends Activity{

    private TextView deviceNameTextView;
    private SmsCursor.Person_Sms person_sms;
    private ContactsCursor.SortEntry contact;
    private ListView m_contactslist;
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.contacts_detail);
        ViewUtils.inject(this);
        initTitleView();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("person");
        if(bundle.getBoolean("hasSms")) {//根据是否有短信，跳转到不同的界面
            person_sms = (SmsCursor.Person_Sms)bundle.getSerializable("sms");
        }
        contact =(ContactsCursor.SortEntry) bundle.getSerializable("contact");

        deviceNameTextView = (TextView)findViewById(R.id.deviceName);

        deviceNameTextView.setText(bundle.getString("name"));

        m_contactslist = (ListView)findViewById(R.id.contact_list);
        m_contactslist.setAdapter(new ContactDetailAdapter(ContactsDetailActivity.this,contact));
        //点击进入详情
        m_contactslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long l) {

            }
        });
    }
    private void initTitleView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.contact_detail);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}