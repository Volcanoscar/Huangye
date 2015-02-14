package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fujie.module.activity.BackApplication;
import com.fujie.module.editview.ClearEditText;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.adapter.TagGridViewAdapter;
import com.nuo.adapter.TagListViewAdapter;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.bean.notebook.NoteBookLabel;
import com.nuo.utils.XutilHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zxl on 2015/2/13.
 */
public class TagNoteBookActivity extends Activity{
    private static final String LABEL_NAME = "label_name";
    @ViewInject(R.id.tag_view)
    private GridView tag_view;
    @ViewInject(R.id.tag_list)
    private ListView tag_list;
    @ViewInject(R.id.filter_edit)
    private ClearEditText filter_edit;

    @ViewInject(R.id.add_tag_layout)
    private LinearLayout add_tag_layout;
    @ViewInject(R.id.new_tag_name)
    private TextView new_tag_name;
    private NoteBook notebook;
    private DbUtils dbUtils;
    private List<NoteBookLabel> hasLabelList = new ArrayList<NoteBookLabel>();
    private List<NoteBookLabel> noLabelList = new ArrayList<NoteBookLabel>();
    private TagGridViewAdapter gridViewAdapter;
    private TagListViewAdapter listViewAdapter;
    private String tagName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_notebook);
        initActionBar();
        ViewUtils.inject(this);
        initData();
        initViewAction();
    }

    private void initViewAction() {
        add_tag_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加新标签
                NoteBookLabel noteBookLabel = new NoteBookLabel();
                String tag_name = filter_edit.getText().toString();
                noteBookLabel.setName(tag_name);
                noteBookLabel.setCreate_time(new Date());
                try {
                    dbUtils.saveBindingId(noteBookLabel);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                noteBookLabel.setHasTag(true);
                add_tag_layout.setVisibility(View.GONE);
                tag_list.setVisibility(View.VISIBLE);
                hasLabelList = hasLabelList == null ? new ArrayList<NoteBookLabel>() : hasLabelList;
                noLabelList = noLabelList == null ? new ArrayList<NoteBookLabel>() : noLabelList;
                hasLabelList.add(noteBookLabel);
                noLabelList.add(noteBookLabel);
                gridViewAdapter.notifyDataSetChanged();
                listViewAdapter.notifyDataSetChanged();
                filter_edit.setText("");
                saveLabel();
            }
        });
        //检测输入内容
        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s)) {
                    listViewAdapter.setData(noLabelList);
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    List<NoteBookLabel> tempList = new ArrayList<NoteBookLabel>();
                    for (NoteBookLabel label : noLabelList) {
                        if (label.getName().contains(s)) {
                            tempList.add(label);
                        }
                    }
                    if (tempList.isEmpty()) {
                        new_tag_name.setText("\"" + s + "\"");
                        add_tag_layout.setVisibility(View.VISIBLE);
                        tag_list.setVisibility(View.GONE);
                    } else {
                        listViewAdapter.setData(tempList);
                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void initData() {
        notebook = (NoteBook) getIntent().getSerializableExtra("notebook");
        dbUtils = XutilHelper.getDB(this);
        String label = notebook.getLabel();
        if (label != null) {
            String[] labelid = label.split(",");
            try {
                //查询本笔记已经有的标签
                hasLabelList = dbUtils.findAll(Selector.from(NoteBookLabel.class).where("id", "in", labelid).orderBy("id"));
                //查询本笔记没有的标签
                noLabelList = dbUtils.findAll(Selector.from(NoteBookLabel.class).orderBy("id"));
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        hasLabelList = hasLabelList == null ? new ArrayList<NoteBookLabel>() : hasLabelList;
        noLabelList = noLabelList == null ? new ArrayList<NoteBookLabel>() : noLabelList;
        for (NoteBookLabel temp : noLabelList) {
            if (hasLabelList.contains(temp)) {
                temp.setHasTag(true);
            }
        }
        gridViewAdapter = new TagGridViewAdapter(this, hasLabelList);
        listViewAdapter = new TagListViewAdapter(this, noLabelList);
        gridViewAdapter.setCheckListener(new TagListViewAdapter.CheckListener() {
            @Override
            public void onCheckedChanged(NoteBookLabel noteBookLabel) {
                hasLabelList.remove(noteBookLabel);
                for (NoteBookLabel label : noLabelList) {
                    if (label.getId().equals(noteBookLabel.getId())) {
                        label.setHasTag(false);
                    }
                }
                listViewAdapter.notifyDataSetChanged();
                saveLabel();
            }
        });
        tag_view.setAdapter(gridViewAdapter);
        listViewAdapter.setListener(new TagListViewAdapter.CheckListener() {
            @Override
            public void onCheckedChanged(NoteBookLabel noteBookLabel) {
                if (noteBookLabel.isHasTag()) {
                    hasLabelList.add(noteBookLabel);
                } else {
                    hasLabelList.remove(noteBookLabel);
                }
                gridViewAdapter.notifyDataSetChanged();
                saveLabel();
            }
        });
        tag_list.setAdapter(listViewAdapter);
    }

    private void saveLabel(){
       String tags = "";
        tagName = "";
       for (NoteBookLabel label : hasLabelList) {
           if (tags.equals("")) {
               tags=label.getId().toString();
               tagName =label.getName().toString();
           }
           else {
               tags += "," + label.getId().toString();
               tagName += "," + label.getName().toString();
           }
       }
       notebook.setLabel(tags);
       notebook.setLabelName(tagName);
       try {
           dbUtils.update(notebook,"notebook_label","notebook_name");
       } catch (DbException e) {
           e.printStackTrace();
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
        BackApplication application = (BackApplication) getApplication();
        application.getActivityManager().popActivity(this);
        Intent lastIntent = getIntent();
        lastIntent.putExtra(LABEL_NAME, tagName);
        setResult(RESULT_OK, lastIntent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent lastIntent = getIntent();
                lastIntent.putExtra(LABEL_NAME, tagName);
                setResult(RESULT_OK, lastIntent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}