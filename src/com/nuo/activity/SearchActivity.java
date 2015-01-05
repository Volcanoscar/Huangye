package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.common.DownLoadManager;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.NetUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 搜索模块
 */

public class SearchActivity extends Activity {

    // 导航条linearlayout作为按钮
    /*private LinearLayout mSearch_city, mSearch_search*/;
    // gridview样式linearlayout作为按钮
    private LinearLayout mSearch_food, mSearch_outing, mSearch_hotel,
            mSearch_pub, mSearch_more, mSearch_chinsesnack;
    // listview样式linearlayout作为按钮
    private LinearLayout mSearch_list_huiyuanka, mSearch_list_souquancheng,
            mSearch_list_paihangbang, mSearch_list_youhuiquan;

    //一级分类布局对象
    @ViewInject(R.id.oneTypeTableLayout)
    private TableLayout oneTypeTableLayout;
    @ViewInject(R.id.jiazheng)
    private TableLayout jiazheng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
    }

    /**
     * 一级分类 点击事件
     * 一级分类包括：家政
     * <p/>
     * *
     */
    @OnClick({R.id.jiazheng})
    public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致
        switch (v.getId()) {
            case R.id.jiazheng:
                Intent intent = new Intent(SearchActivity.this,
                        SearchTheCity.class);
                intent.putExtra("parentName",getResources().getString(R.string.jiazheng_name));
                SearchActivity.this.startActivity(intent);
                break;
        }
    }
}
