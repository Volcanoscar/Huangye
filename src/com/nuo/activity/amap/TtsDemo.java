package com.nuo.activity.amap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.iflytek.cloud.*;
import com.iflytek.sunflower.FlowerCollector;
import com.nuo.activity.R;

public class TtsDemo extends Activity implements OnClickListener {
	private static String TAG = "TtsDemo"; 	
	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认发音人
	private String voicer="xiaoyan";
    // 语音+安装助手类
    ApkInstaller mInstaller ;
	private String[] cloudVoicersEntries;
	private String[] cloudVoicersValue ;
	
	//缓冲进度
	private int mPercentForBuffering = 0;	
	//播放进度
	private int mPercentForPlaying = 0;
	
	// 云端/本地选择按钮
	private RadioGroup mRadioGroup;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
;
	
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	
	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ttsdemo);
		initLayout();

		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		
		// 云端发音人名称列表
		/*cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
		cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);*/
				
		mSharedPreferences = getSharedPreferences("", Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
		
		mInstaller = new  ApkInstaller(TtsDemo.this);
	}

	/**
	 * 初始化Layout。
	 */
	private void initLayout() {
		findViewById(R.id.tts_play).setOnClickListener(this);
		
		findViewById(R.id.tts_cancel).setOnClickListener(this);
		findViewById(R.id.tts_pause).setOnClickListener(this);
		findViewById(R.id.tts_resume).setOnClickListener(this);
		findViewById(R.id.image_tts_set).setOnClickListener(this);

		findViewById(R.id.tts_btn_person_select);
		findViewById(R.id.tts_btn_person_select).setOnClickListener(this);
		
		mRadioGroup=((RadioGroup) findViewById(R.id.tts_rediogroup));
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tts_radioCloud:
					mEngineType = SpeechConstant.TYPE_CLOUD;
					break;
				case R.id.tts_radioLocal:
					mEngineType =  SpeechConstant.TYPE_LOCAL;
					/**
					 * 选择本地合成
					 * 判断是否安装语音+,未安装则跳转到提示安装页面
					 */
					if (!SpeechUtility.getUtility().checkServiceInstalled()) {
						mInstaller.install();
					}
					break;
				default:
					break;
				}

			}
		} );
	}	

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.image_tts_set:
			if(SpeechConstant.TYPE_CLOUD.equals(mEngineType)){
			/*	Intent intent = new Intent(TtsDemo.this, TtsSettings.class);
				startActivity(intent);*/
			}else{
				// 本地设置跳转到语音+中
				if (!SpeechUtility.getUtility().checkServiceInstalled()) {
					mInstaller.install();
				}else {
					SpeechUtility.getUtility().openEngineSettings(null);				
				}
			}
			break;
		// 开始合成
		case R.id.tts_play:
			String text = ((EditText) findViewById(R.id.tts_text)).getText().toString();
			// 设置参数
			setParam();
			int code = mTts.startSpeaking(text, mTtsListener);
			if (code != ErrorCode.SUCCESS) {
				if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
					//未安装则跳转到提示安装页面
					mInstaller.install();
					showTip("语音合成失败,错误码: " + code);
				}else {
					showTip("语音合成失败,错误码: " + code);	
				}
			}
			break;
		// 取消合成
		case R.id.tts_cancel:
			mTts.stopSpeaking();
			break;
		// 暂停播放
		case R.id.tts_pause:
			mTts.pauseSpeaking();
			break;
		// 继续播放
		case R.id.tts_resume:
			mTts.resumeSpeaking();
			break;
		// 选择发音人
		case R.id.tts_btn_person_select:
			showPresonSelectDialog();
			break;
		}
	}

	
	private int selectedNum=0;
	/**
	 * 发音人选择。
	 */
	private void showPresonSelectDialog() {
		switch (mRadioGroup.getCheckedRadioButtonId()) {
		// 选择在线合成
		case R.id.tts_radioCloud:			
			new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
			.setSingleChoiceItems(cloudVoicersEntries, // 单选框有几项,各是什么名字
					selectedNum, // 默认的选项
					new DialogInterface.OnClickListener() { // 点击单选框后的处理
				public void onClick(DialogInterface dialog,
						int which) { // 点击了哪一项
					voicer = cloudVoicersValue[which];
					if ("catherine".equals(voicer) || "henry".equals(voicer) || "vimary".equals(voicer)) {
						/* ((EditText) findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);*/
					}else {
						((EditText) findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
					}
					selectedNum = which;
					dialog.dismiss();
				}
			}).show();
			break;
			
		// 选择本地合成
		case R.id.tts_radioLocal:
			if (!SpeechUtility.getUtility().checkServiceInstalled()) {
				mInstaller.install();
			}else {
				SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);				
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}		
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			mPercentForBuffering = percent;
			showTip(String.format("",
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			mPercentForPlaying = percent;
			showTip(String.format("",
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if(error == null)
			{
				showTip("播放完成");
			}
			else if(error != null)
			{
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			
		}
	};

	private void showTip(final String str)
	{
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * @param
	 * @return 
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		//设置合成
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD))
		{
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			//设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME,voicer);
			
			//设置语速
			mTts.setParameter(SpeechConstant.SPEED,mSharedPreferences.getString("speed_preference", "50"));

			//设置音调
			mTts.setParameter(SpeechConstant.PITCH,mSharedPreferences.getString("pitch_preference", "50"));

			//设置音量
			mTts.setParameter(SpeechConstant.VOLUME,mSharedPreferences.getString("volume_preference", "50"));
			
			//设置播放器音频流类型
			mTts.setParameter(SpeechConstant.STREAM_TYPE,mSharedPreferences.getString("stream_preference", "3"));
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			//设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME,"");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
	}
	
	@Override
	protected void onResume() {
		//移动数据统计分析
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart("TtsDemo");
		super.onResume();
	}
	@Override
	protected void onPause() {
		//移动数据统计分析
		FlowerCollector.onPageEnd("TtsDemo");
		FlowerCollector.onPause(this);
		super.onPause();
	}
}
