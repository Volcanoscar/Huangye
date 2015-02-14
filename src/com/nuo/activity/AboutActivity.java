package com.nuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 14-12-17.
 */
public class AboutActivity extends AbstractTemplateActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ViewUtils.inject(this);
    }
}