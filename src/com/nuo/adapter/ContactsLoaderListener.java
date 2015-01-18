package com.nuo.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.nuo.cursor.ContactsCursor;
import com.nuo.cursor.ContactsCursorLoader;
import com.nuo.utils.Utils;


public class ContactsLoaderListener implements LoaderManager.LoaderCallbacks<Cursor>{
	private Context m_context;
	
	public ContactsLoaderListener(Context context) {
		m_context = context;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new ContactsCursorLoader(m_context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			Utils.m_mycursoradapter.swapCursor(arg1);
		
			ContactsCursor data = (ContactsCursor)Utils.m_mycursoradapter.getCursor();
			Utils.mPersons = data.GetContactsArray();
			Utils.m_contactsAdapter.notifyDataSetChanged();
			
			Intent i = new Intent("android.huahua.SMS_Loader");
			m_context.sendBroadcast(i);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Utils.m_mycursoradapter.swapCursor(null);
		
	}
	
}
