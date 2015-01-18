package com.nuo.calllogfragment;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

public class CallLogsCursorLoader extends CursorLoader{
	private Context mcontext;
	public CallLogsCursorLoader(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mcontext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Cursor loadInBackground() {
		// TODO Auto-generated method stub
		Cursor cursor = super.loadInBackground();
		return new CallLogsCursor(cursor,mcontext);
	}

}
