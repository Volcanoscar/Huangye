package com.nuobuluo.huangye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.activity.AbstractTemplateActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AddFriendActivity extends AbstractTemplateActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.query,R.id.scan_layout})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.query:
                Intent intent = new Intent(AddFriendActivity.this, UserSearchResultActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.scan_layout:
                Intent scanIntent = new Intent(AddFriendActivity.this, ErcodeScanActivity.class);
                startActivity(scanIntent);
                break;
        }
    }
}



