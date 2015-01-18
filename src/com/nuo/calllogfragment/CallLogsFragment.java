package com.nuo.calllogfragment;

import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.nuo.ContentObserver.CallLogsContentObserver;
import com.nuo.activity.R;
import com.nuo.utils.Utils;

public class CallLogsFragment extends Fragment {
    //通话记录的界面
    private View CallLogView;
    //通话记录的列表
    private ListView m_calllogslist;
    //通话记录内容观察者
    private CallLogsContentObserver CallLogsCO;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        CallLogView = inflater.inflate(R.layout.calllogsfragment, null);

        CallLogsCO = new CallLogsContentObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI , false, CallLogsCO);

        //通话记录列表
        m_calllogslist = (ListView)CallLogView.findViewById(R.id.calllogs_list);
        m_calllogslist.setAdapter(Utils.m_calllogsadapter);
//		m_calllogslist.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Utils.mPersonCallLogList.get(arg2).choose = true;
//				Utils.m_calllogsadapter.notifyDataSetChanged();
//				
//			}
//		});

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup p = (ViewGroup) CallLogView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }

        return CallLogView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
