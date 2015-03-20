package com.nuobuluo.huangye.activity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;

import com.nuobuluo.huangye.R;

/**
 *
 * */
public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
        if (isFristRun()) {
            //TODO: չʾȥ������ҳ��
            Intent intent = new Intent(StartActivity.this,
                    FrameActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(StartActivity.this,
                    FrameActivity.class);
            startActivity(intent);
        }
        finish();
	};

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (isFristRun()) {
				Intent intent = new Intent(StartActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(StartActivity.this,
						FrameActivity.class);
				startActivity(intent);
			}
			finish();
		};
	};

	private boolean isFristRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (!isFirstRun) {
			return false;
		} else {
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}
}
