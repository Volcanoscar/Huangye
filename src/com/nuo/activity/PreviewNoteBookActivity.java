package com.nuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.dialog.AlertDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.activity.NoteBookActivity;
import com.nuo.activity.R;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.utils.DateUtil;
import com.nuo.utils.T;
import com.nuo.utils.XutilHelper;

import java.util.Date;

/**
 * Created by zxl on 2015/2/10.
 * 添加笔记界面
 */
public class PreviewNoteBookActivity extends AbstractTemplateActivity {
    private DbUtils dbUtils;

    @ViewInject(R.id.time)
    private TextView time;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.content)
    private TextView content;

    @ViewInject(R.id.scroll_view)
    private ScrollView scrollView;

    @ViewInject(R.id.bottom_layout)
    private LinearLayout mBottomBar;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_notebook);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initView() {

        scrollView.setOnTouchListener(new View.OnTouchListener() {
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
        });
    }

    private void action_down(float y) {
        mMotionY = y;
        bIsDown = true;
        mHandler.removeCallbacks(showBottomBarRunnable);
    }

    private void initData() {
        id = getIntent().getIntExtra("id", -1);
        dbUtils = XutilHelper.getDB(this);
        try {
            NoteBook noteBook = dbUtils.findById(NoteBook.class, id);
            title.setText(noteBook.getTitle());
            content.setText(noteBook.getContent());
            time.setText(DateUtil.dateToStr(noteBook.getCreate_time())); //创建时间还修改时间？
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.menu_share, R.id.menu_edit, R.id.tag_empty_notes, R.id.menu_delete_gray})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享笔记给好友"));
                break;
            case R.id.menu_edit:
                Intent editNoteBook = new Intent(PreviewNoteBookActivity.this, AddNoteBookActivity.class);
                editNoteBook.putExtra("id", id);
                startActivity(editNoteBook);
                finish();
                break;
            case R.id.tag_empty_notes:
                T.showShort(this, "编辑");
                break;
            case R.id.menu_delete_gray:
                new AlertDialog(this).builder().setTitle("删除").setMsg("确定删除?")
                        .setPositiveButton("删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    dbUtils.deleteById(NoteBook.class, id);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                    T.showShort(PreviewNoteBookActivity.this, R.string.delete_error);
                                }
                                Intent intent = new Intent(PreviewNoteBookActivity.this, NoteBookActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
                break;
        }
    }

    public void showBottomBar() {

        if (mBottomBar != null && mBottomBar.getVisibility() == View.GONE) {
            mBottomBar.setVisibility(View.INVISIBLE);
            Animation translateAnimation = new TranslateAnimation(mBottomBar.getLeft(), mBottomBar.getLeft(), mBottomBar.getHeight(), 0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mBottomBar.startAnimation(translateAnimation);

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
                    mBottomBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void hideBottomBar() {

        if (mBottomBar != null && mBottomBar.getVisibility() == View.VISIBLE) {
            Animation translateAnimation = new TranslateAnimation(mBottomBar.getLeft(), mBottomBar.getLeft(), 0, mBottomBar.getHeight());
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            mBottomBar.startAnimation(translateAnimation);
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
                    mBottomBar.setVisibility(View.GONE);

                }
            });
        }
    }

    private Runnable showBottomBarRunnable = new Runnable() {
        @Override
        public void run() {
            showBottomBar();
        }
    };


    private boolean bIsMoved = false;
    private boolean bIsDown = false;
    private int mDeltaY;
    private float mMotionY;
    private int oldFirstVisibleItem = 0;
    private Handler mHandler = new Handler();
}