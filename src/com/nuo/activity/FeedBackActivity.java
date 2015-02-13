package com.nuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.bean.FeedBack;
import com.nuo.bean.UserInfo;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.NetUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.PreferenceUtils;
import com.nuo.utils.T;
import com.nuo.utils.XutilHelper;

/**
 * Created by APPLE on 2014/11/29.
 */
public class FeedBackActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
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
        initView();
    }

    private void initData() {
        db = XutilHelper.getDB(this);
    }

    private void initView() {

        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.feedback_title);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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