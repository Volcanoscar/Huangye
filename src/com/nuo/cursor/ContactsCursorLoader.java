package com.nuo.cursor;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

public class ContactsCursorLoader extends CursorLoader{

	public ContactsCursorLoader(Context context, Uri uri, String[] projection,
								String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Cursor loadInBackground() {
		Cursor cursor = super.loadInBackground();
		return new ContactsCursor(cursor);
	}

}
