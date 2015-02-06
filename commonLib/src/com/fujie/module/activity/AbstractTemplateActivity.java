package com.fujie.module.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.lidroid.xutils.ViewUtils;

public class AbstractTemplateActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackApplication application = (BackApplication) this.getApplication();
        application.getActivityManager().pushActivity(this);
        ViewUtils.inject(this);
        initActionBar();
     
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
        actionBar.setDisplayHomeAsUpEnabled(true);  //添加反馈按键
    }
 
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BackApplication application = (BackApplication) getApplication();
        application.getActivityManager().popActivity(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}