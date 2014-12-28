package com.nuo.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadManager {
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    class CheckVersionTask implements Runnable {
        InputStream is;
        public void run() {
            try {
                String path = URL_SERVER;
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                if (info.getVersion().equals(localVersion)) {
                    Log.i(activity.getClass().getName(), "版本号相同");
                    Message msg = new Message();
                    msg.what = UPDATA_NONEED;
                    handler.sendMessage(msg);
                    // LoginMain();
                } else {
                    Log.i(activity.getClass().getName(), "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
                    if (showMsg) {
                        Toast.makeText(activity.getApplicationContext(), "已是最新版本",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(activity.getApplicationContext(), "获取服务器更新信息失败", 1).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(activity.getApplicationContext(), "下载新版本失败", 1).show();
                    break;
            }
        }
    };
    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     *  1.创建alertDialog的builder.
     *  2.要给builder设置属性, 对话框的内容,样式,按钮
     *  3.通过builder 创建一个对话框
     *  4.对话框show()出来
     */
    protected   void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(activity);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装   ?
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(activity.getClass().getName(), "下载apk,更新");
                downLoadApk();
            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //do sth
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }
    /*
     * 从服务器中下载APK
     */
    protected  void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载最新版本...");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
                    sleep(5000);
                    pd.dismiss(); //结束掉进度条对话框
                    installApk(file);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }

    //安装apk
    protected  void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    private static String getVersionName(Activity activity) throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = activity.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
        return packInfo.versionName;
    }

    public  void  updateVersion(Activity activity) {
        this.activity = activity;
        try {
            localVersion = getVersionName(activity);
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    private static UpdataInfo info;
    private static String localVersion;
    private Activity activity;
    private boolean showMsg;

    public boolean isShowMsg() {
        return showMsg;
    }

    public void setShowMsg(boolean showMsg) {
        this.showMsg = showMsg;
    }

    private String URL_SERVER ="http://121.40.208.124/huangye/version.xml";
    //private String URL_SERVER ="http://192.168.24.250:8080/version.xml";

    /**
     *
     * @param context
     * @param showMsg 如果是最新版本是否Toast提示。
     */
    public static void checkVersion(Activity context,boolean showMsg) {
        DownLoadManager downLoadManager = new DownLoadManager();
        downLoadManager.setShowMsg(showMsg);
        downLoadManager.updateVersion(context);
    }
}