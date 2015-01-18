package com.nuo.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.cursor.ContactsCursor;
import com.nuo.cursor.ContactsCursor.SortEntry;
import com.nuo.cursor.ContactsCursorLoader;
import com.nuo.myview.AlphabetScrollBar;
import com.nuo.utils.Utils;

import java.util.ArrayList;

public class BatchSendSmsActivity extends Activity{
    //字母列视图View
    private AlphabetScrollBar m_asb;
    //显示选中的字母
    private TextView m_letterNotice;
    //联系人的列表
    private ListView m_contactslist;
    //联系人列表的适配器
    private ContactsCursorAdapter m_contactsAdapter;
    //所有联系人的数据list
    private ArrayList<SortEntry> mSortList = new ArrayList<SortEntry>();
    //群发短信的联系人list
    private ArrayList<SortEntry> mBatchSmsList;
    //加载器监听器
    private ContactsLoaderListener m_loaderCallback = new ContactsLoaderListener();
    //确定按钮
    private Button m_OkBtn;
    //选中全部按钮
    private Button m_SelectAllBtn;
    //选中多少个联系人
    private int m_choosenum=0;
    //选择所有联系人的标志
    private boolean m_selectAll = false;
    @ViewInject(R.id.title_bar)
    private TitleBarView mTitleBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sms_batchsend);
        ViewUtils.inject(this);
        //得到字母列的对象,并设置触摸响应监听器
        m_asb = (AlphabetScrollBar)findViewById(R.id.alphabetscrollbar);
        m_asb.setOnTouchBarListener(new ScrollBarListener());
        m_letterNotice = (TextView)findViewById(R.id.pb_letter_notice);
        m_asb.setTextView(m_letterNotice);
        initTitleView();
        //得到联系人列表,并设置适配器
        getLoaderManager().initLoader(0,null,m_loaderCallback);
        m_contactslist = (ListView)findViewById(R.id.pb_listvew);
        m_contactsAdapter = new ContactsCursorAdapter(this, null);
        m_contactslist.setAdapter(m_contactsAdapter);
        m_contactslist.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(mSortList.get(arg2).mchoose == false)
                {
                    mSortList.get(arg2).mchoose = true;
                    m_choosenum++;
                    mBatchSmsList.add(mSortList.get(arg2));
                }
                else
                {
                    mSortList.get(arg2).mchoose = false;
                    for(int i=0;i<m_choosenum;i++)
                    {
                        if(mBatchSmsList.get(i).mID.equals(mSortList.get(arg2).mID))
                        {
                            mBatchSmsList.remove(i);
                            break;
                        }
                    }
                    m_choosenum--;
                }

                if(m_choosenum == mSortList.size())
                {
                    m_selectAll = true;
                    m_SelectAllBtn.setText("取消全部");
                }
                else
                {
                    m_selectAll = false;
                    m_SelectAllBtn.setText("选择全部");
                }
                m_contactsAdapter.notifyDataSetChanged();
                m_OkBtn.setText("确定("+ m_choosenum +")");
            }
        });

        m_OkBtn = (Button)findViewById(R.id.ok_btn);
        m_SelectAllBtn = (Button)findViewById(R.id.select_all);
        m_OkBtn.setOnClickListener(new BtnClick());
        m_SelectAllBtn.setOnClickListener(new BtnClick());

        mBatchSmsList = (ArrayList<SortEntry>) getIntent().getSerializableExtra("BatchSendSms");
        if(mBatchSmsList == null)
        {
            mBatchSmsList = new ArrayList<SortEntry>();
        }

    }
    private void initTitleView(){
        mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        mTitleBarView.setBtnLeft(R.drawable.ic_back, R.string.back);
        mTitleBarView.setTitleText(R.string.change_send_user);
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class BtnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.ok_btn)
            {
                Intent intent = new Intent("android.huahua.BatchSendSms");
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("BatchSendSms", mBatchSmsList);
                intent.putExtras(mBundle);
                sendBroadcast(intent);
                finish();
            }
            else if(v.getId() == R.id.select_all)
            {
                mBatchSmsList.clear();
                if(!m_selectAll)
                {
                    for(int i=0;i<mSortList.size();i++)
                    {
                        mSortList.get(i).mchoose = true;
                        mBatchSmsList.add(mSortList.get(i));
                    }
                    m_choosenum = mBatchSmsList.size();
                    m_OkBtn.setText("确定("+ m_choosenum +")");
                    m_SelectAllBtn.setText("取消全部");
                    m_contactsAdapter.notifyDataSetChanged();
                    m_selectAll = !m_selectAll;
                }
                else
                {
                    for(int i=0;i<mSortList.size();i++)
                    {
                        mSortList.get(i).mchoose = false;
                    }
                    m_choosenum = 0;
                    m_OkBtn.setText("确定(0)");
                    m_SelectAllBtn.setText("选择全部");
                    m_contactsAdapter.notifyDataSetChanged();
                    m_selectAll = !m_selectAll;
                }

            }

        }

    }

    private class ContactsCursorAdapter extends CursorAdapter{
        int ItemPos = -1;
        private ArrayList<ContactsCursor.SortEntry> list;
        private Context context;

        public ContactsCursorAdapter(Context context, Cursor c) {
            super(context, c);
            this.context = context;
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemPos = position;
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.contacts_list_item, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.contacts_name);
            name.setText(mSortList.get(position).mName);

            TextView number = (TextView) convertView.findViewById(R.id.contacts_number);
            number.setText(mSortList.get(position).mNum);

            if(mSortList.get(position).mchoose == true)
            {
                ImageView choosecontact = (ImageView)convertView.findViewById(R.id.choose_contact);
                choosecontact.setImageResource(R.drawable.cb_checked);
            }
            else
            {
                ImageView choosecontact = (ImageView)convertView.findViewById(R.id.choose_contact);
                choosecontact.setImageResource(R.drawable.cb_unchecked);
            }

            return convertView;
//			return super.getView(position, convertView, parent);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if(cursor == null)
            {
                return;
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.contacts_list_item, parent, false);
        }

    }

    //加载器的监听器
    private class ContactsLoaderListener implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new ContactsCursorLoader(BatchSendSmsActivity.this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
            m_contactsAdapter.swapCursor(arg1);

            ContactsCursor data = (ContactsCursor)m_contactsAdapter.getCursor();
            mSortList = data.GetContactsArray();

            for(int i=0;i<mBatchSmsList.size();i++)
            {
                for(int j=0;j<mSortList.size();j++)
                {
                    if(mBatchSmsList.get(i).mName.equals(mSortList.get(j).mName))
                    {
                        mSortList.get(j).mchoose =true;
                        break;
                    }
                }
            }

            m_choosenum = mBatchSmsList.size();
            m_OkBtn.setText("确定("+ m_choosenum +")");
            if(m_choosenum == mSortList.size())
            {
                m_selectAll =true;
                m_SelectAllBtn.setText("取消全部");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
            m_contactsAdapter.swapCursor(null);

        }

    }

    //字母列触摸的监听器
    private class ScrollBarListener implements AlphabetScrollBar.OnTouchBarListener {

        @Override
        public void onTouch(String letter) {

            //触摸字母列时,将联系人列表更新到首字母出现的位置
            int idx = Utils.binarySearch(letter);
            if(idx != -1)
            {
                m_contactslist.setSelection(idx);
            }
        }
    }

}
