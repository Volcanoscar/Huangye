package com.nuobuluo.huangye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.activity.AbstractTemplateActivity;
import com.nuobuluo.module.activity.BackApplication;
import com.nuobuluo.module.dialog.AlertDialog;
import com.nuobuluo.module.listview.SwipeListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.adapter.RecordAdapter;
import com.nuobuluo.huangye.adapter.TagNameAdapter;
import com.nuobuluo.huangye.bean.notebook.NoteBook;
import com.nuobuluo.huangye.bean.notebook.NoteBookLabel;
import com.nuobuluo.huangye.bean.notebook.NoteBookRecord;
import com.nuobuluo.huangye.bean.notebook.NoteBookType;
import com.nuobuluo.huangye.utils.BitMapUtil;
import com.nuobuluo.huangye.utils.DateUtil;
import com.nuobuluo.huangye.utils.T;
import com.nuobuluo.huangye.utils.XutilHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/2/10.
 * 添加笔记界面
 */
public class PreviewNoteBookActivity extends AbstractTemplateActivity {
    private DbUtils dbUtils;

    @ViewInject(R.id.time)
    private TextView time;
    /*  @ViewInject(R.id.title)
      private TextView title;*/
    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.tag_view)
    private GridView tag_view;
    @ViewInject(R.id.recordListView)
    private SwipeListView recordListView;

    @ViewInject(R.id.scroll_view)
    private ScrollView scrollView;

    @ViewInject(R.id.bottom_layout)
    private LinearLayout mBottomBar;
    private NoteBook noteBook;
    private List<NoteBookRecord> recordList = null;
    private RecordAdapter recordAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_notebook);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initView() {
        //标签
        initLabelView();
        content.setCursorVisible(false);      //设置输入框中的光标不可见
        content.setFocusable(false);           //无焦点
        content.setFocusableInTouchMode(false);     //触摸时也得不到焦点

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        NoteBook temp = null;
        try {
            temp = dbUtils.findById(NoteBook.class, noteBook.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
        noteBook.setLabel(temp.getLabel());
        initLabelView();
    }

    private void initLabelView() {
        if (noteBook.getLabel() != null) {
            String[] lagbels = noteBook.getLabel().split(",");
            List<NoteBookLabel> noteBookLabelList = null;
            try {
                noteBookLabelList = dbUtils.findAll(Selector.from(NoteBookLabel.class).where("id", "in", lagbels).orderBy("id"));
            } catch (DbException e) {
                e.printStackTrace();
            }
            noteBookLabelList = noteBookLabelList == null ? new ArrayList<NoteBookLabel>() : noteBookLabelList;
            TagNameAdapter viewAdapter = new TagNameAdapter(this, noteBookLabelList);
            tag_view.setAdapter(viewAdapter);
        }
    }

    private void action_down(float y) {
        mMotionY = y;
        bIsDown = true;
        mHandler.removeCallbacks(showBottomBarRunnable);
    }

    private void initData() {
        noteBook = (NoteBook) getIntent().getSerializableExtra("notebook");
        dbUtils = XutilHelper.getDB(this);
        try {
            NoteBook temp = dbUtils.findById(NoteBook.class, noteBook.getId());
            noteBook.setLabel(temp.getLabel());
        } catch (DbException e) {
            e.printStackTrace();
        }
           /* noteBook = dbUtils.findById(NoteBook.class, id);*/
        //title.setText(Html.fromHtml(noteBook.getTitle()));
        content.setText(Html.fromHtml(noteBook.getContent()));
        time.setText(DateUtil.dateToStr(noteBook.getCreate_time())); //创建时间还修改时间？
        //进行除文本外的其它笔记类型的添加
        try {
            List<NoteBookType> noteBookTypes = dbUtils.findAll(Selector.from(NoteBookType.class).where("note_book_id","=",noteBook.getId()).orderBy("position"));
            if (noteBookTypes != null && !noteBookTypes.isEmpty()) {
                for (NoteBookType type : noteBookTypes) {
                    displayBitmapOnText(type);
                }
            }
            //初始化录音数据
            recordList= dbUtils.findAll(Selector.from(NoteBookRecord.class).where("note_book_id", "=", noteBook.getId()).orderBy("createTime"));
            if (recordList == null) {
                recordList = new ArrayList<NoteBookRecord>();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //录音布局
        //录音listview 初始化
        recordAdapter = new RecordAdapter(PreviewNoteBookActivity.this,recordList);
        recordListView.setAdapter(recordAdapter);
    }

    /*
    * this is add bitmap on edit text

    */
    SpannableString mSpan1 = new SpannableString("1");

    public void displayBitmapOnText(NoteBookType type) {
        Bitmap thumbnailBitmap = BitMapUtil.decodeSampledBitmapFromStream(type.getPath(), 300, 300);
        if (thumbnailBitmap == null)
            return;
        int start = type.getPosition();
        mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - 1, mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mSpan1.toString();
        if (content != null) {
            Editable et = content.getText();
            if (et.length() < start) {
                et.append(mSpan1);
            }else{
                et.insert(start, mSpan1);

            }
            content.setText(et);
        }
        content.setLineSpacing(10f, 1f);
    }

    @OnClick({R.id.menu_share, R.id.menu_edit, R.id.tag_empty_notes, R.id.menu_delete_gray})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, content.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享笔记给好友"));
                break;
            case R.id.menu_edit:
                Intent editNoteBook = new Intent(PreviewNoteBookActivity.this, AddNoteBookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("notebook", noteBook);
                editNoteBook.putExtras(bundle);
                startActivity(editNoteBook);
                BackApplication application = (BackApplication) getApplication();
                application.getActivityManager().popActivity(NoteBookActivity.class);
                finish();
                break;
            case R.id.tag_empty_notes:
                Intent tagNoteBook = new Intent(PreviewNoteBookActivity.this, TagNoteBookActivity.class);
                Bundle temp = new Bundle();
                temp.putSerializable("notebook", noteBook);
                tagNoteBook.putExtras(temp);
                startActivityForResult(tagNoteBook, 1);
                break;
            case R.id.menu_delete_gray:
                new AlertDialog(this).builder().setTitle("删除").setMsg("确定删除?")
                        .setPositiveButton("删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    dbUtils.deleteById(NoteBook.class, noteBook.getId());
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
                   /* mBottomBar.setVisibility(View.VISIBLE);*/
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
                   /* mBottomBar.setVisibility(View.GONE);*/

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