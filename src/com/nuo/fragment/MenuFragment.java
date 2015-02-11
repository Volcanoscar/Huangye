package com.nuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.ViewUtils;
import com.nuo.activity.R;

/**
 * 聊天Fragment的界面
 *
 * @author guolin
 */
public class MenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mBaseView = inflater.inflate(R.layout.fragment_menu, null);
        ViewUtils.inject(this, mBaseView); //注入view和事件
        return mBaseView;
    }
}
