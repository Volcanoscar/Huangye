package com.nuo.bean;

/**
 * Created by zxl on 15-1-4.
 * 商圈对象
 */
public class BizArea {
    /** 商圈id**/
    private Integer bizareaId;
    /** 所属区域代码**/
    private String districtCode;
    /**商圈名称**/
    private String bizareaName;

    public Integer getBizareaId() {
        return bizareaId;
    }

    public void setBizareaId(Integer bizareaId) {
        this.bizareaId = bizareaId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getBizareaName() {
        return bizareaName;
    }

    public void setBizareaName(String bizareaName) {
        this.bizareaName = bizareaName;
    }
}
