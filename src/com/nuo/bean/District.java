package com.nuo.bean;

import com.fujie.module.horizontalListView.ViewBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 15-1-4.
 * 区域对象
 */
public class District {

    /**
     * 区域名称 *
     */
    private String districtName;
    /**
     * 区域代码 *
     */
    private String districtCode;
    /**
     * 上级区域代码 *
     */
    private String parentCode;
    /**
     * 是否是系统默认区域，1：是（新乡市）、0：不是 *
     */
    private Integer top;
    /**
     * 商圈列表 *
     */
    private List<BizArea> bizAreaList;


    public static List<District> parseMap(String result) {
        List<District> districts = new ArrayList<District>();
        District district = new District();
        district.setDistrictName("新乡市");
        districts.add(district);

        Gson gson = new Gson();
        List<District> resultDistrictList = gson.fromJson(result, new TypeToken<List<District>>() {
        }.getType());
        for (District district1 : resultDistrictList) {
            List<BizArea> bizAreas = new ArrayList<BizArea>();
            BizArea bizArea = new BizArea();
            bizArea.setBizareaName("全部商圈");
            bizAreas.add(bizArea);
            bizAreas.addAll(district1.getBizAreaList());
            district1.setBizAreaList(bizAreas);
        }
        districts.addAll(resultDistrictList);
        return districts;
    }

    public static List<District> parseFabuMap(String result) {
        List<District> districts = new ArrayList<District>();
        Gson gson = new Gson();
        List<District> resultDistrictList = gson.fromJson(result, new TypeToken<List<District>>() {
        }.getType());
        for (District district1 : resultDistrictList) {
            List<BizArea> bizAreas = new ArrayList<BizArea>();
            BizArea bizArea = new BizArea();
            bizArea.setBizareaName("全部商圈");
            bizArea.setDistrictCode(district1.getDistrictCode());
            bizAreas.add(bizArea);
            bizAreas.addAll(district1.getBizAreaList());
            district1.setBizAreaList(bizAreas);
        }
        districts.addAll(resultDistrictList);
        return districts;
    }

    public static List<ViewBean> convertDistrictToViewBean(List<District> removeDistrictList) {
        List<ViewBean> mapList = new ArrayList<ViewBean>();
        for (final District district : removeDistrictList) {
            ViewBean viewBean = new ViewBean();
            viewBean.setText(district.getDistrictName());
            viewBean.setId(district.getDistrictCode());
            List<ViewBean> chirdViewBean = new ArrayList<ViewBean>();
            if (district.getBizAreaList() != null) {
                for (BizArea bizArea : district.getBizAreaList()) {
                    ViewBean temp = new ViewBean();
                    temp.setText(bizArea.getBizareaName());
                    if (bizArea.getBizareaId() != null) {
                        temp.setId(bizArea.getBizareaId().toString());
                    }
                    temp.setParentId(bizArea.getDistrictCode());
                    chirdViewBean.add(temp);
                }
            }
            viewBean.setBizAreaList(chirdViewBean);
            mapList.add(viewBean);
        }
        return mapList;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public List<BizArea> getBizAreaList() {
        return bizAreaList;
    }

    public void setBizAreaList(List<BizArea> bizAreaList) {
        this.bizAreaList = bizAreaList;
    }
}
