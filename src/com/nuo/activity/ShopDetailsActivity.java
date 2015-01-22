package com.nuo.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fujie.module.horizontalListView.HorizontalListView;
import com.nuo.adapter.SmallImageListViewAdapter;
import com.nuo.info.CommentsInfo;
import com.nuo.info.FoodInfo;
import com.nuo.info.ShopInfo;
import com.nuo.info.SignInfo;
import com.nuo.model.Model;
import com.nuo.net.ThreadPoolUtils;
import com.nuo.thread.HttpGetThread;
import com.nuo.utils.LoadImg;
import com.nuo.utils.LoadImg.ImageDownloadCallBack;
import com.nuo.utils.MyJson;
import com.nuo.utils.MyJson.DetailCallBack;
/**
 * 店铺详情模块
 * */
public class ShopDetailsActivity extends Activity {

    private ShopInfo info = null;
    private LoadImg loadImg;
    private HttpGetThread http = null;
    private MyJson myJson = new MyJson();
    private ArrayList<SignInfo> SignList;
    private ArrayList<CommentsInfo> CommentsList;
    private ArrayList<FoodInfo> FoodList;
    // top和店铺的属性
    private ImageView mShop_details_back, mShop_details_share,
            mShop_details_off, mShop_details_star;
    private TextView mShop_details_name, mShop_details_money;
    private HorizontalListView mShop_details_photo;

    // 创建popupWindow
    private View parent;
    private PopupWindow popupWindow;
    private RelativeLayout shop_details_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop_details);
        // 初始化图片异步加载类
        loadImg = new LoadImg(ShopDetailsActivity.this);
        // 获取从列表当中传递过来的数据
        Intent intent = getIntent();
        Bundle bund = intent.getBundleExtra("value");
        info = (ShopInfo) bund.getSerializable("ShopInfo");
        initView();
        // 查找网络数据
        String endParames = Model.SHOPDETAILURL + "shopid=" + info.getSid();
        http = new HttpGetThread(hand, endParames);
        ThreadPoolUtils.execute(http);
    }

    private void initView() {
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        // 标题控件
        mShop_details_back = (ImageView) findViewById(R.id.Shop_details_back);
        mShop_details_share = (ImageView) findViewById(R.id.Shop_details_share);
        mShop_details_off = (ImageView) findViewById(R.id.Shop_details_off);
        // 小图横向浏览
        mShop_details_name = (TextView) findViewById(R.id.Shop_details_name);
        mShop_details_photo = (HorizontalListView) findViewById(R.id.horizontalListView);
        SmallImageListViewAdapter hlva=new SmallImageListViewAdapter(this);
        mShop_details_photo.setAdapter(hlva);

        // 给控件设置监听
        mShop_details_back.setOnClickListener(myOnClickListener);
        mShop_details_share.setOnClickListener(myOnClickListener);
        mShop_details_off.setOnClickListener(myOnClickListener);

        //地图
        shop_details_address = (RelativeLayout) findViewById(R.id.shop_details_address);
        shop_details_address.setOnClickListener(myOnClickListener);
    }

    // 控件的监听事件
    private class MyOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();
            if (mID == R.id.Shop_details_back) {
                ShopDetailsActivity.this.finish();
            }
            else if (mID == R.id.shop_details_address) {
                Uri mUri = Uri.parse("geo:39.940409,116.355257?q=西直门");
                Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                startActivity(mIntent);
            }
            else if (mID == R.id.shop_details_qita) {
                Intent intent = new Intent(ShopDetailsActivity.this,
                        ShopDetailsMore.class);
                Bundle bund = new Bundle();
                bund.putSerializable("ShopInfo", info);
                intent.putExtra("value", bund);
                startActivity(intent);
            }
            else if (mID == R.id.Shop_details_bottom_img1) {
                Intent intent = new Intent(ShopDetailsActivity.this,
                        ShopDetailsCheckinActivity.class);
                Bundle bund = new Bundle();
                bund.putSerializable("ShopInfo", info);
                intent.putExtra("value", bund);
                startActivity(intent);
            }
            else if (mID == R.id.Shop_details_bottom_img3) {
                Intent intent = new Intent(ShopDetailsActivity.this,
                        ShopDetailsCommentActivity.class);
                Bundle bund = new Bundle();
                bund.putSerializable("ShopInfo", info);
                intent.putExtra("value", bund);
                startActivity(intent);
            }
            else if (mID == R.id.Shop_details_bottom_img4) {
                creatPopupWindow();
            }

        }
    }

    private void creatPopupWindow() {
        Builder builder = new Builder(ShopDetailsActivity.this);
        builder.setTitle("报错类型");
        final String[] items = new String[] { "商户已关闭", "地图位置错误", "商户信息错误",
                "商户重复", "其他" };
        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                    arg0.cancel();
                }
                switch (arg1) {
                    case 0:
                        Toast.makeText(ShopDetailsActivity.this,
                                "报错信息0:" + items[arg1], 1).show();
                        break;
                    case 1:
                        Toast.makeText(ShopDetailsActivity.this,
                                "报错信息1:" + items[arg1], 1).show();
                        break;
                    case 2:
                        Toast.makeText(ShopDetailsActivity.this,
                                "报错信息2:" + items[arg1], 1).show();
                        break;
                    case 3:
                        Toast.makeText(ShopDetailsActivity.this,
                                "报错信息3:" + items[arg1], 1).show();
                        break;
                    case 4:
                        Toast.makeText(ShopDetailsActivity.this,
                                "报错信息4:" + items[arg1], 1).show();
                        break;
                }
            }
        };
        builder.setItems(items, dialog);
        builder.setPositiveButton("取消", dialog);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

   /* // 添加图片方法
    private void addImg() {
        mShop_details_photo.setTag(Model.SHOPLISTIMGURL + info.getIname());
        Bitmap bit = loadImg.loadImage(mShop_details_photo,
                Model.SHOPLISTIMGURL + info.getIname(),
                new ImageDownloadCallBack() {
                    public void onImageDownload(ImageView imageView,
                                                Bitmap bitmap) {
                        // 不需要按照tag查找图片，不存在img复用问题
                        mShop_details_photo.setImageBitmap(bitmap);
                    }
                });
        if (bit != null) {
            mShop_details_photo.setImageBitmap(bit);
        }
    }*/

    // 线程返回信息
    Handler hand = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404) {
                Toast.makeText(ShopDetailsActivity.this, "找不到地址", 1).show();
            } else if (msg.what == 100) {
                Toast.makeText(ShopDetailsActivity.this, "传输失败", 1).show();
            } else if (msg.what == 200) {
                String result = (String) msg.obj;
                Log.e("result", "result:" + result);
                if (result != null) {
                    // 解析数据
                    myJson.getShopDetail(result, new DetailCallBack() {

                        @Override
                        public void getList(
                                ArrayList<SignInfo> SignList,
                                ArrayList<com.nuo.info.CommentsInfo> CommentsList,
                                ArrayList<FoodInfo> FoodList) {
                            // 获取解析回调数据
                            ShopDetailsActivity.this.SignList = SignList;
                            ShopDetailsActivity.this.CommentsList = CommentsList;
                            ShopDetailsActivity.this.FoodList = FoodList;
                        }
                    });
                }
            }
        };

    };

}
