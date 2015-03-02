package com.nuo.utils;

/**
 * Created by APPLE on 2015/3/2.
 */
public class StringUtils {

    public static String delete(String str, int start, int end) {
        if (str == null || start < 0 || end < 0 || start > str.length() || end > str.length()) {
            return str;
        }
        return str.substring(0, end-1) + str.substring(end);
    }

    public static String batchDelete(String str, int start, int end) {
        if (str == null || start < 0 || end < 0 || start > str.length() || end > str.length()) {
            return str;
        }
        return str.substring(0, start) + str.substring(end+1);
    }

    public static void main(String[] args) {
        System.out.print(delete("adb2efg",5,6));
    }
}
