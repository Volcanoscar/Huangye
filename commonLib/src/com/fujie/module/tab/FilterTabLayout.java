package com.fujie.module.tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fujie.common.SystemMethod;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.fujie.module.horizontalListView.ViewBean;
import com.fujie.module.titlebar.R;

import java.util.List;

/**
 * Created by zxl on 15-1-5.
 *
 * 过滤条件tab. <br></br>根据数据生成不成类型的Tab。
 */
public class FilterTabLayout extends RelativeLayout{
    private static final String TAG = "FilterTabLayout";
    private Context mContext;
    private HorizontalListView horizontalListView;
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
    }
    public void setData(List<ViewBean> viewBeanList){
        //districtAdapter = new SearchMainAdapter(ShopListActivity.this, districtList, R.layout.shop_list1_item, false);

    }
}
