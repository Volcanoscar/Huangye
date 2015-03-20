package com.nuobuluo.huangye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.activity.AbstractTemplateActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.bean.FeedBack;
import com.nuobuluo.huangye.bean.UserInfo;
import com.nuobuluo.huangye.handler.TimeOutHandler;
import com.nuobuluo.huangye.utils.NetUtil;
import com.nuobuluo.huangye.utils.PreferenceConstants;
import com.nuobuluo.huangye.utils.PreferenceUtils;
import com.nuobuluo.huangye.utils.T;
import com.nuobuluo.huangye.utils.XutilHelper;

/**
 * Created by APPLE on 2014/11/29.
 */
public class FeedBackActivity extends AbstractTemplateActivity {
    @ViewInject(R.id.content)
    private EditText contentEditText;
    @ViewInject(R.id.contact)
    private EditText contactEditText;
    private DbUtils db;
    private TimeOutHandler timeOutHandler= new TimeOutHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ViewUtils.inject(this);
        initData();
    }

    private void initData() {
        db = XutilHelper.getDB(this);
    }

    @OnClick(R.id.FeedbackButton)
    public void click(View view) {
        //验证
        String content = contentEditText.getText().toString();
        String contact = contactEditText.getText().toString();
        if (content.trim().equals("")) {
            T.showShort(this, "内容不能为空");
        }
        FeedBack feedBack = new FeedBack();
        feedBack.setContent(content);
        feedBack.setContact(contact);
        Integer accountId = PreferenceUtils.getPrefInt(FeedBackActivity.this,
                PreferenceConstants.ACCOUNT_ID, -1);
        try {
            if (accountId != -1) {
                UserInfo userInfo = db.findById(UserInfo.class, accountId);
                if (userInfo != null) {
                    feedBack.setUserId(userInfo.getUserId());
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        NetUtil.feedBack(feedBack,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                timeOutHandler.stop();
                T.showShort(FeedBackActivity.this,"反馈成功，谢谢您的提议");
            }

            @Override
            public void onFailure(HttpException e, String s) {
                timeOutHandler.stop();
                T.showShort(FeedBackActivity.this, R.string.net_error);
            }
            @Override
            public void onStart() {
                super.onStart();
                timeOutHandler.start(null);
            }
        });

    }
}