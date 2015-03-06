package com.nuo.activity.amap;

import android.content.Context;
import android.os.Bundle;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;

import com.iflytek.cloud.*;
import com.nuo.activity.R;
import com.nuo.utils.T;

/**
 * 语音播报组件
 *
 */
public class TTSController implements SynthesizerListener, AMapNaviListener {

	public static TTSController ttsManager;
	private Context mContext;
	// 合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;

	TTSController(Context context) {
		mContext = context;
	}

	public static TTSController getInstance(Context context) {
		if (ttsManager == null) {
			ttsManager = new TTSController(context);
		}
		return ttsManager;
	}
	/**
	 * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
	 * 
	 * @param
	 */
	public void playText(String playText) {
		if (!isfinish) {
			return;
		}
		if (null == mSpeechSynthesizer) {
			// 创建合成对象.
			mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext,mTtsInitListener);
			initSpeechSynthesizer();
		}
		// 进行语音合成.
		mSpeechSynthesizer.startSpeaking(playText,this);

	}
	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				T.showShort(mContext, "初始化失败,错误码："+code);
			}
		}
	};

	public void stopSpeaking() {
		if (mSpeechSynthesizer != null)
			mSpeechSynthesizer.stopSpeaking();
	}
	public void startSpeaking() {
		 isfinish=true;
	}

	private void initSpeechSynthesizer() {
		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
				mContext.getString(R.string.preference_default_tts_role));
		// 设置语速
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
				"" + mContext.getString(R.string.preference_key_tts_speed));
		// 设置音量
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
				"" + mContext.getString(R.string.preference_key_tts_volume));
		// 设置语调
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
				"" + mContext.getString(R.string.preference_key_tts_pitch));

	}

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener() {



		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {

			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}

		@Override
		public void onBufferReceived(byte[] bytes) {

		}
	};

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int i, int i1, int i2, Bundle bundle) {

	}

	boolean isfinish = true;



	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		isfinish = false;

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError speechError) {

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}



	public void destroy() {
		if (mSpeechSynthesizer != null) {
			mSpeechSynthesizer.stopSpeaking();
		}
	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		this.playText("到达目的地");
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		 this.playText("路径计算失败，请检查网络或输入参数");
	}

	@Override
	public void onCalculateRouteSuccess() {
		String calculateResult = "路径计算就绪";

		this.playText(calculateResult);
	}

	@Override
	public void onEndEmulatorNavi() {
		this.playText("导航结束");

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		this.playText(arg1);
	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub
        this.playText("导航错误");
	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		this.playText("前方路线拥堵，路线重新规划");
	}

	@Override
	public void onReCalculateRouteForYaw() {

		this.playText("您已偏航");
	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}


}
