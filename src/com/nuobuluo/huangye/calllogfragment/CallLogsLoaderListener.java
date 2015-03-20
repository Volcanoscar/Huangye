package com.nuobuluo.huangye.calllogfragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import com.nuobuluo.huangye.utils.Utils;

public class CallLogsLoaderListener implements LoaderManager.LoaderCallbacks<Cursor>{
	private Context m_context;
	
	public CallLogsLoaderListener(Context context) {
		m_context = context;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		return new CallLogsCursorLoader(m_context, CallLog.Calls.CONTENT_URI, 
				null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		Utils.m_mycursoradapter.swapCursor(arg1);
		
		CallLogsCursor data = (CallLogsCursor)Utils.m_mycursoradapter.getCursor();
		Utils.mPersonCallLogList = data.GetPersonCallLogArray();
		Utils.m_calllogsadapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Utils.m_mycursoradapter.swapCursor(null);
		
	}
	
}
