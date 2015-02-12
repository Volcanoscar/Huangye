package com.nuo.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.fujie.common.SystemMethod;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.fragment.MenuFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/2/10.
 */
public class CenterActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    private ImageView[] tips;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    @ViewInject(R.id.viewGroup)
    private ViewGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ViewUtils.inject(this);
        initData();
    }

    @OnClick(R.id.back_layout)
    public void click(View view) {
        finish();
    }

    private void initData() {
        MenuFragment menuFragment = new MenuFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.push_bottom_in,
                R.anim.push_bottom_out);
        fragmentTransaction.commit();
        fragmentList.add(menuFragment);
        tips = new ImageView[fragmentList.size()];
        if (tips.length > 1) {
            for (int i = 0; i < tips.length; i++) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
                layoutParams.setMargins(0, 0, SystemMethod.dip2px(this, 5), 0);
                imageView.setLayoutParams(layoutParams);
                tips[i] = imageView;
                if (i == 0) {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                } else {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
                }
                group.addView(imageView);
            }
        }

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    /**
     * @author xiaanming
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % fragmentList.size());
    }

    /**
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }
        }
    }
}
