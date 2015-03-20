package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

public class SmsCursorLoader extends CursorLoader{
	private Context mcontext;
	public SmsCursorLoader(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mcontext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Cursor loadInBackground() {
		// TODO Auto-generated method stub
		Cursor cursor = super.loadInBackground();
		return new SmsCursor(cursor,mcontext);
	}

}
