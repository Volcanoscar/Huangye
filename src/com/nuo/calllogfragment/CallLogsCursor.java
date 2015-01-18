package com.nuo.calllogfragment;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.CallLog;

import java.util.ArrayList;

public class CallLogsCursor extends CursorWrapper{
	private Cursor mCursor;  
	private Context  mcontext;
	private ArrayList<Person_CallLog> mPerson_CallLogList;
	private int mPos;
	
	/*
	 *通话记录的数据
	 */
	public static class CallLogs {
		public int CallLogId; 		//每条通话记录的标识
		public int CallLogType;      //通话记录的状态 1:来电 2:去电 3:未接
		public Long CallLogDate;      //通话记录日期
		public int CallLogDuration;   //通话记录时长
		public int mOrder;  //在原Cursor中的位置
	} 
	
	//单个联系人的通话记录属性
	public static class Person_CallLog  {
		public String Name;  //姓名
		public String Number;      //电话号码
		public boolean choose;   //是否选中状态
		public ArrayList<CallLogs> person_calllogs= new ArrayList<CallLogs>();   
	} 
	
	public CallLogsCursor(Cursor cursor, Context context) {
		super(cursor);
		// TODO Auto-generated constructor stub
		mCursor = cursor;
		mcontext = context;
		
		mPerson_CallLogList = new ArrayList<Person_CallLog>();
		//从游标的最后索引往前查询,因为最新的通话记录在最后
		for(cursor.moveToLast(); ! cursor.isBeforeFirst();  cursor.moveToPrevious()) 
        {
        	Boolean AddPersonFlag = true;
        	Person_CallLog  person_calllog = new Person_CallLog();
        	CallLogs calllog = new CallLogs();
            
        	calllog.CallLogId = cursor.getInt(cursor.getColumnIndex("_id"));
        	person_calllog.choose = false;
        	person_calllog.Name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
    		person_calllog.Number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
    		calllog.CallLogType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
    		calllog.CallLogDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
    		calllog.CallLogDuration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
    		
            if(person_calllog.Number != null)
            {
            	String NumberRemove86 = null;
        		if (person_calllog.Number.contains("+86")) //如果包含+86的号码，因为有些短信发的话在签名会+86
        		{
        			NumberRemove86 = person_calllog.Number.substring(3, person_calllog.Number.length());
        		}
        		else
        		{
        			NumberRemove86 = "+86" + person_calllog.Number;
        		}
            	
                for(int i=0;i<mPerson_CallLogList.size();i++)
                {
            	   if(mPerson_CallLogList.get(i).Number.equals(person_calllog.Number)
            			   || mPerson_CallLogList.get(i).Number.equals(NumberRemove86))
            	   {
            		   AddPersonFlag = false;
            		   mPerson_CallLogList.get(i).person_calllogs.add(calllog);
            		   break;
            	   }
            	   else if(person_calllog.Name != null)
            	   {
            		   if(mPerson_CallLogList.get(i).Name.equals(person_calllog.Name))
            		   {
                		   AddPersonFlag = false;
                		   mPerson_CallLogList.get(i).person_calllogs.add(calllog);
                		   break;
            		   }
            	   }
                }
                
        		if(person_calllog.Name == null)
        		{
        			person_calllog.Name = person_calllog.Number;
        		}
                
                if(AddPersonFlag)
                {
                	person_calllog.person_calllogs.add(calllog);
                	mPerson_CallLogList.add(person_calllog);
                }

            }
       
        }
        
	}
	
	@Override
	public boolean moveToPosition(int position) {
		// TODO Auto-generated method stub
		mPos = position; 
		return super.moveToPosition(position);
	}

	@Override
	public boolean moveToFirst() {
		// TODO Auto-generated method stub
		return moveToPosition(0);
	}

	@Override
	public boolean moveToLast() {
		// TODO Auto-generated method stub
		return moveToPosition(getCount() - 1); 
	}

	@Override
	public boolean moveToNext() {
		// TODO Auto-generated method stub
		return moveToPosition(mPos + 1);
	}

	public ArrayList<Person_CallLog> GetPersonCallLogArray() {
		 
    	return mPerson_CallLogList;
	}

}