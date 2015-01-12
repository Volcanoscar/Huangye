package com.fujie.module.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.fujie.module.horizontalListView.HorizontalListViewAdapter;
import com.fujie.module.horizontalListView.ViewBean;
import com.fujie.module.titlebar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 15-1-5.
 * 过滤条件tab. <br></br>根据数据生成不成类型的Tab。
 */
public class FilterTabLayout extends RelativeLayout{
    private static final String TAG = "FilterTabLayout";
    private Context mContext;
    private HorizontalListView horizontalListView;
    private HorizontalListViewAdapter tabAdapter;


    public FilterTabLayout(Context context){
        super(context);
        mContext=context;
        initView();
    }

    public FilterTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }
    private void initView(){

        LayoutInflater.from(mContext).inflate(R.layout.filter_tab_view, this);
        horizontalListView=(HorizontalListView) findViewById(R.id.horizontalListView);
        tabAdapter = new HorizontalListViewAdapter(mContext, new ArrayList<ViewBean>());
        horizontalListView.setAdapter(tabAdapter);

    }

    /**
     * 根据数据是否有子数据来判 断是否有子菜单。
     * 1.生成表头
     * 2.根据数据生成表头对应的弹出菜单
     *
     * @param viewBeanList
     */
    public void setData(List<ViewBean> viewBeanList){
        if (tabAdapter == null) {
            tabAdapter = new HorizontalListViewAdapter(mContext,viewBeanList);
            horizontalListView.setAdapter(tabAdapter);
        }
        tabAdapter.setViewBeanList(viewBeanList);
        tabAdapter.notifyDataSetChanged();
    }


}
