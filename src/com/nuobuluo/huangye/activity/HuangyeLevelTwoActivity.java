package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.bean.ShengHuoType;
import com.nuobuluo.huangye.handler.TimeOutHandler;
import com.nuobuluo.huangye.utils.NetUtil;
import com.nuobuluo.huangye.utils.PreferenceConstants;
import com.nuobuluo.huangye.utils.T;

import java.util.List;

/**
 * 搜全城模块
 */
public class HuangyeLevelTwoActivity extends Activity {

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
    private String toView;

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
        toView = getIntent().getStringExtra("toView");
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
                LinearLayout layout2 = new LinearLayout(HuangyeLevelTwoActivity.this);
                int col = 0;
                int row= 1;
                for (final ShengHuoType shengHuoType : shengHuoTypeList) {
                    col++;
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
                            if ("fabu".equals(toView)) {
                                 intent = new Intent(HuangyeLevelTwoActivity.this, FabuActivity.class);
                            }
                            intent.putExtra("typeCode", shengHuoType.getTypeCode());
                            intent.putExtra("typeName", shengHuoType.getTypeName());
                            intent.putExtra("parentTypeCode", shengHuoType.getLevel1Code());
                            startActivity(intent);
                        }
                    });
                    layout2.addView(textView);
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 5;
                    layoutParams.rightMargin = 5;
                    layout2.setPadding(0, 0, 1, 0);
                    if (row == 1&& col==1) {  //第一个需要自己手动添加
                        shenghuo_two.addView(layout2, layoutParams);
                    }

                    if (col % 3 == 0) {  //每三个从新创建一个LinearLayout.
                        layout2 = new LinearLayout(HuangyeLevelTwoActivity.this);
                        col = 0;
                        row++;
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
