package com.nuo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class NetUtil {
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
//			String name = info.getTypeName();
//			L.i("当前网络名称：" + name);
			isNetConnected = true;
		} else {
			L.i("没有可用网络");
			isNetConnected = false;
		}
		return isNetConnected;
	}

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    public static boolean checkPhone(String phone,RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone", phone);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://"+PreferenceConstants.DEFAULT_SERVER_HOST+"/mapi/reglogin/checkPhone",
                params,
                requestCallBack);
        return false;
    }

    public static String  addParam(String path,Map<String,String> paramMap) {
        String realPath = path;
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (realPath.indexOf("?") == -1) {
                realPath += "?" + entry.getKey() + "=" + entry.getValue();
            }else{
                realPath += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        return realPath;
    }

    public static void login(String s, String s1, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("key", s);
        params.addQueryStringParameter("password", s1);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://"+PreferenceConstants.DEFAULT_SERVER_HOST+"/mapi/reglogin/login",
                params,
                requestCallBack);

    }
}
