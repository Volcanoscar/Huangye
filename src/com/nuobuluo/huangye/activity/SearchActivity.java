package com.nuobuluo.huangye.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.adapter.SwipeAdapter;
import com.nuobuluo.huangye.bean.KeySearchResult;
import com.nuobuluo.huangye.handler.TimeOutHandler;
import com.nuobuluo.huangye.utils.NetUtil;
import com.nuobuluo.huangye.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/1/26.
 * 信息查询 界面
 */
public class SearchActivity extends Activity {
    @ViewInject(R.id.search_list)
    private ListView searchList;

    @ViewInject(R.id.empty_tip)
    private TextView emptyTip;
    private TimeOutHandler timeOutHandler= new TimeOutHandler(this);
    private SwipeAdapter mAdapter;
    private List<KeySearchResult> data = new ArrayList<KeySearchResult>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtils.inject(this);
        initActionBar();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action, menu);
        MenuItem searchItem =menu.findItem(R.id.action_search);
        ///是搜索框默认展开
        searchItem.expandActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                return false;
            }
        });
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 查询事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryData(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }


        });
        return true;
    }

    /**
     * 查询数据
     */
    private void queryData(final String key) {
        NetUtil.searchMsgCount(key, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                timeOutHandler.stop();
                List<KeySearchResult> searchResults = KeySearchResult.parseMap(responseInfo.result);
                if (searchResults != null && searchResults.size() != 0) {
                    searchList.setVisibility(View.VISIBLE);
                    emptyTip.setVisibility(View.GONE);
                    mAdapter.setDate(searchResults);
                    mAdapter.setKey(key);
                    mAdapter.notifyDataSetChanged();
                } else {
                    searchList.setVisibility(View.GONE);
                    emptyTip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                timeOutHandler.stop();
                T.showShort(SearchActivity.this, R.string.net_error);
            }

            @Override
            public void onStart() {
                timeOutHandler.start(null);
                super.onStart();
            }
        });

    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
    }
    /**
     * 初始化界面
     */
    private void initView() {
        mAdapter = new SwipeAdapter(this,data);
        searchList.setAdapter(mAdapter);
    }

}