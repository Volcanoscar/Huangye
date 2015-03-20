package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.R;

public class UserSearchResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend_result);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.query})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.query:
                Intent intent = new Intent(this, UserDetailActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
