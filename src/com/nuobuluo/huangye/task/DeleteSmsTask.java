package com.nuobuluo.huangye.task;

import android.os.AsyncTask;
import com.nuobuluo.huangye.utils.Utils;

public class DeleteSmsTask extends AsyncTask<Void, Integer, Void>{
	int SmsId;
	public DeleteSmsTask(Integer Id) {
		SmsId = Id;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Utils.DeleteSms(SmsId);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}
	
}
