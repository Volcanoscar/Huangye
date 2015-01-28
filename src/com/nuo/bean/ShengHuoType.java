package com.nuo.bean;

import android.content.Intent;
import com.fujie.module.horizontalListView.ViewBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-12-29.
 * 本地生活类型
 */
public class ShengHuoType {

    private String typeName;
    private String level;
    private Integer orderBy;
    private String level1Code;
    private String level2Code;
    private String level3Code;
    private Integer isParent;
    private String typeCode;
    private List<ShengHuoType> level4List;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getLevel2Code() {
        return level2Code;
    }

    public void setLevel2Code(String level2Code) {
        this.level2Code = level2Code;
    }

    public String getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(String level3Code) {
        this.level3Code = level3Code;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public List<ShengHuoType> getLevel4List() {
        return level4List;
    }

    public void setLevel4List(List<ShengHuoType> level4List) {
        this.level4List = level4List;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public static List<ShengHuoType> parseMap(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<List<ShengHuoType>>(){}.getType());
    }

    public static List<ViewBean> getlevel4ViewBean(List<ShengHuoType> shengHuoTypeList) {
        //添加“三级菜单”
        List<ViewBean> level3List = new ArrayList<ViewBean>();

        for (ShengHuoType shengHuoType : shengHuoTypeList) {
            ViewBean viewBean = new ViewBean();
            viewBean.setText(shengHuoType.getTypeName());
            List<ViewBean> chirdViewBean = new ArrayList<ViewBean>();
            ViewBean oneBean = new ViewBean();
            oneBean.setText("全部");
            chirdViewBean.add(oneBean);
            for (ShengHuoType level4Type : shengHuoType.getLevel4List()) {
                ViewBean temp = new ViewBean();
                temp.setText(level4Type.getTypeName());
                temp.setId(level4Type.getTypeCode());
                chirdViewBean.add(temp);
            }
            viewBean.setBizAreaList(chirdViewBean);
            level3List.add(viewBean);
        }
        return level3List;
    }

    public static ViewBean getlevel3ViewBean(List<ShengHuoType> shengHuoTypeList) {
        //添加“三级菜单”
        List<ViewBean> level3List = new ArrayList<ViewBean>();
        ViewBean oneBean = new ViewBean();
        oneBean.setText("全部");
        level3List.add(oneBean);
        for (ShengHuoType shengHuoType : shengHuoTypeList) {
            ViewBean viewBean = new ViewBean();
            viewBean.setText(shengHuoType.getTypeName());
            viewBean.setId(shengHuoType.getTypeCode());
            level3List.add(viewBean);
        }
        ViewBean categoryViewBean = ViewBean.getCategoryViewBean();
        categoryViewBean.setBizAreaList(level3List);
        return categoryViewBean;
    }
}
