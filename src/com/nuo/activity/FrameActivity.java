package com.nuo.activity;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nuo.common.DownLoadManager;
import com.nuo.view.NoScrollViewPager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 软件框架界面
 * */
public class FrameActivity extends ActivityGroup {

    private LinearLayout mMyBottemSearchBtn,
            mMyBottemCheckinBtn, mMyBottemMyBtn,mMyBottemMoreBtn;
    private ImageView mMyBottemSearchImg,
            mMyBottemCheckinImg, mMyBottemMyImg,mMyBottemMoreImg;
    private TextView mMyBottemSearchTxt,  mMyBottemCheckinTxt,
            mMyBottemMyTxt,mMyBottemMoreTxt;
    private List<View> list = new ArrayList<View>();// 相当于数据源
    private View view = null;
    private View view1 = null;
    private View view2 = null;
    private View view4 = null;
    private NoScrollViewPager mViewPager;  //如果需要滑动就把isCanScroll变量修改一下
    private PagerAdapter pagerAdapter = null;// 数据源和viewpager之间的桥梁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        // tab webchat
        setOverflowShowingAlways();
        initView();
        //检测版本
        DownLoadManager.checkVersion(FrameActivity.this, false);
    }
    /** 屏蔽掉物理Menu键，不然在有物理Menu键的手机上，overflow按钮会显示不出来。 **/
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 加载 动作栏 菜单**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_collection) {
            Intent it = new Intent(FrameActivity.this, MyActivity.class);
            startActivity(it);
        }
        return super.onMenuItemSelected(featureId, item);
    }
    /**用于让隐藏在overflow当中的Action按钮的图标显示出来  **/
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    // 初始化控件
    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.FramePager);
        // 查找以linearlayout为按钮作用的控件
        mMyBottemSearchBtn = (LinearLayout) findViewById(R.id.MyBottemSearchBtn);
        mMyBottemCheckinBtn = (LinearLayout) findViewById(R.id.MyBottemCheckinBtn);
        mMyBottemMyBtn = (LinearLayout) findViewById(R.id.MyBottemMyBtn);
		mMyBottemMoreBtn = (LinearLayout) findViewById(R.id.MyBottemMoreBtn);
        // 查找linearlayout中的imageview
        mMyBottemSearchImg = (ImageView) findViewById(R.id.MyBottemSearchImg);
        mMyBottemCheckinImg = (ImageView) findViewById(R.id.MyBottemCheckinImg);
        mMyBottemMyImg = (ImageView) findViewById(R.id.MyBottemMyImg);
		mMyBottemMoreImg = (ImageView) findViewById(R.id.MyBottemMoreImg);
        // 查找linearlayout中的textview
        mMyBottemSearchTxt = (TextView) findViewById(R.id.MyBottemSearchTxt);
        mMyBottemCheckinTxt = (TextView) findViewById(R.id.MyBottemCheckinTxt);
        mMyBottemMyTxt = (TextView) findViewById(R.id.MyBottemMyTxt);
		mMyBottemMoreTxt = (TextView) findViewById(R.id.MyBottemMoreTxt);
        createView();
        // 写一个内部类pageradapter
        pagerAdapter = new PagerAdapter() {
            // 判断再次添加的view和之前的view 是否是同一个view
            // arg0新添加的view，arg1之前的
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            // 返回数据源长度
            @Override
            public int getCount() {
                return list.size();
            }

            // 销毁被滑动走的view
            /**
             * ViewGroup 代表了我们的viewpager 相当于activitygroup当中的view容器， 添加之前先移除。
             * position 第几条数据 Object 被移出的view
             * */
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // 移除view
                container.removeView(list.get(position));
            }

            /**
             * instantiateItem viewpager要现实的view ViewGroup viewpager position
             * 第几条数据
             * */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 获取view添加到容器当中，并返回
                View v = list.get(position);
                container.addView(v);
                return v;
            }
        };
        // 设置我们的adapter
        mViewPager.setAdapter(pagerAdapter);

        MyBtnOnclick mytouchlistener = new MyBtnOnclick();
        mMyBottemSearchBtn.setOnClickListener(mytouchlistener);
        mMyBottemCheckinBtn.setOnClickListener(mytouchlistener);
        mMyBottemMyBtn.setOnClickListener(mytouchlistener);
		mMyBottemMoreBtn.setOnClickListener(mytouchlistener);

        // 设置viewpager界面切换监听,监听viewpager切换第几个界面以及滑动的
        /*mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            // arg0 获取 viewpager里面的界面切换到第几个的
            @Override
            public void onPageSelected(int arg0) {
                // 先清除按钮样式
                initBottemBtn();
                // 按照对应的view的tag来判断到底切换到哪个界面。
                // 更改对应的button状态
                int flag = (Integer) list.get((arg0)).getTag();
                if (flag == 0) {
                    mMyBottemSearchImg
                            .setImageResource(R.drawable.wb_home_tap_index_pressed);
                    mMyBottemSearchTxt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 1) {
                    mMyBottemCheckinImg
                            .setImageResource(R.drawable.wb_home_tap_publish_pressed);
                    mMyBottemCheckinTxt.setTextColor(Color
                            .parseColor("#FF8C00"));
                } else if (flag == 2) {
                    mMyBottemMyImg
                            .setImageResource(R.drawable.wb_home_tap_center_pressed);
                    mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 3) {
					mMyBottemMoreImg
							.setImageResource(R.drawable.wb_home_tap_center_pressed);
					mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				}
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }


            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/

    }

    // 把viewpager里面要显示的view实例化出来，并且把相关的view添加到一个list当中
    private void createView() {
        view = this
                .getLocalActivityManager()
                .startActivity("search",
                        new Intent(FrameActivity.this, SmsActivity.class))
                .getDecorView();
        // 用来更改下面button的样式的标志
        view.setTag(0);
        list.add(view);
        view1 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("tuan",
                        new Intent(FrameActivity.this, RelationActivity.class))
                .getDecorView();
        view1.setTag(1);
        list.add(view1);

        view2 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("my",
                        new Intent(FrameActivity.this, SearchActivity.class))
                .getDecorView();
        view2.setTag(2);
        list.add(view2);

        view4 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("more",
                        new Intent(FrameActivity.this, ToolActivity.class))
                .getDecorView();
        view4.setTag(3);
        list.add(view4);
    }

    /**
     * 用linearlayout作为按钮的监听事件
     * */
    private class MyBtnOnclick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            int mBtnid = arg0.getId();
            switch (mBtnid) {
                case R.id.MyBottemSearchBtn:
                    // //设置我们的viewpager跳转那个界面0这个参数和我们的list相关,相当于list里面的下标
                    mViewPager.setCurrentItem(0);
                    initBottemBtn();
                    mMyBottemSearchImg
                            .setImageResource(R.drawable.wb_home_tap_index_pressed);
                    mMyBottemSearchTxt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
			/*case R.id.MyBottemTuanBtn:
				mViewPager.setCurrentItem(1);
				initBottemBtn();
				mMyBottemTuanImg
						.setImageResource(R.drawable.main_index_tuan_pressed);
				mMyBottemTuanTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;*/
                case R.id.MyBottemCheckinBtn:
                    mViewPager.setCurrentItem(1);
                    initBottemBtn();
                    mMyBottemCheckinImg
                            .setImageResource(R.drawable.wb_home_tap_publish_pressed);
                    mMyBottemCheckinTxt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
                case R.id.MyBottemMyBtn:
                    mViewPager.setCurrentItem(2);
                    initBottemBtn();
                    mMyBottemMyImg
                            .setImageResource(R.drawable.wb_home_tap_center_pressed);
                    mMyBottemMyTxt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
			case R.id.MyBottemMoreBtn:
				mViewPager.setCurrentItem(3);
				initBottemBtn();
				mMyBottemMoreImg
						.setImageResource(R.drawable.wb_home_tap_center_pressed);
				mMyBottemMoreTxt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			}

            }
    }
    /**
     * 初始化控件的颜色
     */
    private void initBottemBtn() {
        mMyBottemSearchImg.setImageResource(R.drawable.search_bottem_search);
        mMyBottemCheckinImg.setImageResource(R.drawable.search_bottem_checkin);
        mMyBottemMyImg.setImageResource(R.drawable.search_bottem_my);
        mMyBottemMoreImg.setImageResource(R.drawable.search_bottem_my);
        mMyBottemSearchTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        mMyBottemCheckinTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        mMyBottemMyTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        mMyBottemMoreTxt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
    }

    /**
     * 返回按钮的监听，用来询问用户是否退出程序
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Builder builder = new Builder(FrameActivity.this);
                builder.setTitle("提示");
                builder.setMessage("你确定要退出吗？");
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                            arg0.cancel();
                        } else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {
                            FrameActivity.this.finish();
                        }
                    }
                };
                builder.setPositiveButton("取消", dialog);
                builder.setNegativeButton("确定", dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        return false;
    } 
}