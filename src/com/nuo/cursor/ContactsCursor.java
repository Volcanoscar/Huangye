package com.nuo.cursor;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.ContactsContract;
import com.nuo.utils.PinyinUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsCursor extends CursorWrapper{
    private ArrayList<SortEntry> mSortList;
    private ArrayList<SortEntry> mFilterList;//筛选后的数据list
    private Cursor mCursor;
    private int mPos;
    private Context context;
    public ContactsCursor(Cursor cursor) {
        super(cursor);
        mCursor = cursor;
        init();
    }
    private void init(){
        mSortList = new ArrayList<SortEntry>();
        for( mCursor.moveToFirst(); ! mCursor.isAfterLast();  mCursor.moveToNext()) {
            SortEntry entry = new SortEntry();
            //String mId =mCursor.getString(mCursor.getColumnIndex(Data.RAW_CONTACT_ID));;
            String mId =mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
            entry.mID = mId;
            String mName =mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            entry.mName = mName;
            entry.mOrder = mCursor.getPosition();
            entry.mPY = PinyinUtils.getPingYin(entry.mName);
            // entry.mNum =  mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            entry.mFisrtSpell = PinyinUtils.getFirstSpell(entry.mName);
            entry.mchoose =false;
            List list = new ArrayList<String>();
            //
            // 查看该联系人有多少个电话号码。如果没有这返回值为0
            int phoneCount = mCursor
                    .getInt(mCursor
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (phoneCount > 0) {
                // 获得联系人的电话号码
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " +mId , null, null);
                if (phones.moveToFirst()) {
                    do {
                        // 遍历所有的电话号码
                        String phoneNumber = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // 以电话号码为关键字
                        list.add(phoneNumber);

                        if(entry.mNum==null){//TODO: 待重构
                            entry.mNum = phoneNumber;
                        }
                    } while (phones.moveToNext());
                }
                phones.close();
            }
            //多个联系人(一个或者多个)
            entry.mNums=list;
            if (list.size() != 0) {
                mSortList.add(entry);
            }
        }

        Collections.sort(mSortList, new ComparatorPY());

    }

    public ContactsCursor(Context context, Cursor cursor) {
        super(cursor);
        mCursor = cursor;
        this.context=context;
        init();

    }

    public static class SortEntry implements Serializable{
        public String mID;		  //在数据库中的ID号
        public String mName;  //姓名
        public String mPY;      //姓名拼音
        public String mNum;      //电话号码
        public String mFisrtSpell;      //中文名首字母 例:张雪冰:zxb
        public boolean mchoose;    //是否选中
        public int mOrder;      //在原Cursor中的位置
        public List<String> mNums;
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
