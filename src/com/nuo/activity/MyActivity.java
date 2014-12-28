package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.common.DownLoadManager;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.PreferenceUtils;
import com.nuo.utils.T;

/**
 * 我的模块
 */

public class MyActivity extends Activity {

    @ViewInject(R.id.user_name)
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ViewUtils.inject(this); //注入view和事件
        initView();

    }

    private void initView() {

        //判断用户是否已经登录过
        //如果密码已经保存，则不用登录
        String password = PreferenceUtils.getPrefString(MyActivity.this,
                PreferenceConstants.PASSWORD, "");
        if (!TextUtils.isEmpty(password)) {  //进入个人中心
           /* startActivity(new Intent(MyActivity.this, MainActivity.class));
            finish();*/
        } else {
            userName.setText(R.string.login_tip);
            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
