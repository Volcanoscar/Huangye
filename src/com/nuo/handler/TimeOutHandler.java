package com.nuo.handler;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import com.nuo.activity.R;
import com.nuo.utils.DialogUtil;
import com.nuo.utils.T;

/**
 * Created by zxl on 14-10-17.
 * ��¼��ʱ
 */
public class TimeOutHandler extends Handler {
    private Activity context;
    private Dialog mDialog;
    private ConnectionOutTimeProcess connectionOutTimeProcess;
    public TimeOutHandler(Activity context) {
        this.context= context;
    }
    public static final int LOGIN_OUT_TIME = 0;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LOGIN_OUT_TIME:
                stopProcess();
                closeTip();
                T.showShort(context, R.string.timeout_try_again);
                break;
            default:
                break;
        }
    }
    public void start(Integer str){
        startProcess();
        showTip(str);
    }
    public void stop(){
        stopProcess();
        closeTip();
    }

    private void closeTip() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    private void showTip(Integer str){
        if(str==null){
            str=R.string.default_tip;
        }
        if (mDialog == null){
            mDialog = DialogUtil.getLoginDialog(context, str);
        }
        mDialog.show();


    }
    private void startProcess(){
        if(connectionOutTimeProcess==null) {
            connectionOutTimeProcess = new ConnectionOutTimeProcess(this);
        }
        if(!connectionOutTimeProcess.running){
            connectionOutTimeProcess.start();
        }
    }
    private void stopProcess(){
        if (connectionOutTimeProcess != null && connectionOutTimeProcess.running) {
            connectionOutTimeProcess.stop();
            connectionOutTimeProcess = null;
        }
    }
}
