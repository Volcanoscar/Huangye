package com.nuo.utils;


import com.nuo.activity.R;

public enum StatusMode {
	dnd(R.string.status_dnd), // 请勿打扰
	xa(R.string.status_xa), // 隐身
	away(R.string.status_away), // 离开
	available(R.string.status_online), // 在线
	chat(R.string.status_chat);// Q我吧

	private final int textId;

	StatusMode(int textId) {
		this.textId = textId;
	}

	public int getTextId() {
		return textId;
	}

	public String toString() {
		return name();
	}

	public static StatusMode fromString(String status) {
		return StatusMode.valueOf(status);
	}

}
