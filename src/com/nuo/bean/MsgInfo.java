package com.nuo.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuo.utils.JsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 查询出来的信息
 * */
public class MsgInfo extends EntityBase implements Serializable {
	private String userId;
	private String title;
	private String content;
	private String level1Code;
	private String level2Code;
	private String phone;
	private String createTime;
	private String photo;
	private String dianpuName;
	private String imgCount;
	private String weixin;
	private String qq;

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDianpuName() {
		return dianpuName;
	}

	public void setDianpuName(String dianpuName) {
		this.dianpuName = dianpuName;
	}

	public String getImgCount() {
		return imgCount;
	}

	public void setImgCount(String imgCount) {
		this.imgCount = imgCount;
	}

	public static List<MsgInfo> parseMap(String result) {
		Gson gson = JsonUtil.getDateGson();
		return gson.fromJson(result, new TypeToken<List<MsgInfo>>(){}.getType());
	}
}
