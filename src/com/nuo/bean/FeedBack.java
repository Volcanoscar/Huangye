package com.nuo.bean;

import com.google.gson.Gson;

/**
 * Created by zxl on 2015/2/13.
 * 反馈信息
 */
public class FeedBack {
    private String content;
    private String userId;
    private String contact;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
