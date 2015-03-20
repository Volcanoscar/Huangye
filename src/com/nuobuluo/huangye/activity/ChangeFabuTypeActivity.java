package com.nuobuluo.huangye.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.R;

/**
 * Created by zxl on 2014/11/26.
 */
public class ChangeFabuTypeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_fabu_type);
        ViewUtils.inject(this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
    }

    /**
     * 一级分类 点击事件
     * 一级分类包括：家政
     * <p/>
     */
    @OnClick({R.id.jiazheng})
    public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致
        switch (v.getId()) {
            case R.id.jiazheng:
                Intent intent = new Intent(ChangeFabuTypeActivity.this,
                        HuangyeLevelTwoActivity.class);
                intent.putExtra("toView", "fabu");
                intent.putExtra("parentName",getResources().getString(R.string.jiazheng_name));
                startActivity(intent);
                break;
        }
    }
}