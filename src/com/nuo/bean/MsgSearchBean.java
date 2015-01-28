package com.nuo.bean;

import com.fujie.module.horizontalListView.ViewBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxl on 2015/1/26.
 * 封装了搜索信息的条件
 */
public class MsgSearchBean {
    /**
     * 区域d
     */
    private String district;
    /**
     * 商圈a
     */
    private String area;
    /**
     * 关键字 k
     */
    private String key;
    /**
     * 只看今天today=0|1
     */
    private String today;
    /**
     * 只看有图img=0|1
     */
    private String img;
    /**
     * 分页参数 p
     */
    private String param;

    private String levelOneTypeCode;
    private String levelTwoTypeCode;

    public String getLevelOneTypeCode() {
        return levelOneTypeCode;
    }

    public void setLevelOneTypeCode(String levelOneTypeCode) {
        this.levelOneTypeCode = levelOneTypeCode;
    }

    public String getLevelTwoTypeCode() {
        return levelTwoTypeCode;
    }

    public void setLevelTwoTypeCode(String levelTwoTypeCode) {
        this.levelTwoTypeCode = levelTwoTypeCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    /**
     * 非空属性封装到Map中
     *
     * @return
     */
    public Map<String, String> toMap() {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (district != null && !district.isEmpty()) {
            resultMap.put("d", district);
        }
        if (param != null && !param.isEmpty()) {
            resultMap.put("p", param);
        }
        if (area != null && !area.isEmpty()) {
            resultMap.put("a", area);
        }
        if (today != null && !today.isEmpty()) {
            resultMap.put("today", today);
        }
        if (img != null && !img.isEmpty()) {
            resultMap.put("img", img);
        }
        if (key != null && !key.isEmpty()) {
            resultMap.put("k", key);
        }
        return resultMap;
    }
}
