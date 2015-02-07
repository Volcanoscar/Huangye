package com.fujie.module.horizontalListView;

import com.fujie.module.titlebar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 15-1-4.
 * <p/>
 * 用于构建过滤所用类
 */
public class ViewBean {

    private String text;
    private String id;
    private String img;
    /**
     * 是否是二级菜单
     */
    private boolean parent;

    private String parentId;
    private List<ViewBean> bizAreaList;
    private static ViewBean categoryViewBean;
    private static ViewBean districtViewBean;

    static {
        categoryViewBean = new ViewBean();
        categoryViewBean.setText("分类");
        districtViewBean = new ViewBean();
        districtViewBean.setText("区域");
        districtViewBean.setParent(true);
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static ViewBean getCategoryViewBean() {
        return categoryViewBean;
    }

    public static ViewBean getDistrictViewBean() {
        return districtViewBean;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<ViewBean> getBizAreaList() {
        return bizAreaList;
    }

    public void setBizAreaList(List<ViewBean> bizAreaList) {
        this.bizAreaList = bizAreaList;
    }


}
