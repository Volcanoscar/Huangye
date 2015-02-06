package com.nuo.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fujie.module.dialog.ActionSheetDialog;

public class ChangePicActivity extends Activity {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    private Intent intent;
    /**
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /**
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    /**
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";

    private static final String TAG = "ChangePicActivity";

    /**
     * 获取到的图片路径
     */
    private String picPath;
    private Uri photoUri;
    private Intent lastIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pic);
        initView();
    }

    private void initView() {
        lastIntent = getIntent();

        ActionSheetDialog holeDialog = new ActionSheetDialog(ChangePicActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
                                //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                               /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);*/
                                //执行拍照前，应该先判断SD卡是否存在
                                String SDState = Environment.getExternalStorageState();
                                if (SDState.equals(Environment.MEDIA_MOUNTED)) {

                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
                                    /***
                                     * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
                                     * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
                                     * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
                                     */
                                    ContentValues values = new ContentValues();
                                    photoUri = ChangePicActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                                    /**-----------------*/
                                    startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
                                } else {
                                    Toast.makeText(ChangePicActivity.this, "内存卡不存在", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                )
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                                //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
                            }
                        }
                );
        holeDialog.show();
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = " + picPath);
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            finish();
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

}
