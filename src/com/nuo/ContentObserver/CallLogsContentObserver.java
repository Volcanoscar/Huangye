package com.nuo.ContentObserver;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class CallLogsContentObserver extends ContentObserver{

    public CallLogsContentObserver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i("huahua", "通话记录数据库发生了变化");
    }

}
