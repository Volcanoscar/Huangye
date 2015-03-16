package com.nuo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.fujie.module.editview.ClearEditText;
import com.fujie.module.titlebar.TitleBarView;
import com.nuo.utils.T;

public class AddFriendActivity extends Activity{

    private Context mContext;
    private View mBaseView;
    private TitleBarView mTitleBarView;
    private ClearEditText clearEditText;
    private Button searchBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        findView();
        initView();
    }

    private void initView() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchBtn.getText())) {
                    T.showShort(AddFriendActivity.this, R.string.null_account_prompt);
                }else {

                }
            }
        });
    }

    private void findView() {
        clearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        searchBtn = (Button) findViewById(R.id.query);
    }
}

