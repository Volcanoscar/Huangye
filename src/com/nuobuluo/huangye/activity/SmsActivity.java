
package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.lidroid.xutils.ViewUtils;
import com.nuobuluo.huangye.ContentObserver.SmssContentObserver;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.utils.Utils;

public class SmsActivity extends Activity {
	//信息的列表
	private ListView m_smsslist;
	//短信息内容观察者
	private SmssContentObserver SmssCO;
	//新增短信息按钮
	private ImageButton m_NewSmsBtn;
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
