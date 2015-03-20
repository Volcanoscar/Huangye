package com.nuobuluo.huangye.bean;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/2/6.
 * 发布信息封装类
 */
public class Publish {
    private String title;
    private String content;
    private String level1Code;
    private String level2Code;
    private List<String> typeList=new ArrayList<String>();
    private List<String> district= new ArrayList<String>();
    private List<String> bizArea= new ArrayList<String>();
    private List<String> img= new ArrayList<String>();
    private String qq;
    private String weixin;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public void addTypeList(String temp) {
        typeList.add(temp);
    }

    public List<String> getDistrict() {
        return district;
    }

    public void setDistrict(List<String> district) {
        this.district = district;
    }

    public void addDistrict(String dis) {
        this.district.add(dis);
    }

    public void clearDistrict() {
        this.district.clear();
    }

    public List<String> getBizArea() {
        return bizArea;
    }

    public void setBizArea(List<String> bizArea) {
        this.bizArea = bizArea;
    }

    public void addBizArea(String temp) {
        bizArea.add(temp);
    }
    public void clearBizArea() {
        bizArea.clear();
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    public static String toJson(Publish publish) {
        Gson json = new Gson();
       return json.toJson(publish);
    }

    public void clearTypeList() {
        typeList.clear();
    }
}
