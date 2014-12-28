package com.fujie.module.activity;

import android.app.Application;

/**
 * Created by zxl on 2014/11/7.
 */
public class BackApplication extends Application {
    private ActivityManager activityManager;

    public synchronized ActivityManager getActivityManager() {
        if (activityManager == null) {
            //初始化自定义Activity管理器
            activityManager = ActivityManager.getScreenManager();
        }
        return activityManager;
    }
}
