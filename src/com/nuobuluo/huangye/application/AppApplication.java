package com.nuobuluo.huangye.application;

import android.content.Intent;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.activity.FrameActivity;
import com.nuobuluo.huangye.activity.amap.TTSController;
import com.nuobuluo.huangye.db.SQLHelper;
import com.nuobuluo.huangye.utils.T;
import com.nuobuluo.module.activity.BackApplication;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class AppApplication extends BackApplication /*implements
        Thread.UncaughtExceptionHandle*/{
    private static AppApplication mAppApplication;
    private SQLHelper sqlHelper;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mAppApplication = this;
        initImageLoader();
        //设置Thread Exception Handler
        //Thread.setDefaultUncaughtExceptionHandler(this);
        SpeechUtility.createUtility(AppApplication.this, "appid=54bf0f2e");
        AMapNavi.getInstance(this).setAMapNaviListener(TTSController.getInstance(this));// 设置语音模块播报
        RongIM.init(this);
        // 此处直接 hardcode 给 token 赋值，请替换为您自己的 Token。
        String token = "+A5klljdBR1Af7TIYF7PbK1rqs21B6wEhXuy799oRhr/cu7rKR7JRdZkiHykUqzN0uBHR23Idf+j5uKRa03YDA==";
        // 连接融云服务器。
        try {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    Log.d("Connect:", "Login successfully.");
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    Log.d("Connect:", "Login failed.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * uncaughtException方法中处理异常，这里我们关闭App并启动我们需要的Activity
     * @param thread
     * @param ex
     */
    //@Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(this,FrameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        T.showShort(this, R.string.system_error);
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
