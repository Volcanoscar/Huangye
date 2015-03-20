package com.nuobuluo.huangye.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.nuobuluo.huangye.utils.Utils;

public class SmsLoaderListener implements LoaderManager.LoaderCallbacks<Cursor>{
	private Context m_context;
	
	public SmsLoaderListener(Context context) {
		m_context = context;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SmsCursorLoader(m_context, Uri.parse("content://sms/"), 
				null, null, null, null);
	
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			Utils.m_mycursoradapter.swapCursor(arg1);
			SmsCursor data = (SmsCursor)Utils.m_mycursoradapter.getCursor();
			Utils.mPersonSmsList = data.GetPersonSmsArray();
			Utils.m_smsadapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Utils.m_mycursoradapter.swapCursor(null);
	}
}