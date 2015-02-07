package com.nuo.application;

import android.app.Application;
import android.content.Intent;

import com.fujie.module.activity.BackApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nuo.activity.FrameActivity;
import com.nuo.activity.R;
import com.nuo.db.SQLHelper;
import com.nuo.utils.T;

public class AppApplication extends BackApplication implements
        Thread.UncaughtExceptionHandler{
    private static AppApplication mAppApplication;
    private SQLHelper sqlHelper;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mAppApplication = this;
        initImageLoader();
        //设置Thread Exception Handler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException方法中处理异常，这里我们关闭App并启动我们需要的Activity
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(this,FrameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        T.showShort(this,R.string.system_error);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    /** 获取Application */
    public static AppApplication getApp() {
        return mAppApplication;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    /** 摧毁应用进程时候调用 */
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }

    public void clearAppCache() {
    }
}
