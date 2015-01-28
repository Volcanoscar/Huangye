package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.bean.UserInfo;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.PreferenceUtils;
import com.nuo.utils.XutilHelper;

/**
 * Created by APPLE on 2014/12/18.
 */
public class UserCenterActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
    @ViewInject(R.id.nameTextView)
    private TextView nameTextView;
    @ViewInject(R.id.telTextView)
    private TextView telTextView;
    @ViewInject(R.id.codeTextView)
    private TextView codeTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ViewUtils.inject(UserCenterActivity.this); //注入view和事件
        initView();
        initData();
    }

    private void initData() {
        DbUtils db = XutilHelper.getDB(this);
        Integer accountId = PreferenceUtils.getPrefInt(UserCenterActivity.this,
                PreferenceConstants.ACCOUNT_ID, -1);
        try {
            if (accountId != -1) {
                UserInfo userInfo = db.findById(UserInfo.class, accountId);
                if (userInfo != null) {
                    codeTextView.setText(userInfo.getUserId());
                    telTextView.setText(userInfo.getPhone());
                    nameTextView.setText(userInfo.getUserName());
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.user_msg);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @OnClick({R.id.logout_btn})
    public void onClick(View v){
        PreferenceUtils.removeUserInfo(UserCenterActivity.this);
        finish();
        Intent intent = new Intent(UserCenterActivity.this, MyActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(UserCenterActivity.this, MyActivity.class);
        startActivity(intent);
    }
}