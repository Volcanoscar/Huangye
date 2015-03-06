package com.nuo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nuo.activity.amap.GeocoderActivity;
import com.nuo.adapter.SmallImageListViewAdapter;
import com.nuo.adapter.SmsCursor;
import com.nuo.bean.ShopInfo;
import com.nuo.bean.UserInfo;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.*;
import com.nuo.utils.amap.AMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺详情模块
 */
public class ShopDetailActivity extends Activity {

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
    private TimeOutHandler timeOutHandler = new TimeOutHandler(this);
    private ShopInfo info;
    private TextView contactPerson;
    private String userId;
    private TextView qqCodeTextView;
    private TextView weixinCodeTextView;
    private TextView urlTextView;
    private TextView yingyeTextView;
    private TextView otherPhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shop_detail);
        initData();
    }

    private void initData() {
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Integer accountId = PreferenceUtils.getPrefInt(ShopDetailActivity.this,
                    PreferenceConstants.ACCOUNT_ID, -1);
            DbUtils dbUtils = XutilHelper.getDB(ShopDetailActivity.this);
            try {
                UserInfo userInfo = dbUtils.findById(UserInfo.class, accountId);
                userId = userInfo.getUserId();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        NetUtil.getShopInfo(userId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                info = ShopInfo.parseMap(responseInfo.result);
                initView();
                timeOutHandler.stop();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                timeOutHandler.stop();
                T.showShort(ShopDetailActivity.this, R.string.net_error);
            }

            @Override
            public void onStart() {
                super.onStart();
                timeOutHandler.start(null);
            }
        });


    }

    private void initView() {
        otherPhoneTextView = (TextView) findViewById(R.id.otherPhoneTextView);
        qqCodeTextView = (TextView) findViewById(R.id.qqCodeTextView);
        weixinCodeTextView = (TextView) findViewById(R.id.telTextView);
        urlTextView = (TextView) findViewById(R.id.weixinCodeTextView);
        yingyeTextView = (TextView) findViewById(R.id.qqTextView);


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
        contactPerson = (TextView) findViewById(R.id.contactPerson);
        shop_details_qita_txt = (TextView) findViewById(R.id.contentTextView);
        mShop_details_photo = (HorizontalListView) findViewById(R.id.horizontalListView);
        smallImageListViewAdapter = new SmallImageListViewAdapter(this, imgAddList);
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

        //标题
        mShop_details_name.setText(info.getDianpuName());
        //发布时间
        create_time.setText(DateUtil.dateToStr(info.getCreateTime()));
        //店铺名称
        shop_details_address_txt.setText(info.getAddress());
        //描述
        shop_details_qita_txt.setText(info.getDesc());
        //电话
        phone.setText(info.getContactPhone());
        contactPerson.setText(info.getContactPerson());

        qqCodeTextView.setText(info.getQq());
        weixinCodeTextView.setText(info.getWeixin());
        urlTextView.setText(info.getUrl());
        yingyeTextView.setText(info.getYingyeTime());
        otherPhoneTextView.setText(info.getContactTel());
        //图片
        imgAddList.add(info.getImg());
        smallImageListViewAdapter.setImgAddList(imgAddList);
        smallImageListViewAdapter.notifyDataSetChanged();
    }

    // 控件的监听事件
    private class MyOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();
            if (mID == R.id.Shop_details_back) {
                ShopDetailActivity.this.finish();
            } else if (mID == R.id.shop_details_address) {
             /*   Uri mUri = Uri.parse("geo:"+info.getLng()+","+info.getLat()+"?q="+info.getAddress());*/
          /*      double[] point = AMapUtil.bd_encrypt(Double.valueOf(info.getLat()),Double.valueOf(info.getLng()),0,0);
                Uri mUri = Uri.parse("geo:"+point[1]+","+point[0]+"?q="+info.getAddress());
                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);*/
                Intent mIntent = new Intent(ShopDetailActivity.this, GeocoderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("addInfo", info);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
            } else if (mID == R.id.Shop_details_bottom_phone) {  //确认电话弹出框
                AlertDialog.Builder builer = new AlertDialog.Builder(ShopDetailActivity.this);
                builer.setMessage("是否拨打电话  " + info.getContactPhone());
                builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.getContactPhone()));
                        startActivity(intent);
                    }
                });
                builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builer.create();
                dialog.show();
            } else if (mID == R.id.Shop_details_bottom_sms) { //sms 模板
                Intent intent = new Intent(ShopDetailActivity.this,
                        ChatActivity.class);
                Bundle mBundle = new Bundle();
                SmsCursor.Person_Sms person_sms = new SmsCursor.Person_Sms();
                person_sms.Name = person_sms.Number = info.getContactPhone();
                mBundle.putSerializable("chatperson", person_sms);
                mBundle.putString("msg", "你好，我对你在诺部落上的店铺\"" + info.getDianpuName() + " \"中的信息很感兴趣,想和你详细了解一下。");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        }
    }
}
