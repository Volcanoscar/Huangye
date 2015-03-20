
package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.ContentObserver.ContactsContentObserver;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.adapter.FilterAdapter;
import com.nuobuluo.huangye.adapter.SmsCursor.Person_Sms;
import com.nuobuluo.huangye.cursor.ContactsCursor.SortEntry;
import com.nuobuluo.huangye.myview.AlphabetScrollBar;
import com.nuobuluo.huangye.popup.SelectPicPopupWindow;
import com.nuobuluo.huangye.utils.Utils;

import java.util.ArrayList;

/**
 * 关系主界面
 */
public class RelationActivity extends Activity {
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
    //选中的联系人ID
    private String ChooseContactID;
    //加载对话框
    ProgressDialog m_dialogLoading;
    private LinearLayout relation_layout;
    private SelectPicPopupWindow menuWindow;

    @ViewInject(R.id.tel_mianban)
    private RelativeLayout telMianban;
    @ViewInject(R.id.tel_show)
    private ImageButton tel_show;
    @ViewInject(R.id.number_layout)
    private RelativeLayout number_layout;
    @ViewInject(R.id.phoneNumber_edit)
    private EditText phoneNumber_edit;


    private void keyPressed(int keyCode) {
        if (number_layout.getVisibility() == View.GONE) {
            number_layout.setVisibility(View.VISIBLE);
        }
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        phoneNumber_edit.onKeyDown(keyCode, event);

        if (phoneNumber_edit.getText().length() == 0) {
            number_layout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.add_contact_button, R.id.delete_bt, R.id.dial_callbutton_single, R.id.tel_show, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_contact_button:
                telMianban.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_bottom_out));
                telMianban.setVisibility(View.GONE);
                tel_show.setVisibility(View.VISIBLE);
                break;
            case R.id.tel_show:
                telMianban.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_bottom_in));
                telMianban.setVisibility(View.VISIBLE);
                tel_show.setVisibility(View.GONE);
                break;
            case R.id.one:
                keyPressed(KeyEvent.KEYCODE_1);
                break;
            case R.id.two:
                keyPressed(KeyEvent.KEYCODE_2);
                break;
            case R.id.three:
                keyPressed(KeyEvent.KEYCODE_3);
                break;
            case R.id.four:
                keyPressed(KeyEvent.KEYCODE_4);
                break;
            case R.id.five:
                keyPressed(KeyEvent.KEYCODE_5);
                break;
            case R.id.six:
                keyPressed(KeyEvent.KEYCODE_6);
                break;
            case R.id.seven:
                keyPressed(KeyEvent.KEYCODE_7);
                break;
            case R.id.eight:
                keyPressed(KeyEvent.KEYCODE_8);
                break;
            case R.id.nine:
                keyPressed(KeyEvent.KEYCODE_9);
                break;
            case R.id.zero:
                keyPressed(KeyEvent.KEYCODE_0);
                break;
            case R.id.star:
                keyPressed(KeyEvent.KEYCODE_STAR);
                break;
            case R.id.pound:
                keyPressed(KeyEvent.KEYCODE_POUND);
                break;
            case R.id.dial_callbutton_single:
                if (phoneNumber_edit.length() != 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + phoneNumber_edit.getText().toString()));
                    startActivity(intent);
                } else {
                    Toast.makeText(RelationActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete_bt:
                keyPressed(KeyEvent.KEYCODE_DEL);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactsfragment);
        ViewUtils.inject(this);
        ContactsCO = new ContactsContentObserver(new Handler());
        getContentResolver().registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, false, ContactsCO);
        //得到字母列的对象,并设置触摸响应监听器
        m_asb = (AlphabetScrollBar) findViewById(R.id.alphabetscrollbar);
        m_asb.setOnTouchBarListener(new ScrollBarListener());
        m_letterNotice = (TextView) findViewById(R.id.pb_letter_notice);
        m_asb.setTextView(m_letterNotice);


        //得到联系人列表,并设置适配器
        m_contactslist = (ListView) findViewById(R.id.pb_listvew);
        m_contactslist.setAdapter(Utils.m_contactsAdapter);
        //点击进入详情
        m_contactslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long l) {
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(50);
                SortEntry sortEntry=null;
                if("".equals(phoneNumber_edit.getText().toString()))
                {
                ChooseContactName = Utils.mPersons.get(arg2).mName;
                ChooseContactID = Utils.mPersons.get(arg2).mID;
                    sortEntry = Utils.mPersons.get(arg2);
                }
                else
				{
					ChooseContactName = mFilterList.get(arg2).mName;
					ChooseContactID = mFilterList.get(arg2).mID;
                    sortEntry =mFilterList.get(arg2);
				}
                Bundle bundle = new Bundle();
                bundle.putInt("tpye", 0);
                bundle.putString("name", ChooseContactName);
                bundle.putSerializable("contact",sortEntry);
                int index = -1;
                for (int i = 0; i < Utils.mPersonSmsList.size(); i++) {
                    if (sortEntry.getmNums().contains(Utils.mPersonSmsList.get(i).Number)) {
                        index = i;
                    }
                }
                if (index != -1) {
                    bundle.putSerializable("sms", Utils.mPersonSmsList.get(index));
                    bundle.putBoolean("hasSms", true);
                } else {
                    bundle.putBoolean("hasSms", false);
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
                                           final int arg2, long arg3) {

                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(50);
				if("".equals(phoneNumber_edit.getText().toString()))
				{
                ChooseContactName = Utils.mPersons.get(arg2).mName;
                ChooseContactID = Utils.mPersons.get(arg2).mID;
				}
				else
				{
					ChooseContactName = mFilterList.get(arg2).mName;
					ChooseContactID = mFilterList.get(arg2).mID;
				}

                AlertDialog ListDialog = new AlertDialog.Builder(RelationActivity.this).
                        setTitle(ChooseContactName).
                        setItems(new String[]{"拨号", "短信", "删除联系人", "编辑联系人"}, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + Utils.mPersons.get(arg2).getmNums().get(0)));
                                    startActivity(intent);
                                } else if (which == 1) {
                                    Boolean HaveSmsRecord = false;
                                    for (int i = 0; i < Utils.mPersonSmsList.size(); i++) {
                                        if (Utils.mPersonSmsList.get(i).Name.equals(ChooseContactName)) {
                                            HaveSmsRecord = true;
                                            Intent intent = new Intent(RelationActivity.this, ChatActivity.class);
                                            Bundle mBundle = new Bundle();
                                            mBundle.putSerializable("chatperson", Utils.mPersonSmsList.get(i));
                                            intent.putExtras(mBundle);
                                            startActivity(intent);

                                            break;
                                        }
                                    }

                                    if (!HaveSmsRecord) {
                                        Person_Sms person_sms = new Person_Sms();
                                        person_sms.Name = ChooseContactName;
                                        person_sms.Number = Utils.mPersons.get(arg2).getmNums().get(0);

                                        Intent intent = new Intent(RelationActivity.this, ChatActivity.class);
                                        Bundle mBundle = new Bundle();
                                        mBundle.putSerializable("chatperson", person_sms);
                                        intent.putExtras(mBundle);
                                        startActivity(intent);
                                    }

                                } else if (which == 2) {
                                    AlertDialog DeleteDialog = new AlertDialog.Builder(RelationActivity.this).
                                            setTitle("删除").
                                            setMessage("删除联系人" + ChooseContactName + "?").
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
                                } else if (which == 3) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("tpye", 1);
                                    bundle.putString("id", ChooseContactID);
                                    bundle.putString("name", ChooseContactName);
                                    bundle.putString("number", Utils.mPersons.get(arg2).getmNums().get(0));

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

        m_contactslist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (tel_show.getVisibility() == View.GONE) {
                            telMianban.startAnimation(AnimationUtils.loadAnimation(RelationActivity.this, R.anim.push_bottom_out));
                            telMianban.setVisibility(View.GONE);
                            tel_show.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return false;
            }
        });

        m_listEmptyText = (TextView) findViewById(R.id.nocontacts_notice);

        m_topcontactslayout = (FrameLayout) findViewById(R.id.top_contacts_layout);

        //初始化搜索编辑框,设置文本改变时的监听器
        //m_FilterEditText = (EditText)findViewById(R.id.pb_search_edit);
        //设置提示文字
        //m_FilterEditText.setHint("搜索"+Utils.mPersons.size()+"位联系人");
        phoneNumber_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(s.toString().trim())) {
                    //根据编辑框值过滤联系人并更新联系列表
                    FilterSearch(s.toString().trim());

                    m_FAdapter = new FilterAdapter(RelationActivity.this, mFilterList, s.toString().trim());
                    m_contactslist.setAdapter(m_FAdapter);
                } else {
                    m_contactslist.setAdapter(Utils.m_contactsAdapter);
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

    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    menuWindow.showAtLocation(relation_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    menuWindow.update();
                    break;
            }
        }

    };

    private void initPopupWindow() {
        menuWindow = new SelectPicPopupWindow(RelationActivity.this, null);
        popupHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "批量删除");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent intent = new Intent(RelationActivity.this, BatchDeleteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteContactTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Utils.DeleteContact(ChooseContactID);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (m_dialogLoading != null) {
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
            if (idx != -1) {
                m_contactslist.setSelection(idx);
            }
        }
    }

    public void FilterSearch(String keyword) {
        mFilterList.clear();
        //遍历mArrayList
        for (int i = 0; i < Utils.mPersons.size(); i++) {
            //如果遍历到List包含所输入字符串
            boolean hasKey= false;
            for(String key: Utils.mPersons.get(i).getmNums()) {
                if (key.indexOf(keyword) != -1) {
                    hasKey=true;
                    break;
                }
            }
            if (hasKey
                    || isStrInString(Utils.mPersons.get(i).mPY, keyword)
                    || Utils.mPersons.get(i).mName.contains(keyword)
                    || isStrInString(Utils.mPersons.get(i).mFisrtSpell, keyword)) {
                //将遍历到的元素重新组成一个list
                mFilterList.add(Utils.mPersons.get(i));
            }
        }
    }

    public boolean isStrInString(String bigStr, String smallStr) {
        if (bigStr.toUpperCase().indexOf(smallStr.toUpperCase()) > -1) {
            return true;
        } else {
            return false;
        }
    }
}
