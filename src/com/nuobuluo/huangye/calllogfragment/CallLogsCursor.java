package com.nuobuluo.huangye.calllogfragment;

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
	 *ͨ����¼������
	 */
	public static class CallLogs {
		public int CallLogId; 		//ÿ��ͨ����¼�ı�ʶ
		public int CallLogType;      //ͨ����¼��״̬ 1:���� 2:ȥ�� 3:δ��
		public Long CallLogDate;      //ͨ����¼����
		public int CallLogDuration;   //ͨ����¼ʱ��
		public int mOrder;  //��ԭCursor�е�λ��
	} 
	
	//������ϵ�˵�ͨ����¼����
	public static class Person_CallLog  {
		public String Name;  //����
		public String Number;      //�绰����
		public boolean choose;   //�Ƿ�ѡ��״̬
		public ArrayList<CallLogs> person_calllogs= new ArrayList<CallLogs>();   
	} 
	
	public CallLogsCursor(Cursor cursor, Context context) {
		super(cursor);
		// TODO Auto-generated constructor stub
		mCursor = cursor;
		mcontext = context;
		
		mPerson_CallLogList = new ArrayList<Person_CallLog>();
		//���α�����������ǰ��ѯ,��Ϊ���µ�ͨ����¼�����
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
        		if (person_calllog.Number.contains("+86")) //�������+86�ĺ��룬��Ϊ��Щ���ŷ��Ļ���ǩ����+86
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