package com.nuobuluo.huangye.ContentObserver;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class SmssContentObserver extends ContentObserver{

    public SmssContentObserver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i("huahua", "短信息数据库发生了变化");
    }

}
