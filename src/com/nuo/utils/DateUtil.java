package com.nuo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxl on 2015/1/29.
 */
public class DateUtil {
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 日期转换成字符串
     * @param date
     * @return
     */
    public  static String dateToStr(Date date) {
        if(date==null) return "";
        return df.format(date);
    }
}
