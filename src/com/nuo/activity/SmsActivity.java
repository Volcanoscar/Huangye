
package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.ContentObserver.SmssContentObserver;
import com.nuo.utils.Utils;

public class SmsActivity extends Activity {
	//信息的列表
	private ListView m_smsslist;
	//短信息内容观察者
	private SmssContentObserver SmssCO;
	//新增短信息按钮
	private ImageButton m_NewSmsBtn;

	@ViewInject(R.id.title_bar)
	private TitleBarView mTitleBarView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smsfragment);
		ViewUtils.inject(this);
		SmssCO = new SmssContentObserver(new Handler());
		getContentResolver().registerContentObserver(Uri.parse("content://sms/") , false, SmssCO);
		m_smsslist = (ListView)findViewById(R.id.sms_list);
		m_smsslist.setAdapter(Utils.m_smsadapter);
		m_smsslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Intent intent = new Intent(SmsActivity.this,ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("chatperson", Utils.mPersonSmsList.get(arg2));
				intent.putExtras(mBundle);
				startActivity(intent);
			}
		});
		// title
		initView();
	}
	private void initView() {
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		mTitleBarView.setTitleText(R.string.sms);
		mTitleBarView.getBtnRight().setPadding(8, 0, 8, 0);
		mTitleBarView.getBtnRight().setTextColor(getResources().getColor(R.color.white));
		mTitleBarView.setBtnRight(R.drawable.btn_compose_msg, R.string.add_sms);
		mTitleBarView.setBtnRightBg(R.drawable.login_btn);
		mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SmsActivity.this, NewSmsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/** 加载 动作栏 菜单**/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
