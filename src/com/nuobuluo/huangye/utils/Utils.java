package com.nuobuluo.huangye.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.util.Log;
import com.nuobuluo.huangye.adapter.ContactsBaseAdapter;
import com.nuobuluo.huangye.adapter.MyCursorAdapter;
import com.nuobuluo.huangye.adapter.SmsBaseAdapter;
import com.nuobuluo.huangye.adapter.SmsCursor.Person_Sms;
import com.nuobuluo.huangye.calllogfragment.CallLogsBaseAdapter;
import com.nuobuluo.huangye.calllogfragment.CallLogsCursor;
import com.nuobuluo.huangye.cursor.ContactsCursor.SortEntry;

import java.util.ArrayList;

public class Utils {
    public static Context m_context;
    //所有联系人的数据list
    public static ArrayList<SortEntry> mPersons = new ArrayList<SortEntry>();
    //信息联系人的数据list
    public static  ArrayList<Person_Sms> mPersonSmsList = new ArrayList<Person_Sms>();
    //通话记录联系人的数据list
    public static  ArrayList<CallLogsCursor.Person_CallLog> mPersonCallLogList = new ArrayList<CallLogsCursor.Person_CallLog>();
    //信息列表的适配器
    public static SmsBaseAdapter m_smsadapter;
    //联系人列表的适配器
    public static ContactsBaseAdapter m_contactsAdapter;
    //通话记录列表的BaseAdapter
    public static CallLogsBaseAdapter m_calllogsadapter;
    //过渡的CursorAdapter
    public static MyCursorAdapter m_mycursoradapter ;

    //初始化传入主Activity的上下文
    public static void init(Context context)
    {
        m_context = context;
        m_mycursoradapter= new MyCursorAdapter(m_context, null);
        m_contactsAdapter = new ContactsBaseAdapter(m_context);
        m_smsadapter= new SmsBaseAdapter(m_context);
        m_calllogsadapter = new CallLogsBaseAdapter(m_context);

    }

    //往数据库中新增联系人
    public static void AddContact(String name, String number)
    {
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId  
        Uri rawContactUri = m_context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        //往data表插入姓名数据 
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);//内容类型 
        values.put(StructuredName.GIVEN_NAME, name);
        m_context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //往data表插入电话数据 
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, number);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        m_context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

    }

    //更改数据库中联系人
    public static void ChangeContact(String name, String number, String ContactId)
    {
        Log.i("huahua", name);
        ContentValues values = new ContentValues();
        // 更新姓名
        values.put(StructuredName.GIVEN_NAME, name);
        m_context.getContentResolver().update(ContactsContract.Data.CONTENT_URI,
                values,
                Data.RAW_CONTACT_ID + "=? and " + Data.MIMETYPE  + "=?",
                new String[] { ContactId,StructuredName.CONTENT_ITEM_TYPE });

        //更新电话
        values.clear();
        values.put(Phone.NUMBER, number);
        m_context.getContentResolver().update(ContactsContract.Data.CONTENT_URI,
                values,
                Data.RAW_CONTACT_ID + "=? and " + Data.MIMETYPE  + "=?",
                new String[] { ContactId,Phone.CONTENT_ITEM_TYPE});
    }

    //删除联系人
    public static void DeleteContact(String ContactId)
    {
        Uri uri = Uri.withAppendedPath(RawContacts.CONTENT_URI, ContactId);
        m_context.getContentResolver().delete(uri, null, null);

        //此删除方法不全面
//		m_context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI,
//				Data.RAW_CONTACT_ID + "=? ",
//				new String[] { ContactId});
    }

    //往短信数据库中新增发送的短信
    public static void AddSms(String Number,String Content,Long Date,int Type)
    {
        ContentValues values = new ContentValues();
        values.put("address", Number);
        values.put("body", Content);
        values.put("date", Date);
        values.put("type", Type);
        Uri result = m_context.getContentResolver().insert(Uri.parse("content://sms/"), values);
    }

    //删除指定短信
    public static void DeleteSms(int SmsId)
    {
        Uri uri = Uri.withAppendedPath(Uri.parse("content://sms"), ""+SmsId);
        m_context.getContentResolver().delete(uri, null, null);

//		m_context.getContentResolver().delete(
//	    	       Uri.parse("content://sms"), "_id=" + SmsId, null);
    }

    //根据字母列定位更新联系人列表
    public static int binarySearch(String letter) {
        for (int index = 0;   index < Utils.mPersons .size(); index++) {
            if (Utils.mPersons .get(index).mPY.substring(0, 1).compareToIgnoreCase(letter) == 0) {
                return index;
            }
        }
        return -1;
    }
}
