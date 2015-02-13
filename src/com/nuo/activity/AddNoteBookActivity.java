package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fujie.module.activity.AbstractTemplateActivity;
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
public class AddNoteBookActivity extends AbstractTemplateActivity {
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

    /**
     * 加载 动作栏 菜单<br>
     * 根据不同的Activity修改ActionBar上的动作，并修改标题
     * *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_notebook, menu);
        //每个动作栏中都有反馈项
        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //验证
                if (titleEditText.getText().length() == 0 || contentEditText.getText().length() == 0) {
                    T.showShort(AddNoteBookActivity.this, R.string.notebook_add_tip);
                    return false;
                }
                //保存
                Date date = new Date();
                if (noteBook == null) {
                    noteBook = new NoteBook();
                }
                noteBook.setTitle(titleEditText.getText().toString());
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
                Intent intent = new Intent(AddNoteBookActivity.this, NoteBookActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
        return true;
    }
}