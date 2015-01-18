
package com.nuo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.fujie.module.titlebar.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nuo.ContentObserver.ContactsContentObserver;
import com.nuo.adapter.FilterAdapter;
import com.nuo.adapter.SmsCursor.Person_Sms;
import com.nuo.adapter.SwipeAdapter;
import com.nuo.cursor.ContactsCursor.SortEntry;
import com.nuo.model.WXMessage;
import com.nuo.myview.AlphabetScrollBar;
import com.nuo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RelationActivity extends Activity{
	//字母列视图View
	private AlphabetScrollBar m_asb;
	//显示选中的字母
	private TextView m_letterNotice;
	//联系人的列表
	private ListView m_contactslist;
	//筛选后的适配器
	private FilterAdapter m_FAdapter;
	//筛选查询后的数据list
	private ArrayList<SortEntry> mFilterList = new ArrayList<SortEntry>();
	//搜索过滤联系人EditText
	private EditText m_FilterEditText;
	//没有匹配联系人时显示的TextView
	private TextView m_listEmptyText;
	//新增联系人按钮
	private ImageButton m_AddContactBtn;
	//最上面的layout
	private FrameLayout m_topcontactslayout;
	//联系人内容观察者
	private ContactsContentObserver ContactsCO;
	//选中的联系人名字
	private String ChooseContactName;
	//选中的联系人号码
	private String ChooseContactNumber;
	//选中的联系人ID
	private String ChooseContactID;
	//加载对话框
	ProgressDialog m_dialogLoading;
	@ViewInject(R.id.title_bar)
	private TitleBarView mTitleBarView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactsfragment);
		ViewUtils.inject(this);
		ContactsCO = new ContactsContentObserver(new Handler());
		getContentResolver().registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, false, ContactsCO);
		initView();
		//得到字母列的对象,并设置触摸响应监听器
		m_asb = (AlphabetScrollBar)findViewById(R.id.alphabetscrollbar);
		m_asb.setOnTouchBarListener(new ScrollBarListener());
		m_letterNotice = (TextView)findViewById(R.id.pb_letter_notice);
		m_asb.setTextView(m_letterNotice);

		//得到联系人列表,并设置适配器
		m_contactslist = (ListView)findViewById(R.id.pb_listvew);
		m_contactslist.setAdapter(Utils.m_contactsAdapter);
		//点击进入详情
		m_contactslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long l) {
				Vibrator vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
				vib.vibrate(50);

				if(m_topcontactslayout.getVisibility() == View.VISIBLE)
				{
					ChooseContactName = Utils.mPersons.get(arg2).mName;
					ChooseContactNumber = Utils.mPersons.get(arg2).mNum;
					ChooseContactID = Utils.mPersons.get(arg2).mID;
				}
				else
				{
					ChooseContactName = mFilterList.get(arg2).mName;
					ChooseContactNumber = mFilterList.get(arg2).mNum;
					ChooseContactID = mFilterList.get(arg2).mID;
				}

				Bundle bundle = new Bundle();
				bundle.putInt("tpye", 0);
				bundle.putString("name", ChooseContactName);
				bundle.putString("number",ChooseContactNumber);
				for(int i=0;i<Utils.mPersons.size();i++) {
					if (ChooseContactNumber.equals(Utils.mPersons.get(i).mNum)) {
						bundle.putSerializable("contact", Utils.mPersons.get(i));
					}
				}
				int index =-1;
				for(int i=0;i<Utils.mPersonSmsList.size();i++) {
					if (ChooseContactNumber.equals(Utils.mPersonSmsList.get(i).Number)) {
						index = i;
					}
				}
				if(index!=-1){
					bundle.putSerializable("sms",Utils.mPersonSmsList.get(index));
					bundle.putBoolean("hasSms",true);
				}else{
					bundle.putBoolean("hasSms",false);
				}
				Intent intent = new Intent(RelationActivity.this, ContactsDetailActivity.class);
				intent.putExtra("person", bundle);
				startActivity(intent);
			}
		});
		//联系人列表长按监听
		m_contactslist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {

				Vibrator vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
				vib.vibrate(50);

				if(m_topcontactslayout.getVisibility() == View.VISIBLE)
				{
					ChooseContactName = Utils.mPersons.get(arg2).mName;
					ChooseContactNumber = Utils.mPersons.get(arg2).mNum;
					ChooseContactID = Utils.mPersons.get(arg2).mID;
				}
				else
				{
					ChooseContactName = mFilterList.get(arg2).mName;
					ChooseContactNumber = mFilterList.get(arg2).mNum;
					ChooseContactID = mFilterList.get(arg2).mID;
				}

				AlertDialog ListDialog = new AlertDialog.Builder(RelationActivity.this).
						setTitle(ChooseContactName).
						setItems(new String[] { "拨号","短信","删除联系人","编辑联系人"}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which == 0)
								{
									Intent  intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel://" + ChooseContactNumber));
									startActivity(intent);
								}
								else if(which == 1)
								{
									Boolean HaveSmsRecord=false;
									for(int i=0;i<Utils.mPersonSmsList.size();i++)
									{
										if(Utils.mPersonSmsList.get(i).Name.equals(ChooseContactName))
										{
											HaveSmsRecord = true;
											Intent intent = new Intent(RelationActivity.this,ChatActivity.class);
											Bundle mBundle = new Bundle();
											mBundle.putSerializable("chatperson", Utils.mPersonSmsList.get(i));
											intent.putExtras(mBundle);
											startActivity(intent);

											break;
										}
									}

									if(!HaveSmsRecord)
									{
										Person_Sms person_sms = new Person_Sms();
										person_sms.Name = ChooseContactName;
										person_sms.Number = ChooseContactNumber;

										Intent intent = new Intent(RelationActivity.this,ChatActivity.class);
										Bundle mBundle = new Bundle();
										mBundle.putSerializable("chatperson", person_sms);
										intent.putExtras(mBundle);
										startActivity(intent);
									}

								}
								else if(which == 2)
								{
									AlertDialog DeleteDialog = new AlertDialog.Builder(RelationActivity.this).
											setTitle("删除").
											setMessage("删除联系人"+ChooseContactName +"?").
											setPositiveButton("确定", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {
													//删除联系人操作,放在线程中处理
													new DeleteContactTask().execute();
												}
											}).
											setNegativeButton("取消", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {

												}
											}).
											create();
									DeleteDialog.show();
								}
								else if(which == 3)
								{
									Bundle bundle = new Bundle();
									bundle.putInt("tpye", 1);
									bundle.putString("id", ChooseContactID);
									bundle.putString("name", ChooseContactName);
									bundle.putString("number", ChooseContactNumber);

									Intent intent = new Intent(RelationActivity.this, AddContactsActivity.class);
									intent.putExtra("person", bundle);
									startActivity(intent);
								}
							}
						}).
						create();
				ListDialog.show();

				return false;
			}
		});

		m_listEmptyText = (TextView)findViewById(R.id.nocontacts_notice);

		m_topcontactslayout = (FrameLayout)findViewById(R.id.top_contacts_layout);

		//初始化搜索编辑框,设置文本改变时的监听器
		m_FilterEditText = (EditText)findViewById(R.id.pb_search_edit);
		//设置提示文字
		m_FilterEditText.setHint("搜索"+Utils.mPersons.size()+"位联系人");
		m_FilterEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(!"".equals(s.toString().trim()))
				{
					//根据编辑框值过滤联系人并更新联系列表
					FilterSearch(s.toString().trim());

					m_FAdapter = new FilterAdapter(RelationActivity.this, mFilterList,s.toString().trim());
					m_contactslist.setAdapter(m_FAdapter);

					m_asb.setVisibility(View.GONE);
					m_topcontactslayout.setVisibility(View.GONE);
				}
				else
				{
					m_contactslist.setAdapter(Utils.m_contactsAdapter);
					m_topcontactslayout.setVisibility(View.VISIBLE);
					m_asb.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void initView() {
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE);
		mTitleBarView.setTitleText(R.string.relation);
		mTitleBarView.getBtnRight().setPadding(8, 0, 8, 0);
		mTitleBarView.getBtnRight().setTextColor(getResources().getColor(R.color.white));
		mTitleBarView.setBtnRight(R.drawable.btn_hi_new_contact, R.string.add_sms);
		mTitleBarView.setBtnRightBg(R.drawable.login_btn);
		mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putInt("tpye", 0);
				bundle.putString("name", "");

				bundle.putString("number", "");

				Intent intent = new Intent(RelationActivity.this, AddContactsActivity.class);
				intent.putExtra("person", bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "批量删除");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1)
		{
			Intent intent = new Intent(RelationActivity.this, BatchDeleteActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private class  DeleteContactTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Utils.DeleteContact(ChooseContactID);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(m_dialogLoading!= null)
			{
				m_dialogLoading.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			m_dialogLoading = new ProgressDialog(RelationActivity.this);
			m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
			m_dialogLoading.setMessage("正在删除");
			m_dialogLoading.setCancelable(false);
			m_dialogLoading.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.i("huahua", "onProgressUpdate");
		}

	}



	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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

	public void FilterSearch(String keyword) {
		mFilterList.clear();
		//遍历mArrayList
		for (int i = 0; i < Utils.mPersons.size(); i++) {
			//如果遍历到List包含所输入字符串
			if (Utils.mPersons.get(i).mNum.indexOf(keyword) !=-1
					||isStrInString(Utils.mPersons.get(i).mPY,keyword)
					|| Utils.mPersons.get(i).mName.contains(keyword)
					||isStrInString(Utils.mPersons.get(i).mFisrtSpell,keyword)){
				//将遍历到的元素重新组成一个list

				SortEntry entry = new SortEntry();
				entry.mName = Utils.mPersons.get(i).mName;
				entry.mID = Utils.mPersons.get(i).mID;
				entry.mOrder = i;
				entry.mPY = Utils.mPersons.get(i).mPY;
				entry.mNum = Utils.mPersons.get(i).mNum;
				mFilterList.add(entry);
			}
		}
	}

	public boolean isStrInString(String bigStr,String smallStr){
		if(bigStr.toUpperCase().indexOf(smallStr.toUpperCase())>-1){
			return true;
		}else{
			return false;
		}
	}

}
