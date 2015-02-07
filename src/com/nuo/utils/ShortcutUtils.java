package com.nuo.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.nuo.activity.R;

public class ShortcutUtils {
    /**

     * 返回添加到桌面快捷方式的Intent：

     * 1.给Intent指定action="com.android.launcher.INSTALL_SHORTCUT"

     * 2.给定义为Intent.EXTRA_SHORTCUT_INENT的Intent设置与安装时一致的action(必须要有)

     * 3.添加权限:com.android.launcher.permission.INSTALL_SHORTCUT

     */

    public static Intent getShortcutToDesktopIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, context.getClass());
        /*以下两句是为了在卸载应用的时候同时删除桌面快捷方式*/
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,context.getString(R.string.app_name));
        // 设置图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
        // 设置意图和快捷方式关联程序
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        return shortcut;

    }

    /**
     * 删除快捷方式
     * */
    public static void deleteShortCut(Context context)
    {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,context.getString(R.string.app_name));
        /**删除和创建需要对应才能找到快捷方式并成功删除**/
        Intent intent = new Intent();
        intent.setClass(context, context.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        context.sendBroadcast(shortcut);
    }

    /**
     * 判断是否已添加快捷方式：
     * 暂时没有方法能够准确的判断到快捷方式，原因是，
     1、不同厂商的机型他的快捷方式uri不同，我遇到过HTC的他的URI是content://com.htc.launcher.settings/favorites?notify=true
     2、桌面不只是android自带的，可能是第三方的桌面，他们的快捷方式uri都不同

     提供一个解决办法，创建快捷方式的时候保存到preference，或者建个文件在SD卡上，下次加载的时候判断不存在就先发删除广播，再重新创建

     * 添加权限:<uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" ></uses-permission>

     */
    public static boolean hasInstallShortcut(Context context) {
        boolean hasInstall = false;

        String AUTHORITY = "com.android.launcher.settings";
        int systemversion = Build.VERSION.SDK_INT;
        Log.i("Build.VERSION.SDK==========>", systemversion + "");
	     /*大于8的时候在com.android.launcher2.settings 里查询（未测试）*/
        if(systemversion >= 8){
            AUTHORITY = "com.android.launcher2.settings";
        }
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY  + "/favorites?notify=true");

        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                new String[] { "title" }, "title=?",
                new String[] { context.getString(R.string.app_name) }, null);

        if (cursor != null && cursor.getCount() > 0) {
            hasInstall = true;
        }

        return hasInstall;
    }
}
