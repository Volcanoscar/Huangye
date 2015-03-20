package com.nuobuluo.huangye.task;

import android.os.AsyncTask;
import com.nuobuluo.huangye.utils.Utils;

public class AddSmsTask extends AsyncTask<Void, Integer, Void>{
	String Number;
	String Content;
	Long Date;
	Integer Type;
	public AddSmsTask(String mNum, String sMSContent, Long sMSDate,
					  Integer sMSType) {
		Number = mNum;
		Content = sMSContent;
		Date = sMSDate;
		Type = sMSType;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Utils.AddSms(Number, Content, Date, Type);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}
	
}
