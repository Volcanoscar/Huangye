package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.button.FloatingActionButton;
import com.fujie.module.button.ShowHideOnScroll;
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

    @ViewInject(R.id.tel_show)
    private FloatingActionButton saveBtn;

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
        msg_list.setOnTouchListener(new ShowHideOnScroll(saveBtn));
        /*msg_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                float y = ev.getY();
                int action = ev.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        action_down(y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mDeltaY = (int) (y - mMotionY);
                        bIsMoved = true;
                        action_down(y);
                        break;
                    case MotionEvent.ACTION_UP:
                        bIsMoved = false;
                        bIsDown = false;
                        if (!bIsMoved && !bIsDown) {
                            mHandler.postDelayed(showBottomBarRunnable, 2000);
                        }
                        if (mDeltaY < 0) {
                            hideBottomBar();
                        } else {
                            showBottomBar();
                        }
                        bIsMoved = false;
                        break;

                }
                return false;
            }
        });*/
    }
    private void action_down(float y) {
        mMotionY = y;
        bIsDown = true;
        mHandler.removeCallbacks(showBottomBarRunnable);
    }
    public void showBottomBar() {
        if (saveBtn != null && saveBtn.getVisibility() == View.GONE) {
            saveBtn.setVisibility(View.INVISIBLE);
            Animation translateAnimation = new TranslateAnimation(saveBtn.getLeft(), saveBtn.getLeft(), 0, saveBtn.getHeight());
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            saveBtn.startAnimation(translateAnimation);

            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getActionBar().show();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    saveBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void hideBottomBar() {
        if (saveBtn != null && saveBtn.getVisibility() == View.VISIBLE) {
            Animation translateAnimation = new TranslateAnimation(saveBtn.getLeft(), saveBtn.getLeft(),saveBtn.getHeight(),0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            saveBtn.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    getActionBar().hide();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    saveBtn.setVisibility(View.GONE);

                }
            });
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

    /**
     * 加载 动作栏 菜单<br>
     * 根据不同的Activity修改ActionBar上的动作，并修改标题
     * *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notebook_action, menu);
        MenuItem searchItem =menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("搜索笔记内容或标签");
        // 查询事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if ("".equals(s)) {
                    noteBookAdapter.setData(null,noteBookList);
                    noteBookAdapter.notifyDataSetChanged();
                    return false;
                }
                if (noteBookList != null) {
                    List<NoteBook> noteBooks = new ArrayList<NoteBook>();
                    for (NoteBook noteBook : noteBookList) {
                        if (noteBook.getContent().contains(s)||noteBook.getLabelName().contains(s)) {
                            try {
                                noteBooks.add(noteBook.clone());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    noteBookAdapter.setData(s, noteBooks);
                    noteBookAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        return true;
    }
    private boolean bIsMoved = false;
    private boolean bIsDown = false;
    private int mDeltaY;
    private float mMotionY;
    private int oldFirstVisibleItem = 0;
    private Handler mHandler = new Handler();
    private Runnable showBottomBarRunnable = new Runnable() {
        @Override
        public void run() {
            showBottomBar();
        }
    };
}
