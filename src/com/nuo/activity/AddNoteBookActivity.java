package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fujie.module.listview.SwipeListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.adapter.RecordAdapter;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.bean.notebook.NoteBookRecord;
import com.nuo.bean.notebook.NoteBookType;
import com.nuo.utils.ActivityForResultUtil;
import com.nuo.utils.BitMapUtil;
import com.nuo.utils.StringUtils;
import com.nuo.utils.T;
import com.nuo.utils.XutilHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxl on 2015/2/10.
 * 添加笔记界面
 */
public class AddNoteBookActivity extends Activity {
    private DbUtils dbUtils;

    /*  @ViewInject(R.id.title)
      private EditText titleEditText;*/
    @ViewInject(R.id.content)
    private EditText contentEditText;
    @ViewInject(R.id.write_layout)
    private LinearLayout write_layout;
    @ViewInject(R.id.text_layout)
    private LinearLayout text_layout;
    @ViewInject(R.id.recordListView)
    private SwipeListView recordListView;
    private NoteBook noteBook;
    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private List<Bitmap> mBitmaps = new ArrayList<Bitmap>();
    List<NoteBookType> imageSpanList = new ArrayList<NoteBookType>();
    Map<ImageSpan, NoteBookType> imageSpanMap = new HashMap<ImageSpan, NoteBookType>();
    private List<NoteBookRecord> recordList = null;
    private RecordAdapter recordAdapter;

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
        if (noteBook != null) {//预览界面过来的
            //titleEditText.setText(noteBook.getTitle());
            contentEditText.setText(noteBook.getContent());

            //进行除文本外的其它笔记类型的添加
            if (noteBook.getId() != null) {
                try {
                    List<NoteBookType> noteBookTypes = dbUtils.findAll(Selector.from(NoteBookType.class).where("note_book_id", "=", noteBook.getId()).orderBy("position"));
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
            }
            //初始化字体
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFF000000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(25);
            MyView myView = (MyView) findViewById(R.id.myView1);
            myView.setPaint(mPaint);
            myView.setBitmapMap(mBitmaps);
        }
        //录音listview 初始化
        recordAdapter = new RecordAdapter(AddNoteBookActivity.this,recordList);
        recordListView.setAdapter(recordAdapter);
    }

    private void initData() {
        dbUtils = XutilHelper.getDB(this);
        noteBook = (NoteBook) getIntent().getSerializableExtra("notebook");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String picPath = data.getStringExtra(ChangePicActivity.KEY_PHOTO_PATH);
            Log.i(AddNoteBookActivity.this.toString(), "最终选择的图片=" + picPath);
            if (picPath != null) {
                displayBitmapOnText(picPath, BitMapUtil.decodeSampledBitmapFromStream(picPath, 500, 500), NoteBookType.TYPE_PIC);
            }
        } else if (resultCode == ActivityForResultUtil.REQUESTCODE_RECORD) {  //录音返回
            NoteBookRecord recordPath = (NoteBookRecord)data.getSerializableExtra(VoiceActivity.RECORD_PATH);
            recordList.add(recordPath);
            recordAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.handwriting, R.id.gallery, R.id.camera, R.id.record, R.id.handwrite_delete, R.id.handwrite_exit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.handwriting:
                text_layout.setVisibility(View.GONE);
                write_layout.setVisibility(View.VISIBLE);
                contentEditText.setCursorVisible(false);      //设置输入框中的光标不可见
                contentEditText.setFocusable(false);           //无焦点
                contentEditText.setFocusableInTouchMode(false);     //触摸时也得不到焦点
                break;
            case R.id.gallery:
                Intent intent = new Intent(AddNoteBookActivity.this,
                        ChangePicActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1);
                break;
            case R.id.camera:
                Intent temp = new Intent(AddNoteBookActivity.this,
                        ChangePicActivity.class);
                temp.putExtra("type", 2);
                startActivityForResult(temp, 1);
                break;
            case R.id.record:
                Intent recordTemp = new Intent(AddNoteBookActivity.this,
                        VoiceActivity.class);
                startActivityForResult(recordTemp, 1);
                break;
            case R.id.handwrite_delete:
                deleteEditTextSpan();
                break;
            case R.id.handwrite_exit:
                text_layout.setVisibility(View.VISIBLE);
                write_layout.setVisibility(View.GONE);
                contentEditText.setCursorVisible(true);
                contentEditText.setFocusable(true);
                contentEditText.setFocusableInTouchMode(true);
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
        if (contentEditText.getText().length() != 0) {  //保存笔记本
            //保存
            Date date = new Date();
            if (noteBook == null) {
                noteBook = new NoteBook();
            }
          /*  String title = titleEditText.getText().toString();
            if ("".equals(title)) {
                title = getResources().getString(R.string.default_title);
            }
            noteBook.setTitle(title);*/

            final Spanned s = contentEditText.getEditableText();
            String et = contentEditText.getText().toString();
            final ImageSpan[] imageSpan = s.getSpans(0, s.length(), ImageSpan.class);
            //之后以要排序，是要从后向前把占位符号给删除掉。
            List<ImageSpan> spanList = Arrays.asList(imageSpan);
            Collections.sort(spanList, new Comparator<ImageSpan>() {
                @Override
                public int compare(ImageSpan lhs, ImageSpan rhs) {
                    return s.getSpanStart(lhs) - s.getSpanStart(rhs);
                }
            });

            for (int i = spanList.size() - 1; i >= 0; i--) {
                int start = s.getSpanStart(spanList.get(i));
                int end = s.getSpanEnd(spanList.get(i));
                if (start >= 0) {
                    et = StringUtils.delete(et, start, end);
                }
            }
            for (ImageSpan img : imageSpan) {
                NoteBookType noteBookType = imageSpanMap.get(img);
                if (noteBookType != null) {
                    noteBookType.setPosition(s.getSpanStart(img));
                    imageSpanMap.remove(img);
                    imageSpanList.add(noteBookType);
                }
            }
            //删除其它文件
            for (NoteBookType type : imageSpanMap.values()) {
                File file = new File(type.getPath());
                file.deleteOnExit();
            }

            noteBook.setContent(et);
            noteBook.setUpdate_time(date);
            try {
                if (noteBook.getId() != null) {
                    dbUtils.update(noteBook, "title", "content", "update_time");
                } else {
                    noteBook.setCreate_time(date);
                    dbUtils.saveBindingId(noteBook);
                }
                dbUtils.delete(NoteBookType.class, WhereBuilder.b("note_book_id", "=", noteBook.getId()));
                for (NoteBookType type : imageSpanList) {
                    type.setNoteBookId(noteBook.getId());
                }
                dbUtils.saveAll(imageSpanList);

                //保存录音
                for (NoteBookRecord record : recordList) {
                    record.setNote_book_id(noteBook.getId());
                }
                dbUtils.saveBindingIdAll(recordList);
            } catch (DbException e) {
                e.printStackTrace();
            }
            T.showShort(AddNoteBookActivity.this, "笔记保存成功");
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

    SpannableString mSpan1 = new SpannableString("1");

    /*
    * this is add bitmap on edit text

    */
    public void displayBitmapOnText(String path, Bitmap thumbnailBitmap, Integer displayTpye) {

        if (thumbnailBitmap == null)

            return;

        int start = contentEditText.getSelectionStart();
        ImageSpan span = new ImageSpan(thumbnailBitmap);
        mSpan1.setSpan(span, mSpan1.length() - 1, mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (contentEditText != null) {
            Editable et = contentEditText.getText();
            et.insert(start, mSpan1);
            contentEditText.setText(et);
            contentEditText.setSelection(start + mSpan1.length());
        }
        contentEditText.setLineSpacing(10f, 1f);
        saveBitMap(span, path, thumbnailBitmap, start, displayTpye);

    }

    /*
    * this is delete bitmap on edit text
    * from end to start
    */
    private void deleteEditTextSpan() {
        Spanned s = contentEditText.getEditableText();
        ImageSpan[] imageSpan = s.getSpans(0, s.length(), ImageSpan.class);
        for (int i = imageSpan.length - 1; i >= 0; i--) {
            if (i == imageSpan.length - 1) {
                int start = s.getSpanStart(imageSpan[i]);
                int end = s.getSpanEnd(imageSpan[i]);
                Editable et = contentEditText.getText();
                et.delete(start, end);
            }
        }
        contentEditText.invalidate();
        //TODO:..删除原文件
    }

    /**
     * 把添加的imageSpan放在里面，最后统一在saveNoteBook方法中保存到数据库中
     *
     * @param thumbnailBitmap
     * @param start
     * @see #saveNoteBook()
     */
    private void saveBitMap(ImageSpan span, String path, Bitmap thumbnailBitmap, int start, int displayTpye) {
        path = path == null ? BitMapUtil.saveBitmap(thumbnailBitmap) : path;
        NoteBookType type = new NoteBookType();
        type.setPosition(start);
        type.setPath(path);
        type.setType(displayTpye);
        imageSpanMap.put(span, type);
    }

    public void displayBitmapOnText(NoteBookType type) {
        Bitmap thumbnailBitmap = BitMapUtil.decodeSampledBitmapFromStream(type.getPath(), 500, 500);
        if (thumbnailBitmap == null)
            return;
        int start = type.getPosition();
        ImageSpan span = new ImageSpan(thumbnailBitmap);
        mSpan1.setSpan(span, mSpan1.length() - 1, mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mSpan1.toString();
        if (contentEditText != null) {
            Editable et = contentEditText.getText();
            if (et.length() < start) {
                et.append(mSpan1);
            } else {
                et.insert(start, mSpan1);

            }
            contentEditText.setText(et);
            contentEditText.setSelection(et.length());
        }
        contentEditText.setLineSpacing(10f, 1f);
        imageSpanMap.put(span, type);
    }
}