
package com.nuobuluo.huangye.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.nuobuluo.huangye.R;
import com.nuobuluo.module.viewpager.PagerSlidingTabStrip;
import com.lidroid.xutils.ViewUtils;
import com.nuobuluo.huangye.fragment.FriendFragment;
import com.nuobuluo.huangye.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 关系主界面
 */
public class NewRelationActivity extends FragmentActivity {
    /**
     * PagerSlidingTabStrip的实例
     */
    private PagerSlidingTabStrip tabs;
    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;

    MyPagerAdapter adapter;
    ViewPager pager;
    private List<String>   titleList = new ArrayList<String>();
    Fragment friendFragment = new FriendFragment();
    Fragment groupFragment = new GroupFragment();
    private List<Fragment> fraList  = new ArrayList<Fragment>();;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_relation);
        ViewUtils.inject(this);
        pager = (ViewPager) findViewById(R.id.pager);
        initData();
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        dm = getResources().getDisplayMetrics();
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(0);
        setTabsValue();

    }

    private void initData() {

        titleList.add("好友");
        titleList.add("群组");
        fraList.add(friendFragment);
        fraList.add(groupFragment);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#45c01a"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
        //tabs.setAlpha(0);  // TODO: zxl 隐藏tab
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public int getCount() {
            return fraList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fraList.get(position);
        }
    }

}
