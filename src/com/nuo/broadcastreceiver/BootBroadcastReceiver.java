package com.nuo.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nuo.activity.FrameActivity;
import com.nuo.utils.T;


public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)) {
           Intent sayHelloIntent=new Intent(context,FrameActivity.class);
           sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(sayHelloIntent);
            T.showShort(context, "诺部落已经启动");
        }
    }
}