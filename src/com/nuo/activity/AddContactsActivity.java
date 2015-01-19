package com.nuo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.utils.Utils;

public class AddContactsActivity extends Activity{
    private EditText m_EditName;
    private EditText m_EditNum;
    private String m_ContactId;
    private int m_Type;

    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;

    ProgressDialog m_dialogLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_contacts);
        ViewUtils.inject(this);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("person");

        m_EditName = (EditText)findViewById(R.id.edit_name);
        m_EditNum = (EditText)findViewById(R.id.edit_num);

        m_Type = bundle.getInt("tpye");
        m_EditName.setText(bundle.getString("name"));
        m_EditNum.setText(bundle.getString("number"));
    }

    private void initView() {

        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        if(m_Type == 0)//新增联系人
        {
            mTitleBarView.setTitleText(R.string.add_contract);
        }
        else if(m_Type == 1)//编辑联系人
        {
            mTitleBarView.setTitleText(R.string.edit_contract);
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("person");
            m_ContactId = bundle.getString("id");
        }

        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBarView.getBtnRight().setPadding(8, 0, 8, 0);
        mTitleBarView.getBtnRight().setTextColor(getResources().getColor(R.color.white));
        mTitleBarView.setBtnRightText(R.string.save);
        mTitleBarView.setBtnRightBg(R.drawable.login_btn);
        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(m_EditName.getText().toString()))
                {
                    Toast.makeText(AddContactsActivity.this, "请输入联系人姓名", Toast.LENGTH_SHORT).show();
                }
                else if("".equals(m_EditNum.getText().toString()))
                {
                    Toast.makeText(AddContactsActivity.this, "请输入联系人电话", Toast.LENGTH_SHORT).show();
                }
                else if(m_Type == 0)
                {
                    //新增联系人操作,放在线程中处理
                    new SaveContactTask().execute();
                }
                else if(m_Type == 1)
                {
                    //更新联系人操作,放在线程中处理
                    new ChangeContactTask().execute();
                }
            }
        });
    }

    class  SaveContactTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            Utils.AddContact(m_EditName.getText().toString(), m_EditNum.getText().toString());

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(m_dialogLoading!= null)
            {
                m_dialogLoading.dismiss();
                finish();
            }
        }
        @Override
        protected void onPreExecute() {
            m_dialogLoading = new ProgressDialog(AddContactsActivity.this);
            m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            m_dialogLoading.setMessage("正在保存联系人");
            m_dialogLoading.setCancelable(false);
            m_dialogLoading.show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }

    }

    class  ChangeContactTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Utils.ChangeContact(m_EditName.getText().toString(), m_EditNum.getText().toString(),m_ContactId);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(m_dialogLoading!= null)
            {
                m_dialogLoading.dismiss();
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
            m_dialogLoading = new ProgressDialog(AddContactsActivity.this);
            m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            m_dialogLoading.setMessage("正在保存联系人");
            m_dialogLoading.setCancelable(false);
            m_dialogLoading.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
