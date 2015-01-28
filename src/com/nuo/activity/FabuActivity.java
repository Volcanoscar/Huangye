package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zxl on 2014/11/26.
 */
public class FabuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
    }


}