package com.nuo.activity;

import android.app.ActionBar;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.nuo.adapter.ContactsLoaderListener;
import com.nuo.adapter.SmsLoaderListener;
import com.nuo.common.DownLoadManager;
import com.nuo.floatwindow.FloatWindowService;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.PreferenceUtils;
import com.nuo.utils.Utils;
import com.nuo.view.NoScrollViewPager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 软件框架界面
 * */
public class FrameActivity extends ActivityGroup {

    private LinearLayout sms_layout,
            relation_layout, huangye_layout, tool_layout;
    private ImageView sms_img,
            relation_img, huangye_img, tool_img;
    private TextView sms_txt, relation_txt,
            huangye_txt, tool_txt;
    private List<View> list = new ArrayList<View>();// 相当于数据源
    private View view = null;
    private View view1 = null;
    private View view2 = null;
    private View view3 = null;
    private NoScrollViewPager mViewPager;  //如果需要滑动就把isCanScroll变量修改一下
    private PagerAdapter pagerAdapter = null;// 数据源和viewpager之间的桥梁

    //联系人加载器监听器
    private ContactsLoaderListener m_ContactsCallback = new ContactsLoaderListener(this);
    //SMS加载器监听器
    private SmsLoaderListener m_SmsCallback = new SmsLoaderListener(this);
    private LinearLayout center_layout;
    private ImageView center_img;
    private TextView center_txt;
    private View view4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        // tab webchat
        initActionBar();
        //通讯录相关
        Utils.init(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.huahua.SMS_Loader");
        registerReceiver(mReceiver , filter);
        getLoaderManager().initLoader(0,null,m_ContactsCallback);
        initView();
        String click =getIntent().getStringExtra("click");
        if ("huangye".equals(click)) {
            huangye_layout.performClick();
        }
        //检测版本
        DownLoadManager.checkVersion(FrameActivity.this, false);
        //添加快捷方式
       /* if(!ShortcutUtils.hasInstallShortcut(FrameActivity.this)) {
            sendBroadcast(ShortcutUtils.getShortcutToDesktopIntent(FrameActivity.this));
        }*/
        overridePendingTransition(0,0);

        //首页service
        Intent startIntent = new Intent(this, FloatWindowService.class);
        startService(startIntent);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        setOverflowShowingAlways();
        actionBar.setDisplayShowHomeEnabled(false); //隐藏logo和icon
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals("android.huahua.SMS_Loader"))
            {
                getLoaderManager().initLoader(2,null,m_SmsCallback);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
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




    /**
     *  加载 动作栏 菜单<br>
     *  根据不同的Activity修改ActionBar上的动作，并修改标题
     *  **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int currItem = mViewPager.getCurrentItem();
        ActionBar actionBar = getActionBar();
        final Integer accountId = PreferenceUtils.getPrefInt(FrameActivity.this,
                PreferenceConstants.ACCOUNT_ID, -1);
        if (currItem==0) { // 黄页
            getMenuInflater().inflate(R.menu.huangye_action, menu);
            actionBar.setTitle(R.string.huangye);
            MenuItem searchItem =menu.findItem(R.id.action_info_search);
            MenuItem fabuItem =menu.findItem(R.id.action_fabu_sms);
            MenuItem myShopItem =menu.findItem(R.id.action_my_shop);
            // 查询项 点击事件
            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    //跳转到搜索个界面
                    Intent intent = new Intent(FrameActivity.this, SearchActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
            //  发布项 点击事件
            fabuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    //跳转到发布界面
                    if (accountId!=-1) {  //进入发布详情
                        Intent intent = new Intent(FrameActivity.this, ChangeFabuTypeActivity.class);
                        startActivity(intent);
                    }else{  //进入登录界面
                        Intent intent = new Intent(FrameActivity.this, LoginActivity.class);
                        intent.putExtra("toView", "fabu");
                        startActivity(intent);
                    }
                    return false;
                }
            });

            //  发布项 点击事件
            myShopItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    //跳转到店铺界面
                    if (accountId!=-1) {  //进入店铺详情
                        Intent intent = new Intent(FrameActivity.this, ShopDetailActivity.class);
                        startActivity(intent);
                    }else{  //进入登录界面
                        Intent intent = new Intent(FrameActivity.this, LoginActivity.class);
                        intent.putExtra("toView", "shop");
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }else if(currItem==1){ //关系
            getMenuInflater().inflate(R.menu.main, menu);
            actionBar.setTitle(R.string.relation);

        }else if(currItem==3){ // 信息
            getMenuInflater().inflate(R.menu.sms_action, menu);
            actionBar.setTitle(R.string.sms);

        }else if(currItem==4){ //工具
            getMenuInflater().inflate(R.menu.tuan_details, menu);
            actionBar.setTitle(R.string.tool);
        }

        //每个动作栏中都有反馈项
        MenuItem feedbackItem =menu.findItem(R.id.action_feedback);
        if(feedbackItem!=null){
            feedbackItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(FrameActivity.this, FeedBackActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_plus) {
            Bundle bundle = new Bundle();
            bundle.putInt("tpye", 0);
            bundle.putString("name", "");
            bundle.putString("number", "");
            Intent intent = new Intent(FrameActivity.this, AddContactsActivity.class);
            intent.putExtra("person", bundle);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.action_add_sms){  //添加信息
            Intent intent = new Intent(FrameActivity.this, NewSmsActivity.class);
            startActivity(intent);
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
        sms_layout = (LinearLayout) findViewById(R.id.sms_layout);
        relation_layout = (LinearLayout) findViewById(R.id.relation_layout);
        huangye_layout = (LinearLayout) findViewById(R.id.huangye_layout);
        tool_layout = (LinearLayout) findViewById(R.id.tool_layout);
        center_layout = (LinearLayout) findViewById(R.id.center_layout);
        // 查找linearlayout中的imageview
        sms_img = (ImageView) findViewById(R.id.sms_img);
        relation_img = (ImageView) findViewById(R.id.relation_img);
        huangye_img = (ImageView) findViewById(R.id.huangye_img);
        tool_img = (ImageView) findViewById(R.id.tool_img);
        center_img = (ImageView) findViewById(R.id.center_img);
        // 查找linearlayout中的textview
        sms_txt = (TextView) findViewById(R.id.sms_txt);
        relation_txt = (TextView) findViewById(R.id.relation_txt);
        huangye_txt = (TextView) findViewById(R.id.huangye_txt);
        tool_txt = (TextView) findViewById(R.id.tool_txt);
        center_txt = (TextView) findViewById(R.id.center_txt);
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
        sms_layout.setOnClickListener(mytouchlistener);
        center_layout.setOnClickListener(mytouchlistener);
        relation_layout.setOnClickListener(mytouchlistener);
        huangye_layout.setOnClickListener(mytouchlistener);
		tool_layout.setOnClickListener(mytouchlistener);

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
                    sms_img
                            .setImageResource(R.drawable.wb_home_tap_index_pressed);
                    sms_txt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 1) {
                    relation_img
                            .setImageResource(R.drawable.wb_home_tap_publish_pressed);
                    relation_txt.setTextColor(Color
                            .parseColor("#FF8C00"));
                } else if (flag == 2) {
                    huangye_img
                            .setImageResource(R.drawable.wb_home_tap_center_pressed);
                    huangye_txt.setTextColor(Color.parseColor("#FF8C00"));
                } else if (flag == 3) {
					tool_img
							.setImageResource(R.drawable.wb_home_tap_center_pressed);
					tool_txt.setTextColor(Color.parseColor("#FF8C00"));
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

        view = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("huangye",
                        new Intent(FrameActivity.this, HuangyeActivity.class))
                .getDecorView();
        view.setTag(0);

        list.add(view);

        view1 = this
                .getLocalActivityManager()
                .startActivity("relation",
                        new Intent(FrameActivity.this, RelationActivity.class))
                .getDecorView();
        // 用来更改下面button的样式的标志
        view1.setTag(1);
        list.add(view1);

        view2 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("center",
                        new Intent(FrameActivity.this, CenterActivity.class))
                .getDecorView();
        view2.setTag(2);
        list.add(view2);
        view3 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("sms",
                        new Intent(FrameActivity.this, SmsActivity.class))
                .getDecorView();
        view3.setTag(3);
        list.add(view3);

        view4 = FrameActivity.this
                .getLocalActivityManager()
                .startActivity("tool",
                        new Intent(FrameActivity.this, ToolActivity.class))
                .getDecorView();
        view4.setTag(4);
        view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        view1.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        view2.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        view3.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        view4.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
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
                case R.id.center_layout:
                    // //设置我们的viewpager跳转那个界面0这个参数和我们的list相关,相当于list里面的下标
                    //mViewPager.setCurrentItem(4);
                    Intent intent = new Intent(FrameActivity.this, CenterActivity.class);
                    startActivity(intent);
                   /* initBottemBtn();
                    center_img
                            .setImageResource(R.drawable.wb_home_tap_center_pressed);
                    center_txt.setTextColor(Color.parseColor("#FF8C00"));*/
                    break;
                case R.id.sms_layout:
                    // //设置我们的viewpager跳转那个界面0这个参数和我们的list相关,相当于list里面的下标
                    mViewPager.setCurrentItem(3,false);
                    initBottemBtn();
                    sms_img
                            .setImageResource(R.drawable.wb_home_tap_index_pressed);
                    sms_txt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
                case R.id.relation_layout:
                    mViewPager.setCurrentItem(1,false);
                    initBottemBtn();
                    relation_img
                            .setImageResource(R.drawable.wb_home_tap_publish_pressed);
                    relation_txt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
                case R.id.huangye_layout:
                    mViewPager.setCurrentItem(0,false);
                    initBottemBtn();
                    huangye_img
                            .setImageResource(R.drawable.wb_home_tap_center_pressed);
                    huangye_txt.setTextColor(Color.parseColor("#FF8C00"));
                    break;
			case R.id.tool_layout:
				mViewPager.setCurrentItem(4,false);
				initBottemBtn();
				tool_img
						.setImageResource(R.drawable.wb_home_tap_center_pressed);
				tool_txt.setTextColor(Color.parseColor("#FF8C00"));
				break;
			}
            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            }
    }
    /**
     * 初始化控件的颜色
     */
    private void initBottemBtn() {
        center_img.setImageResource(R.drawable.search_bottem_my);
        sms_img.setImageResource(R.drawable.search_bottem_search);
        relation_img.setImageResource(R.drawable.search_bottem_checkin);
        huangye_img.setImageResource(R.drawable.search_bottem_my);
        tool_img.setImageResource(R.drawable.search_bottem_my);
        center_txt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        sms_txt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        relation_txt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        huangye_txt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
        tool_txt.setTextColor(getResources().getColor(
                R.color.search_bottem_textcolor));
    }

    /**
     * 返回按钮的监听，用来询问用户是否退出程序
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        return false;
    } 
}