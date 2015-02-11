package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zxl on 2015/2/10.
 */
public class CenterActivity extends Activity {

    @ViewInject(R.id.notebookLayout)
    private LinearLayout notebookLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ViewUtils.inject(CenterActivity.this);
    }

    @OnClick(R.id.notebookLayout)
    public void click(View view) {
        switch (view.getId()) {
            case R.id.notebookLayout:
                Intent intent = new Intent(CenterActivity.this,NoteBookActivity.class);
                startActivity(intent);
                break;
        }

    }
    }
