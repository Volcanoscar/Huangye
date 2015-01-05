package com.fujie.module.horizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 15-1-4.
 *
 * 用于构建过滤所用类
 */
public class ViewBean {

    private String text;
    private String id;
    private String img;
    private List<ViewBean> bizAreaList;

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
