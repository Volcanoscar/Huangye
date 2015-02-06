package com.nuo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import com.nuo.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class SmsCursor extends CursorWrapper{
    private Cursor mCursor;
    private Context  mcontext;
    private ArrayList<Person_Sms> mPerson_SmsList;
    private int mPos;

    /*
     *短信息的数据
     */
    public static class SMSs implements Serializable{
        public int SMSId; //每条短信的标识
        public String SMSContent;      //短信内容
        public Long SMSDate;      //短信时间
        public Integer SMSType;      //短信状态
        public int mOrder;  //在原Cursor中的位置
    }

    //单个联系人的信息属性
    public static class Person_Sms implements Serializable {
        public String Name;  //姓名
        public String Number;      //电话号码
        public ArrayList<SMSs> person_smss= new ArrayList<SMSs>();
    }

    public SmsCursor(Cursor cursor, Context context) {
        super(cursor);
        // TODO Auto-generated constructor stub
        mCursor = cursor;
        mcontext = context;

        mPerson_SmsList = new ArrayList<Person_Sms>();
        for(cursor.moveToFirst(); ! cursor.isAfterLast();  cursor.moveToNext())
        {
            Boolean AddPersonFlag = true;
            Person_Sms  person_sms = new Person_Sms();
            SMSs Sms = new SMSs();

            Sms.SMSId = cursor.getInt(cursor.getColumnIndex("_id"));
            person_sms.Number = cursor.getString(cursor.getColumnIndex("address"));
            Sms.SMSContent = cursor.getString(cursor.getColumnIndex("body"));
            Sms.SMSDate = cursor.getLong(cursor.getColumnIndex("date"));
            Sms.SMSType = cursor.getInt(cursor.getColumnIndex("type"));

            if(person_sms.Number != null)
            {
                String NumberRemove86 = null;
                if (person_sms.Number.contains("+86")) //如果包含+86的号码，因为有些短信发的话在签名会+86
                {
                    NumberRemove86 = person_sms.Number.substring(3, person_sms.Number.length());
                }
                else
                {
                    NumberRemove86 = "+86" + person_sms.Number;
                }

                for(int i=0;i<mPerson_SmsList.size();i++)
                {
                    if(mPerson_SmsList.get(i).Number.equals(person_sms.Number)
                            || mPerson_SmsList.get(i).Number.equals(NumberRemove86))
                    {
                        AddPersonFlag = false;
                        mPerson_SmsList.get(i).person_smss.add(Sms);
                        break;
                    }
                }

                if(AddPersonFlag)
                {
                    for(int i=0;i<Utils.mPersons.size();i++)
                    {
                        if(Utils.mPersons.get(i).getmNums().contains(person_sms.Number)
                                || Utils.mPersons.get(i).getmNums().contains(NumberRemove86))
                        {
                            person_sms.Name = Utils.mPersons.get(i).mName;
                            break;
                        }
                    }
                    if(person_sms.Name == null)
                    {
                        person_sms.Name = person_sms.Number;
                    }

                    person_sms.person_smss.add(Sms);
                    mPerson_SmsList.add(person_sms);
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

    public ArrayList<Person_Sms> GetPersonSmsArray() {

        return mPerson_SmsList;
    }

}
