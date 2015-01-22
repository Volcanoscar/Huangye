package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.bean.ShengHuoType;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.NetUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜全城模块
 */
public class HuangyeLevelTwoActivity extends Activity {

    private ImageView mSouquancheng_back;
    private TextView mSouquancheng_moresearch, mSouquancheng_text1,
            mSouquancheng_text2, mSouquancheng_text3, mSouquancheng_text4,
            mSouquancheng_text5, mSouquancheng_text6, mSouquancheng_text7,
            mSouquancheng_text8, mSouquancheng_text9, mSouquancheng_text10,
            mSouquancheng_text11, mSouquancheng_text12, mSouquancheng_text13,
            mSouquancheng_text14, mSouquancheng_text15, mSouquancheng_text16,
            mSouquancheng_text17, mSouquancheng_text18, mSouquancheng_text19,
            mSouquancheng_text20, mSouquancheng_text21, mSouquancheng_text22;

    /**
     * 父类ID *
     */
    @ViewInject(R.id.parent_name)
    private TextView parent_name;
    @ViewInject(R.id.Souquancheng_title_txt)
    private TextView titleText;

    @ViewInject(R.id.shenghuo_two)
    private LinearLayout shenghuo_two;

    //加载提示
    private TimeOutHandler timeOutHandler = new TimeOutHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sou_quan_cheng);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initData() {
        String parentName = getIntent().getStringExtra("parentName");
        parent_name.setText(parentName);
        titleText.setText(parentName);
    }

    @OnClick(R.id.Souquancheng_back)
    private void onclick(View v) {
          finish();
    }

    private void initView() {
        //请求分类接口
        NetUtil.shenhuoType(PreferenceConstants.SHENHUO_TYPE_TWO, PreferenceConstants.SHENHUO_CODE_JIAZHENG, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                timeOutHandler.stop();
                //动态添加布局
                List<ShengHuoType> shengHuoTypeList = ShengHuoType.parseMap(stringResponseInfo.result);
                List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
                LinearLayout layout2 = new LinearLayout(HuangyeLevelTwoActivity.this);
                int i = 0;
                for (final ShengHuoType shengHuoType : shengHuoTypeList) {
                    i++;
                    TextView textView = new TextView(HuangyeLevelTwoActivity.this);
                    textView.setTextAppearance(HuangyeLevelTwoActivity.this, R.style.shenghuo_type_two);
                    textView.setText(shengHuoType.getTypeName());
                    final LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp1.weight = 1;
                    lp1.setMargins(5, 5, 5, 5);
                    textView.setBackgroundResource(R.drawable.my_tab_background);
                    textView.setSingleLine(true);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(0, 15, 0, 15);
                    textView.setLayoutParams(lp1);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HuangyeLevelTwoActivity.this, HuangyeListActivity.class);
                            intent.putExtra("typeCode", shengHuoType.getTypeCode());
                            startActivity(intent);
                        }
                    });
                    layout2.addView(textView);
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 5;
                    layoutParams.rightMargin = 5;
                    layout2.setPadding(0, 0, 1, 0);
                    if (i % 3 == 0) {
                        layout2 = new LinearLayout(HuangyeLevelTwoActivity.this);
                        i = 0;
                        shenghuo_two.addView(layout2, layoutParams);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                timeOutHandler.stop();
                T.showShort(HuangyeLevelTwoActivity.this, R.string.net_error);
            }

            @Override
            public void onStart() {
                timeOutHandler.start(null);
            }
        });

    }
}
