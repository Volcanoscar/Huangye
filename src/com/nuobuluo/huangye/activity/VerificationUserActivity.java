package com.nuobuluo.huangye.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.activity.AbstractTemplateActivity;
import com.lidroid.xutils.ViewUtils;
import com.nuobuluo.huangye.utils.T;

/**
 * Created by zxl on 2015/3/17.
 */
public class VerificationUserActivity extends AbstractTemplateActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_user);
        ViewUtils.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.huangye_action, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_plus) {
            T.showShort(this,"发送");
        }
        return true;
    }
}