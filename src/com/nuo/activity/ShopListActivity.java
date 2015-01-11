package com.nuo.activity;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fujie.module.tab.FilterTabLayout;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nuo.adapter.SearchMainAdapter;
import com.nuo.adapter.SearchMoreAdapter;
import com.nuo.adapter.ShopAdapter;
import com.nuo.bean.District;
import com.fujie.module.horizontalListView.ViewBean;
import com.nuo.bean.ShengHuoType;
import com.nuo.info.ShopInfo;
import com.nuo.model.Model;
import com.nuo.net.MyGet;
import com.nuo.net.ThreadPoolUtils;
import com.nuo.thread.HttpGetThread;
import com.nuo.utils.MyJson;
import com.nuo.utils.NetUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.T;

/**
 * 店铺列表模块
 */
public class ShopListActivity extends Activity {

    private ListView mListView;
    private ImageView mShoplist_back;
    private LinearLayout mShoplist_shanghuleixing;
    private FilterTabLayout filterTabLayout;
    private MyGet myGet = new MyGet();
    private MyJson myJson = new MyJson();
    private List<ShopInfo> list = new ArrayList<ShopInfo>();
    private ShopAdapter mAdapter = null;
    private SearchMoreAdapter topadapter = null;
    private SearchMoreAdapter threeadapter = null;
    private SearchMoreAdapter bizAreaAdapter = null;
    private SearchMainAdapter districtAdapter = null;
    private SearchMoreAdapter twoadapter2 = null;
    private SearchMainAdapter oneadapter2 = null;
    private Button ListBottem = null;
    private ImageView mSearch_city_img;
    private TextView mShoplist_title_txt;
    private int mStart = 1;
    private int mEnd = 5;
    private String url = null;
    private boolean flag = true;
    private boolean listBottemFlag = true;
    private List<ViewBean> districtList= new ArrayList<ViewBean>();
    private List<ViewBean> mainList2 = new ArrayList<ViewBean>();
    private String typeCode;
    /** 过滤tab数据集合 **/
    private List<ViewBean> filterTabViewBeanList = new ArrayList<ViewBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shoplist);
        initView();
        initData();
    }

    private void initData() {
        //得到变量
         typeCode = getIntent().getStringExtra("typeCode");
        queryDistrictData();
    }

    private void queryShenghuoTypeData() {
        //得到过滤信息(区域以及三级分类)
        NetUtil.shenhuoType(PreferenceConstants.SHENHUO_TYPE_THREE, typeCode, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                //判断是否有三级分类、判断是否有四级分类(判断规则如下：一旦有一个三级分类有四级分类，则所有的三级分类有四级分类)
                List<ShengHuoType> shengHuoTypeList = ShengHuoType.parseMap(stringResponseInfo.result);
                if (shengHuoTypeList != null && !shengHuoTypeList.isEmpty()) {
                    if (PreferenceConstants.IS_PARENT_TRUE.equals(shengHuoTypeList.get(0).getIsParent())) { //如果有四级菜单
                        filterTabViewBeanList.addAll(ShengHuoType.getlevel4ViewBean(shengHuoTypeList));
                    } else {
                        filterTabViewBeanList.add(ShengHuoType.getlevel3ViewBean(shengHuoTypeList));
                    }
                }
                filterTabLayout.setData(filterTabViewBeanList);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    private void queryDistrictData() {
        //得到区域信息
        NetUtil.queryDistrictList(true, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                //初始化数据
                List<District> removeDistrictList = District.parseMap(stringResponseInfo.result);
                List<ViewBean> mapList = District.convertDistrictToViewBean(removeDistrictList);
                districtList.addAll(mapList);
                //初始化filterTabLayout
                ViewBean viewBean =ViewBean.getDistrictViewBean();
                viewBean.setBizAreaList(mapList);
                filterTabViewBeanList.add(viewBean);
                queryShenghuoTypeData();
            }
            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(ShopListActivity.this, R.string.net_error);
            }
        });
    }

    private void initView() {
        filterTabLayout = (FilterTabLayout) findViewById(R.id.filterTabLayout);
        mShoplist_back = (ImageView) findViewById(R.id.Shoplist_back);
        mShoplist_shanghuleixing = (LinearLayout) findViewById(R.id.Shoplist_shanghuleixing);
        mShoplist_title_txt = (TextView) findViewById(R.id.Shoplist_title_txt);
        mSearch_city_img = (ImageView) findViewById(R.id.Search_city_img);
        mListView = (ListView) findViewById(R.id.ShopListView);
        MyOnclickListener mOnclickListener = new MyOnclickListener();
        mShoplist_back.setOnClickListener(mOnclickListener);
        // -----------------------------------------------------------------
        mAdapter = new ShopAdapter(list, ShopListActivity.this);
        ListBottem = new Button(ShopListActivity.this);
        ListBottem.setText("点击加载更多");
        ListBottem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag && listBottemFlag) {
                    url = Model.SHOPURL + "start=" + mStart + "&end=" + mEnd;
                    ThreadPoolUtils.execute(new HttpGetThread(hand, url));
                    listBottemFlag = false;
                } else if (!listBottemFlag)
                    Toast.makeText(ShopListActivity.this, "加载中请稍候", 1).show();
            }
        });
        mListView.addFooterView(ListBottem, null, false);
        ListBottem.setVisibility(View.GONE);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new MainListOnItemClickListener());
        // 拼接字符串操作
        url = Model.SHOPURL + "start=" + mStart + "&end=" + mEnd;
        ThreadPoolUtils.execute(new HttpGetThread(hand, url));
    }

    private class MyOnclickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();
            if (mID == R.id.Shoplist_back) {
                ShopListActivity.this.finish();
            }
        }
    }

    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404) {
                Toast.makeText(ShopListActivity.this, "找不到地址", 1).show();
                listBottemFlag = true;
            } else if (msg.what == 100) {
                Toast.makeText(ShopListActivity.this, "传输失败", 1).show();
                listBottemFlag = true;
            } else if (msg.what == 200) {
                String result = (String) msg.obj;
                // 在activity当中获取网络交互的数据
                if (result != null) {
                    // 1次网络请求返回的数据
                    List<ShopInfo> newList = myJson.getShopList(result);
                    if (newList != null) {
                        if (newList.size() == 5) {
                            ListBottem.setVisibility(View.VISIBLE);
                            mStart += 5;
                            mEnd += 5;
                        } else {
                            ListBottem.setVisibility(View.GONE);
                        }
                        for (ShopInfo info : newList) {
                            list.add(info);
                        }
                        mAdapter.notifyDataSetChanged();
                        listBottemFlag = true;
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        ;
    };

    private class MainListOnItemClickListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Intent intent = new Intent(ShopListActivity.this, ShopDetailsActivity.class);
            Bundle bund = new Bundle();
            bund.putSerializable("ShopInfo", list.get(arg2));
            intent.putExtra("value", bund);
            startActivity(intent);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return false;
    }

}
