package com.nuo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.activity.MyActivity;
import com.nuo.activity.NoteBookActivity;
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

    @OnClick({R.id.user_center_layout, R.id.notebook_layout})
    public void click(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.user_center_layout:
                 intent = new Intent(getActivity(), MyActivity.class);
                startActivity(intent);
                break;
            case R.id.notebook_layout:
                intent = new Intent(getActivity(), NoteBookActivity.class);
                startActivity(intent);
                break;
        }
    }
}
