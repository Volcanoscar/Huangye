package com.nuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by APPLE on 2014/11/29.
 */
public class FeedBackActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {

        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.feedback_title);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}