package com.nuobuluo.huangye.activity;

import android.os.Bundle;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.activity.AbstractTemplateActivity;
import com.lidroid.xutils.ViewUtils;

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