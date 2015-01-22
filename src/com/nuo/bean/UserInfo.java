package com.nuo.bean;

import com.google.gson.Gson;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;
import java.util.Map;

/**
 * Created by zxl on 14-12-17.
 */
@Table(name = "sys_user_info")
public class UserInfo extends EntityBase{
    @Column(column = "create_time")
    private Date createTime;
    @Column(column = "phone")
    private String phone;
    @Column(column = "weixin")
    private String weixin;
    @Column(column = "email")
    private String email;
    @Column(column = "user_id")
    private String userId;
    @Column(column = "user_name")
    private String userName;
    @Column(column = "password")
    private String password;
    @Column(column = "qq")
    private String qq;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public static UserInfo parseMap(String result) {
        Gson gson = new Gson();
        Map map = gson.fromJson(result, Map.class);
        if ("false".equals(map.get("result").toString())) {
            return null;
        }
        return gson.fromJson(result, UserInfo.class);
    }
}

