package com.nuo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.fujie.module.horizontalListView.ViewBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nuo.bean.MsgSearchBean;
import com.nuo.bean.Publish;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与网络和请求接口数据相关的类
 */
public class NetUtil {
    /**
     * 检测手机是否联网*
     */
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

    /**
     * 手机联网状态:未联网*
     */
    public static final int NETWORN_NONE = 0;
    /**
     * 手机联网状态:WIFI*
     */
    public static final int NETWORN_WIFI = 1;
    /**
     * 手机联网状态:手机流量*
     */
    public static final int NETWORN_MOBILE = 2;

    /**
     * 得到手机网络状态
     *
     * @param context
     * @return
     */
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

    /**
     * 关键字搜索（搜标题）。
     * 参数：
     * k - 关键字 not null
     * 返回：统计二级分类下搜索到的数量：
     * [
     * {
     * typeCode：''
     * level1Code：''
     * typeName：''
     * count:2222
     * }
     * ...
     * ]
     *
     * @param key             关键字
     * @param requestCallBack 请求接口后响应的回调函数
     * @return
     */
    public static void searchMsgCount(String key, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("k", key);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/fbxx/search",
                params,
                requestCallBack);
    }

    /**
     * http://121.40.208.124/mapi/fbxx/serviceSearch/zuche::type1::type2::typeN? 其中zuche是二级分类代码要方第一个位置，后面的type1...typeN是你选择的其他分类过滤条件
     * 例如：serviceSearch/jiaxiao::jiaxiaozhaosheng::banbie-quanzhou::jiazhao-c1?d=&a=&k=&p=1&today=0&img=0    type1...typeN无顺序限制
     * @param msg
     * @param viewBeanList
     * @param requestCallBack
     */
    public static void searchMsg(MsgSearchBean msg, List<ViewBean> viewBeanList, RequestCallBack<String> requestCallBack) {
        if (msg.getLevelTwoTypeCode() == null ||  msg.getLevelTwoTypeCode().isEmpty()) {
            requestCallBack.onFailure(null, null);
        }
        StringBuffer url = new StringBuffer("http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/fbxx/serviceSearch/" + msg.getLevelTwoTypeCode());
        if (viewBeanList != null && !viewBeanList.isEmpty()) {
            for (ViewBean viewBean : viewBeanList) {
                if (viewBean.getId() != null&&!viewBean.getId().isEmpty()) {
                    url.append("::").append(viewBean.getId());
                }
            }
        }
        RequestParams params = new RequestParams();
        for (Map.Entry<String, String> entity : msg.toMap().entrySet()) {
            params.addQueryStringParameter(entity.getKey(), entity.getValue());
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url.toString(),
                params,
                requestCallBack);
    }

    /**
     * 请求 校验手机号是否注册接口，return：true|false
     *
     * @param phone           电话号码
     * @param requestCallBack 请求接口后响应的回调函数
     * @return
     */
    public static boolean checkPhone(String phone, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone", phone);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/reglogin/checkPhone",
                params,
                requestCallBack);
        return false;
    }

    /**
     * 请求登录接口
     *
     * @param s               用户名
     * @param s1              密码
     * @param requestCallBack 请求接口后响应的回调函数
     */
    public static void login(String s, String s1, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("key", s);
        params.addQueryStringParameter("password", s1);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/reglogin/login",
                params,
                requestCallBack);

    }

    /**
     * 请求查询分类接口
     * 接口说明如下：
     * <p/>
     * 查询分类，当查询3级别分类时，如果有4级分类一起返回（type4List）<br></br>
     * [<br></br>
     * {<br></br>
     * "typeName": "分类名称",<br></br>
     * "level": 1,//级别<br></br>
     * "orderBy": 2,排序<br></br>
     * "level1Code": "",//所属1级分类code<br></br>
     * "level2Code": "",//所属2级分类code<br></br>
     * "isParent": 1,//是否是父节点<br></br>
     * "typeCode": "jiazhengfuwu",//分类编码<br></br>
     * "level3Code": ""//所属3级分类code<br></br>
     * "level4List":[4级分类列表] },<br></br>
     * ...<br></br>
     * ] <br></br>
     *
     * @param level    分类级别
     * @param typeCode 分类代码，查询一级分类时不需要
     */
    public static void shenhuoType(String level, String typeCode, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("level", level);
        params.addQueryStringParameter("typeCode", typeCode);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/shenghuoType/queryType",
                params,
                requestCallBack);
    }

    /**
     * @param hasBizarea      是否包含区域下的商圈列表
     * @param requestCallBack 回调函数
     */
    public static void queryDistrictList(boolean hasBizarea, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("hasBizarea", String.valueOf(hasBizarea));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/district/queryDistrictList",
                params,
                requestCallBack);
    }

    public static void getShopInfo(String accountId, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("dianpuId",accountId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/dianpu/info",
                params,
                requestCallBack);
    }

    /**
     * 信息发布接口
     * @param publish
     * @param requestCallBack
     */
    public static void publish(Publish publish, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("data",Publish.toJson(publish));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/fbxx/save",
                params,
                requestCallBack);
    }

    public static void uploadImg(List<String> imgAddList, RequestCallBack<String> requestCallBack) {
        RequestParams params = new RequestParams();
        for (String url : imgAddList) {
            params.addBodyParameter("file", new File(url));
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                "http://" + PreferenceConstants.DEFAULT_SERVER_HOST + "/mapi/fbxx/save",
                params,
                requestCallBack);
    }
}
