package com.nuo.bean;

import java.util.Date;

/**
 * Created by zxl on 2015/1/27.
 * 店铺信息
 */
public class ShopInfo extends EntityBase{
    private String dianpuId;
    private String dianpuName;
    private Date createTime;
    /**
     * 营业时间
     */
    private Date yingyeTime;
    /**
     * 联系电话
     */
    private String contactPhone;
    private String desc;
    private String img;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 商圈代码
     */
    private String cityCode;
    /**
     * 商圈名称
     */
    private String cityName;
    /**
     * 区域名称
     */
    private String districtName;
    /**
     * 区域代码
     */
    private String districtCode;
    /**
     * 地址
     */
    private String address;
    /**
     * 其他联系方式
     */
    private String contactTel;
    private String url;
    /**
     * 服务特色
     */
    private String fuwutese;
    private String weixin;
    private String contactPerson;
    private String qq;

    public String getDianpuId() {
        return dianpuId;
    }

    public void setDianpuId(String dianpuId) {
        this.dianpuId = dianpuId;
    }

    public String getDianpuName() {
        return dianpuName;
    }

    public void setDianpuName(String dianpuName) {
        this.dianpuName = dianpuName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getYingyeTime() {
        return yingyeTime;
    }

    public void setYingyeTime(Date yingyeTime) {
        this.yingyeTime = yingyeTime;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFuwutese() {
        return fuwutese;
    }

    public void setFuwutese(String fuwutese) {
        this.fuwutese = fuwutese;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
