package com.nuobuluo.huangye.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuobuluo.module.expandview.IphoneTreeView;
import com.nuobuluo.module.listview.LoadingView;
import com.nuobuluo.module.wifi.AsyncTaskBase;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.adapter.ExpAdapter;
import com.nuobuluo.huangye.bean.RecentChat;
import com.nuobuluo.huangye.test.TestData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by APPLE on 2015/3/16.
 */
public class GroupFragment extends Fragment {

    private Context mContext;
    private View mBaseView;
    private LoadingView mLoadingView;
    private IphoneTreeView mIphoneTreeView;
   /* private View mSearchView;*/
    private ExpAdapter mExpAdapter;
    /*private RelativeLayout constacts;*/
    private HashMap<String, List<RecentChat>> maps = new HashMap<String, List<RecentChat>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_group, null);
        findView();
        init();
        return mBaseView;
    }

    private void findView() {
       /* mSearchView=mBaseView.findViewById(R.id.search);*/
        mLoadingView = (LoadingView) mBaseView.findViewById(R.id.loadingView);
        mIphoneTreeView = (IphoneTreeView) mBaseView.findViewById(R.id.iphone_tree_view);
        /*constacts=(RelativeLayout) mBaseView.findViewById(R.id.rl_tonxunru);*/
    }

    private void init() {
        mIphoneTreeView.setHeaderView(LayoutInflater.from(mContext).inflate(
                R.layout.fragment_constact_head_view, mIphoneTreeView, false));
        mIphoneTreeView.setGroupIndicator(null);
        mExpAdapter = new ExpAdapter(mContext, maps, mIphoneTreeView,null);
        mIphoneTreeView.setAdapter(mExpAdapter);

       /* constacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, QQconstactActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_up, R.anim.fade_out);

            }
        });*/

        new AsyncTaskLoading(mLoadingView).execute(0);
    }

    private class AsyncTaskLoading extends AsyncTaskBase {
        public AsyncTaskLoading(LoadingView loadingView) {
            super(loadingView);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int result = -1;
            maps.put("我的同学", TestData.getRecentChats());
            maps.put("我的朋友", TestData.getRecentChats());
            maps.put("家人", TestData.getRecentChats());
            result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
