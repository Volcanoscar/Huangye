package com.nuo.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts.Data;
import com.nuo.utils.PinyinUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactsCursor extends CursorWrapper{
    private ArrayList<SortEntry> mSortList;
    private ArrayList<SortEntry> mFilterList;//筛选后的数据list
    private Cursor mCursor;
    private int mPos;

    public ContactsCursor(Cursor cursor) {
        super(cursor);

        mCursor = cursor;
        mSortList = new ArrayList<SortEntry>();
        for( cursor.moveToFirst(); ! cursor.isAfterLast();  cursor.moveToNext()) {
            SortEntry entry = new SortEntry();
            entry.mID =  cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
            entry.mName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            entry.mOrder = cursor.getPosition();
            entry.mPY = PinyinUtils.getPingYin(entry.mName);
            entry.mNum =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            entry.mFisrtSpell = PinyinUtils.getFirstSpell(entry.mName);
            entry.mchoose =false;
            mSortList.add(entry);
        }

        Collections.sort(mSortList, new ComparatorPY());

    }

    public static class SortEntry implements Serializable{
        public String mID;		  //在数据库中的ID号
        public String mName;  //姓名
        public String mPY;      //姓名拼音
        public String mNum;      //电话号码
        public String mFisrtSpell;      //中文名首字母 例:张雪冰:zxb
        public boolean mchoose;    //是否选中
        public int mOrder;      //在原Cursor中的位置
    }

    private class ComparatorPY implements Comparator<SortEntry>{

        @Override
        public int compare(SortEntry lhs, SortEntry rhs) {
            // TODO Auto-generated method stub
            String str1 = lhs.mPY;
            String str2 = rhs.mPY;
            return str1.compareToIgnoreCase(str2);
        }

    }

    @Override
    public boolean moveToPosition(int position) {
        mPos = position;
        if(position < mSortList.size() && position >=0)
        {
            return mCursor.moveToPosition(mSortList.get(position).mOrder);
        }

        if (position < 0) {
            mPos = -1;
        }
        if (position >= mSortList.size()) {
            mPos = mSortList.size();
        }
        return mCursor.moveToPosition(position);
    }

    @Override
    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    @Override
    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    @Override
    public boolean moveToNext() {
        // TODO Auto-generated method stub
        return moveToPosition(mPos + 1);
    }

    @Override
    public boolean moveToPrevious() {
        // TODO Auto-generated method stub
        return moveToPosition(mPos - 1);
    }

    public ArrayList<SortEntry> GetContactsArray() {
        return mSortList;
    }

}
