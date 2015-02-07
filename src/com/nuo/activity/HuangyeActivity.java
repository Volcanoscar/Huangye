package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 黄页模块
 */

public class HuangyeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huangye);
        ViewUtils.inject(this);

    }


    /**
     * 一级分类 点击事件
     * 一级分类包括：家政
     * <p/>
     * *
     */
    @OnClick({R.id.jiazheng,R.id.Search_outing})
    public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致
        switch (v.getId()) {
            case R.id.jiazheng:
                Intent intent = new Intent(HuangyeActivity.this,
                        HuangyeLevelTwoActivity.class);
                intent.putExtra("parentName",getResources().getString(R.string.jiazheng_name));
                HuangyeActivity.this.startActivity(intent);
                break;
            case R.id.Search_outing:
                 intent = new Intent(HuangyeActivity.this,
                        KeyActivity.class);
                HuangyeActivity.this.startActivity(intent);
                break;
        }
    }
}
