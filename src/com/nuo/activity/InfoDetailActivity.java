package com.nuo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.nuo.adapter.SmallImageListViewAdapter;
import com.nuo.adapter.SmsCursor;
import com.nuo.bean.MsgInfo;
import com.nuo.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺详情模块
 * */
public class InfoDetailActivity extends Activity {

    private MsgInfo info = null;
    // top和店铺的属性
    private ImageView mShop_details_back, mShop_details_share,
            mShop_details_off;
    private TextView mShop_details_name;
    private HorizontalListView mShop_details_photo;


    private RelativeLayout shop_details_address;
    private TextView create_time;
    private TextView shop_details_address_txt;
    private TextView shop_details_qita_txt;
    private TextView phone;
    private List<String> imgAddList = new ArrayList<String>();
    private SmallImageListViewAdapter smallImageListViewAdapter;
    private LinearLayout Shop_details_bottom_phone;
    private LinearLayout Shop_details_bottom_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_detail);
        // 获取从列表当中传递过来的数据
        Intent intent = getIntent();
        Bundle bund = intent.getBundleExtra("value");
        info = (MsgInfo) bund.getSerializable("ShopInfo");
        initView();
        initData();
    }

    private void initData() {
        //标题
        mShop_details_name.setText(info.getTitle());
        //发布时间
        create_time.setText(DateUtil.dateToStr(info.getCreateTime()));
        //店铺名称
        shop_details_address_txt.setText(info.getDianpuName());
        //描述
        shop_details_qita_txt.setText(info.getContent());
        //电话
        phone.setText(info.getPhone());
        //图片
        imgAddList.add(info.getPhoto());
        smallImageListViewAdapter.setImgAddList(imgAddList);
        smallImageListViewAdapter.notifyDataSetChanged();
    }

    private void initView() {
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        // 标题控件
        mShop_details_back = (ImageView) findViewById(R.id.Shop_details_back);
        mShop_details_share = (ImageView) findViewById(R.id.Shop_details_share);
        mShop_details_off = (ImageView) findViewById(R.id.Shop_details_off);
        create_time = (TextView) findViewById(R.id.create_time);
        // 小图横向浏览
        mShop_details_name = (TextView) findViewById(R.id.Shop_details_name);
        shop_details_address_txt = (TextView) findViewById(R.id.shop_details_address_txt);
        phone = (TextView) findViewById(R.id.phone);
        shop_details_qita_txt = (TextView) findViewById(R.id.shop_details_qita_txt);
        mShop_details_photo = (HorizontalListView) findViewById(R.id.horizontalListView);
        smallImageListViewAdapter = new SmallImageListViewAdapter(this,imgAddList);
        mShop_details_photo.setAdapter(smallImageListViewAdapter);



        Shop_details_bottom_phone = (LinearLayout) findViewById(R.id.Shop_details_bottom_phone);
        Shop_details_bottom_sms = (LinearLayout) findViewById(R.id.Shop_details_bottom_sms);
        // 给控件设置监听
        mShop_details_back.setOnClickListener(myOnClickListener);
        mShop_details_share.setOnClickListener(myOnClickListener);
        mShop_details_off.setOnClickListener(myOnClickListener);
        Shop_details_bottom_phone.setOnClickListener(myOnClickListener);
        Shop_details_bottom_sms.setOnClickListener(myOnClickListener);

        //地图
        shop_details_address = (RelativeLayout) findViewById(R.id.shop_details_address);
        shop_details_address.setOnClickListener(myOnClickListener);
    }

    // 控件的监听事件
    private class MyOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();
            if (mID == R.id.Shop_details_back) {
                InfoDetailActivity.this.finish();
            }
            else if (mID == R.id.shop_details_address) {
                Intent mIntent = new Intent(InfoDetailActivity.this,ShopDetailActivity.class);
                mIntent.putExtra("userId", info.getUserId());
                startActivity(mIntent);
            }
            else if (mID == R.id.Shop_details_bottom_phone) {  //确认电话弹出框
                AlertDialog.Builder builer = new AlertDialog.Builder(InfoDetailActivity.this);
                builer.setMessage("是否拨打电话  " + info.getPhone());
                builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+info.getPhone()));
                        startActivity(intent);
                    }
                });
                builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builer.create();
                dialog.show();
            }
            else if (mID == R.id.Shop_details_bottom_sms) { //sms 模板
                Intent intent = new Intent(InfoDetailActivity.this,
                        ChatActivity.class);
                Bundle mBundle = new Bundle();
                SmsCursor.Person_Sms person_sms = new SmsCursor.Person_Sms();
                person_sms.Name = person_sms.Number = info.getPhone();
                mBundle.putSerializable("chatperson",person_sms);
                mBundle.putString("msg","你好，我对你在诺部落发布的\""+info.getTitle()+" \"很感兴趣,想和你详细了解一下。");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        }
    }
}
