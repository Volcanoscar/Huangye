package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fujie.module.activity.AbstractTemplateActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.adapter.NoteBookAdapter;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.db.DBUtil;
import com.nuo.utils.XutilHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/2/10.
 */
public class NoteBookActivity extends AbstractTemplateActivity {
    @ViewInject(R.id.msg_list)
    private ListView msg_list;
    @ViewInject(R.id.empty)
    private TextView empty;

    private DbUtils dbUtil;
    private List<NoteBook> noteBookList = new ArrayList<NoteBook>();
    private NoteBookAdapter noteBookAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initData() {
        dbUtil = XutilHelper.getDB(NoteBookActivity.this);
        try {
            noteBookList = dbUtil.findAll(Selector.from(NoteBook.class).orderBy("create_time",true));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        if (noteBookList == null || noteBookList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            noteBookAdapter = new NoteBookAdapter(NoteBookActivity.this, noteBookList);
            msg_list.setAdapter(noteBookAdapter);
        }
    }

    @OnClick({R.id.tel_show})
        public void click(View view) {
            switch (view.getId()) {
                case R.id.tel_show:
                    Intent intent = new Intent(this,AddNoteBookActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
    }
    }
