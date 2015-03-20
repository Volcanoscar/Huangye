package com.nuobuluo.huangye.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.module.listview.CustomListView;
import com.nuobuluo.module.listview.LoadingView;
import com.nuobuluo.module.sort.CharacterParser;
import com.nuobuluo.module.sort.PinyinComparator;
import com.nuobuluo.module.sort.SideBar;
import com.nuobuluo.module.sort.SortModel;
import com.nuobuluo.module.wifi.AsyncTaskBase;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.adapter.QQfriendAdapter;
import com.nuobuluo.huangye.test.TestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * Created by APPLE on 2015/3/16.
 */
public class FriendFragment extends Fragment {

    private Context mContext;
    private View mBaseView;
    private CustomListView mCustomListView;
    private LoadingView mLoadingView;
    private View mSearchView;
    private SideBar sideBar;
    private TextView dialog;
    private QQfriendAdapter adapter;
    private Map<String, String> callRecords;
    private RelativeLayout constacts;
    @ViewInject(R.id.tel_show)
    private ImageButton tel_show;
    @ViewInject(R.id.tel_mianban)
    private RelativeLayout telMianban;
    @ViewInject(R.id.number_layout)
    private RelativeLayout number_layout;
    @ViewInject(R.id.phoneNumber_edit)
    private EditText phoneNumber_edit;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_qq_friend, null);
        mSearchView = inflater.inflate(R.layout.common_search_xxl, null);
        ViewUtils.inject(this, mBaseView); //注入view和事件
        findView();
        init();
        return mBaseView;
    }

    private void findView() {
        mCustomListView = (CustomListView) mBaseView
                .findViewById(R.id.listview);
        mLoadingView = (LoadingView) mBaseView.findViewById(R.id.loading);
        sideBar = (SideBar) mBaseView.findViewById(R.id.sidrbar);
        dialog = (TextView) mBaseView.findViewById(R.id.dialog);
    }

    private void init() {
        mCustomListView.setCanLoadMore(false);
        mCustomListView.addHeaderView(mSearchView);
        mCustomListView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskQQConstact(mLoadingView).execute(0);
            }
        });

        mCustomListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (tel_show.getVisibility() == View.GONE) {
                            telMianban.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_out));
                            telMianban.setVisibility(View.GONE);
                            tel_show.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return false;
            }
        });

       /* constacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,QQconstactActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_up, R.anim.fade_out);

            }
        });*/

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mCustomListView.setSelection(position);
                }
            }
        });

        mCustomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String userId = "1";
                String title = "自问自答";
                RongIM.getInstance().startPrivateChat(getActivity(), userId, title);

            }
        });

        new AsyncTaskQQConstact(mLoadingView).execute(0);
    }

    private class AsyncTaskQQConstact extends AsyncTaskBase {
        public AsyncTaskQQConstact(LoadingView loadingView) {
            super(loadingView);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int result = -1;
            callRecords = TestData.getFriends();
            result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mCustomListView.onRefreshComplete();
            if (result == 1) {
                List<String> constact = new ArrayList<String>();
                for (Iterator<String> keys = callRecords.keySet().iterator(); keys
                        .hasNext();) {
                    String key = keys.next();
                    constact.add(key);
                }
                String[] names = new String[] {};
                names = constact.toArray(names);
                SourceDateList = filledData(names);

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new QQfriendAdapter(mContext, SourceDateList,callRecords,mCustomListView);
                mCustomListView.setAdapter(adapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
    @OnClick({R.id.add_contact_button, R.id.delete_bt, R.id.dial_callbutton_single, R.id.tel_show, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_contact_button:
                telMianban.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_out));
                telMianban.setVisibility(View.GONE);
                tel_show.setVisibility(View.VISIBLE);
                break;
            case R.id.tel_show:
                telMianban.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_in));
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
                    Toast.makeText(getActivity(), "请输入号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete_bt:
                keyPressed(KeyEvent.KEYCODE_DEL);
                break;
        }
    }

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



}
