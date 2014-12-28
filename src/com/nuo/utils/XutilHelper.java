package com.nuo.utils;

import android.content.Context;
import com.lidroid.xutils.DbUtils;

/**
 * Created by Administrator on 2014/11/2.
 */
public class XutilHelper {

    public static DbUtils getDB(Context context) {
        DbUtils db=  DbUtils.create(context);
        db.configDebug(true);
        return db;

    }

}
