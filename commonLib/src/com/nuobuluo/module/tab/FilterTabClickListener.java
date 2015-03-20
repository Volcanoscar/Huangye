package com.nuobuluo.module.tab;

import com.nuobuluo.module.horizontalListView.ViewBean;

import java.util.List;

/**
 * Created by zxl on 2015/1/27.
 *
 * 过滤tab点击事件
 */
public interface FilterTabClickListener {

    public void onClick(List<ViewBean> viewBean);
}
