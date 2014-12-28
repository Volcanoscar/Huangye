package com.fujie.module.activity;

import android.app.Activity;
import android.os.Bundle;

public class AbstractTemplateActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackApplication application = (BackApplication) this.getApplication();
        application.getActivityManager().pushActivity(this);
     
    }
 
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BackApplication application = (BackApplication) getApplication();
        application.getActivityManager().popActivity(this);
    }
}