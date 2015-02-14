package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.activity.BackApplication;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.db.DBUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.PreferenceUtils;
import com.nuo.utils.T;
import com.nuo.utils.XutilHelper;

import java.util.Date;

/**
 * Created by zxl on 2015/2/10.
 * 添加笔记界面
 */
public class AddNoteBookActivity extends Activity {
    private DbUtils dbUtils;

    @ViewInject(R.id.title)
    private EditText titleEditText;
    @ViewInject(R.id.content)
    private EditText contentEditText;
    private NoteBook noteBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notebook);
        ViewUtils.inject(this);
        initActionBar();
        initData();
        initView();
    }

    private void initView() {
        if (noteBook!= null) {//预览界面过来的
            titleEditText.setText(noteBook.getTitle());
            contentEditText.setText(noteBook.getContent());
        }
    }

    private void initData() {
        dbUtils = XutilHelper.getDB(this);
        noteBook = (NoteBook) getIntent().getSerializableExtra("notebook");
    }

    @OnClick({R.id.handwriting, R.id.gallery, R.id.camera, R.id.record})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.handwriting:
                T.showShort(AddNoteBookActivity.this, "手写");
                break;
            case R.id.gallery:
                T.showShort(AddNoteBookActivity.this, "选择图片");
                break;
            case R.id.camera:
                T.showShort(AddNoteBookActivity.this, "相机");
                break;
            case R.id.record:
                T.showShort(AddNoteBookActivity.this, "录音");
                break;
        }

    }


    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
        actionBar.setDisplayHomeAsUpEnabled(true);  //添加反馈按键
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNoteBook();

    }

    private void saveNoteBook() {
        //验证
        if (titleEditText.getText().length() == 0 && contentEditText.getText().length() == 0) {
            return ;
        }
        if (contentEditText.getText().length() != 0) {  //保存笔记本
            //保存
            Date date = new Date();
            if (noteBook == null) {
                noteBook = new NoteBook();
            }
            String title = titleEditText.getText().toString();
            if ("".equals(title)) {
                title = getResources().getString(R.string.default_title);
            }
            noteBook.setTitle(title);
            noteBook.setContent(contentEditText.getText().toString());
            noteBook.setUpdate_time(date);
            try {
                if (noteBook.getId()!=null) {
                    dbUtils.update(noteBook, "title", "content", "update_time");
                } else {
                    noteBook.setCreate_time(date);
                    dbUtils.saveBindingId(noteBook);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            T.showShort(AddNoteBookActivity.this,"笔记保存成功");
        }
        Intent intent = new Intent(AddNoteBookActivity.this, NoteBookActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNoteBook();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}