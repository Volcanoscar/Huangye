package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.bean.District;
import com.nuobuluo.huangye.bean.MsgInfo;
import com.nuobuluo.huangye.bean.MsgSearchBean;
import com.nuobuluo.huangye.bean.PageSearchResult;
import com.nuobuluo.huangye.bean.ShengHuoType;
import com.nuobuluo.module.horizontalListView.ViewBean;
import com.nuobuluo.module.tab.FilterTabClickListener;
import com.nuobuluo.module.tab.FilterTabLayout;
import com.nuobuluo.module.xlistview.XListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nuobuluo.huangye.adapter.ShopAdapter;
import com.nuobuluo.huangye.handler.TimeOutHandler;
import com.nuobuluo.huangye.utils.NetUtil;
import com.nuobuluo.huangye.utils.PreferenceConstants;
import com.nuobuluo.huangye.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺列表界面
 * 过滤条件和第三级分类以tab菜单的形式显示
 */
public class HuangyeListActivity extends Activity implements XListView.IXListViewListener {

    private XListView mListView;
    private ImageView mShoplist_back;
    private FilterTabLayout filterTabLayout;
    private List<MsgInfo> list = new ArrayList<MsgInfo>();
    private ShopAdapter mAdapter = null;
    private TextView mShoplist_title_txt;
    private List<ViewBean> districtList= new ArrayList<ViewBean>();
    private String typeCode;
    /** 过滤tab数据集合 **/
    private List<ViewBean> filterTabViewBeanList = new ArrayList<ViewBean>();
    private TimeOutHandler timeOutHandler= new TimeOutHandler(this);
    private String parentTypeCode;
    private String typeName;
    /**
     * 关键字搜索
     */
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shoplist);
        initData();
        initView();
    }

    /**
     * 得到传入此界面的数据
     */
    private void initData() {
        //得到变量
        typeCode = getIntent().getStringExtra("typeCode");
        typeName = getIntent().getStringExtra("typeName");
        key = getIntent().getStringExtra("key");
        parentTypeCode = getIntent().getStringExtra("parentTypeCode");
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
                T.showShort(HuangyeListActivity.this, R.string.net_error);
            }
        });
    }

    private void initView() {
        filterTabLayout = (FilterTabLayout) findViewById(R.id.filterTabLayout);
        mShoplist_back = (ImageView) findViewById(R.id.Shoplist_back);
        mShoplist_title_txt = (TextView) findViewById(R.id.Shoplist_title_txt);
        mShoplist_title_txt.setText(typeName);
        mListView = (XListView) findViewById(R.id.ShopListView);
        mListView.setPullRefreshEnable(false);
        MyOnclickListener mOnclickListener = new MyOnclickListener();
        mShoplist_back.setOnClickListener(mOnclickListener);
        // -----------------------------------------------------------------
        mAdapter = new ShopAdapter(list, HuangyeListActivity.this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new MainListOnItemClickListener());
        mListView.setXListViewListener(this);
        queryDistrictData();
        getData(1); //请求第一页数据
        //tab 点击事件
        filterTabLayout.setFilterTabClickListener(new FilterTabClickListener() {
            @Override
            public void onClick(List<ViewBean> viewBeanList) {
                selectedViewBean = viewBeanList;
                //清空列表数据
                list.clear();
                currPage=1;

                getData(1);
            }
        });
    }
    private List<ViewBean> selectedViewBean =null;
    private Integer currPage =1;

    /**
     * 是否显示加载弹出框
     * 1 显示
     * 0 不显示
     * @param isShowDialog
     */
    private void getData(final int isShowDialog) {
        MsgSearchBean msgSearchBean= new MsgSearchBean();
        msgSearchBean.setLevelOneTypeCode(parentTypeCode);
        msgSearchBean.setLevelTwoTypeCode(typeCode);
        msgSearchBean.setParam(String.valueOf(currPage));
        msgSearchBean.setKey(key);
        NetUtil.searchMsg(msgSearchBean,selectedViewBean,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mListView.stopLoadMore();
                PageSearchResult newList = PageSearchResult.parseMap(responseInfo.result);
                if (newList != null&&newList.getList()!=null&&!newList.getList().isEmpty()) {
                    list.addAll(newList.getList());
                    currPage++;
                    if (newList.getList().size() < 10) {  //TODO:... 每次加载都是十条。。。。。
                        mListView.setPullLoadEnable(false);
                    }else{
                        mListView.setPullLoadEnable(true);
                    }
                }else{
                    mListView.setPullLoadEnable(false);
                }
                mAdapter.notifyDataSetChanged();
                if (isShowDialog==1) {
                    timeOutHandler.stop();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(HuangyeListActivity.this,R.string.net_error);
                if (isShowDialog==1) {
                    timeOutHandler.stop();
                }

            }

            @Override
            public void onStart() {
                if (isShowDialog==1) {
                    timeOutHandler.start(null);
                }
                super.onStart();
            }
        });
    }

    @Override
    public void onRefresh() {
        getData(0);
    }

    @Override
    public void onLoadMore() {
        getData(0);
    }

    private class MyOnclickListener implements View.OnClickListener {
        public void onClick(View v) {
            int mID = v.getId();
            if (mID == R.id.Shoplist_back) {
                HuangyeListActivity.this.finish();
            }
        }
    }

    private class MainListOnItemClickListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            if (list != null&&list.size()!=0) {
                Intent intent = new Intent(HuangyeListActivity.this, InfoDetailActivity.class);
                Bundle bund = new Bundle();
                bund.putSerializable("ShopInfo", list.get(arg2-1));   //因为xlistview的第一个view是HeaderView，所以此处arg2需要减1才能与list中的数据对应。
                intent.putExtra("value", bund);
                startActivity(intent);
            }

        }
    }
}
