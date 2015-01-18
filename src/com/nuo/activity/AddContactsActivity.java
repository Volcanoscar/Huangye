package com.nuo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.nuo.utils.Utils;

public class AddContactsActivity extends Activity{
    private Button m_SaveBtn;
    private EditText m_EditName;
    private EditText m_EditNum;
    private TextView m_TextTitle;
    private String m_ContactId;
    private ImageButton m_BackIB;
    private int m_Type;

    ProgressDialog m_dialogLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_contacts);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("person");

        m_TextTitle = (TextView)findViewById(R.id.text_title);
        m_EditName = (EditText)findViewById(R.id.edit_name);
        m_EditNum = (EditText)findViewById(R.id.edit_num);

        m_Type = bundle.getInt("tpye");
        m_EditName.setText(bundle.getString("name"));
        m_EditNum.setText(bundle.getString("number"));

        if(m_Type == 0)//新增联系人
        {
            m_TextTitle.setText("新增联系人");
        }
        else if(m_Type == 1)//编辑联系人
        {
            m_ContactId = bundle.getString("id");
            m_TextTitle.setText("编辑联系人");
        }

        m_SaveBtn = (Button)findViewById(R.id.btn_save_contact);
        m_SaveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

        m_BackIB = (ImageButton)findViewById(R.id.btn_add_back);
        m_BackIB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();

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
