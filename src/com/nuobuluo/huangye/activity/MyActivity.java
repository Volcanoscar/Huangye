package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.bean.UserInfo;
import com.nuobuluo.huangye.common.DownLoadManager;
import com.nuobuluo.huangye.utils.PreferenceConstants;
import com.nuobuluo.huangye.utils.PreferenceUtils;
import com.nuobuluo.huangye.utils.T;
import com.nuobuluo.huangye.utils.XutilHelper;

/**
 * 我的模块
 */

public class MyActivity extends Activity {

    @ViewInject(R.id.user_name)
    TextView userName;

    @ViewInject(R.id.userLayout)
    RelativeLayout userLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ViewUtils.inject(this); //注入view和事件
        initView();
    }

    private void initView() {
        DbUtils db = XutilHelper.getDB(this);
        //判断用户是否已经登录过 //如果密码已经保存，则不用登录
        Integer accountId = PreferenceUtils.getPrefInt(MyActivity.this,
                PreferenceConstants.ACCOUNT_ID, -1);
        if (accountId!=-1) {  //进入个人中心
            try {
                UserInfo userInfo = db.findById(UserInfo.class,accountId);
                if (userInfo != null) {
                    userName.setText(userInfo.getUserName());
                }
                userLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent intent = new Intent(MyActivity.this,UserCenterActivity.class);
                        startActivity(intent);
                    }
                });
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            userName.setText(R.string.login_tip);
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(MyActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick({R.id.feed_back, R.id.about, R.id.update_verion, R.id.help})
    public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致

        switch (v.getId()) {
            case R.id.feed_back:
                Intent intent = new Intent(MyActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(MyActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.update_verion:
                //检测版本
                DownLoadManager.checkVersion(MyActivity.this, true);
                break;
            case R.id.help:
                T.showShort(MyActivity.this, "帮助");
                break;
        }
    }
}
