package com.nuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujie.common.SystemMethod;
import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.dialog.AlertDialog;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.fujie.module.horizontalListView.ViewBean;
import com.fujie.module.tab.SpinnerPopWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nuo.adapter.SmallImageListViewAdapter;
import com.nuo.bean.District;
import com.nuo.bean.Publish;
import com.nuo.bean.ShengHuoType;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.NetUtil;
import com.nuo.utils.PreferenceConstants;
import com.nuo.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxl on 2015/1/30.
 */
public class FabuActivity extends AbstractTemplateActivity {

    /**
     * 添加后的图片显示区域*
     */
    @ViewInject(R.id.horizontalListView)
    private HorizontalListView horizontalListView;
    @ViewInject(R.id.main)
    private RelativeLayout main;
    @ViewInject(R.id.category_layout)
    private LinearLayout category_layout;
    @ViewInject(R.id.changeAreaTextView)
    private TextView changeAreaTextView;

    @ViewInject(R.id.titleTextView)
    private TextView titleTextView;
    @ViewInject(R.id.telTextView)
    private TextView telTextView;

    @ViewInject(R.id.weixinCodeTextView)
    private TextView weixinCodeTextView;
    @ViewInject(R.id.qqTextView)
    private TextView qqTextView;

    @ViewInject(R.id.contentTextView)
    private TextView contentTextView;

    SpinnerPopWindow puCountyWindow = null;
    private String typeCode;
    private Publish publish = new Publish();
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 过滤tab数据集合 *
     */
    private List<ViewBean> filterTabViewBeanList = new ArrayList<ViewBean>();
    private String parentTypeCode;
    private TimeOutHandler timeOutHandler = new TimeOutHandler(this);
    private SmallImageListViewAdapter smallImageListViewAdapter;
    private List<String> imgAddList = new ArrayList<String>();
    private String picPath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initData() {
        //得到变量
        typeCode = getIntent().getStringExtra("typeCode");
        parentTypeCode = getIntent().getStringExtra("parentTypeCode");
    }


    private void initView() {
        //选择区域
        puCountyWindow = new SpinnerPopWindow(FabuActivity.this);
        //显示类别
        queryShenghuoTypeData();
        //选择后的相册区域组件
        smallImageListViewAdapter = new SmallImageListViewAdapter(this, imgAddList);
        horizontalListView.setAdapter(smallImageListViewAdapter);

    }

    private void queryShenghuoTypeData() {
        //得到过滤信息(区域以及三级分类)
        NetUtil.shenhuoType(PreferenceConstants.SHENHUO_TYPE_THREE, typeCode, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                //判断是否有三级分类、判断是否有四级分类(判断规则如下：一旦有一个三级分类有四级分类，则所有的三级分类有四级分类)
                List<ShengHuoType> shengHuoTypeList = ShengHuoType.parseMap(stringResponseInfo.result);
                if (shengHuoTypeList != null && !shengHuoTypeList.isEmpty()) {
                    if (PreferenceConstants.IS_PARENT_TRUE.equals(shengHuoTypeList.get(0).getIsParent())) { //如果有四级菜单
                        filterTabViewBeanList.addAll(ShengHuoType.getlevel4ViewBean(shengHuoTypeList));
                    } else {
                        filterTabViewBeanList.add(ShengHuoType.getlevel3ViewBean(shengHuoTypeList));
                    }
                }
                //创建类别
                for (final ViewBean temp : filterTabViewBeanList) {
                    RelativeLayout relativeLayout = new RelativeLayout(FabuActivity.this);
                    RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SystemMethod.dip2px(FabuActivity.this, 50));
                    relativeLayout.setLayoutParams(relativeLayoutParams);
                    relativeLayout.setBackgroundResource(R.drawable.item_bg);
                    TextView nameTextView = new TextView(FabuActivity.this);
                    final TextView tipTextView = new TextView(FabuActivity.this);
                    tipTextView.setHint("请选择");
                    tipTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            puCountyWindow.initData(temp);
                            puCountyWindow.setItemSelectListener(new SpinnerPopWindow.IOnItemSelectListener() {
                                @Override
                                public void onItemClick(String tag, ViewBean viewBean, int position) {
                                    tipTextView.setText(viewBean.getText());
                                    if (filterTabViewBeanList.size() == 1) { //没有四级分类
                                        publish.addTypeList(viewBean.getId());
                                    } else { //有四级分类
                                        publish.addTypeList(temp.getId() + "::" + viewBean.getId());
                                    }
                                }
                            });
                            puCountyWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            puCountyWindow.update();
                        }
                    });
                    nameTextView.setId(1);
                    nameTextView.setText(temp.getText());
                    nameTextView.setTextColor(getResources().getColor(R.color.grey));
                    RelativeLayout.LayoutParams nameTextViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    nameTextViewLayoutParams.setMargins(SystemMethod.dip2px(FabuActivity.this, 10), 0, 0, 0);
                    nameTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    relativeLayout.addView(nameTextView, nameTextViewLayoutParams);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.RIGHT_OF, 1);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    layoutParams.setMargins(SystemMethod.dip2px(FabuActivity.this, 10), 0, 0, 0);
                    relativeLayout.addView(tipTextView, layoutParams);
                    category_layout.addView(relativeLayout);
                    View view = new View(FabuActivity.this);
                    ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    view.setLayoutParams(layout);
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    category_layout.addView(view);

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(FabuActivity.this, R.string.net_error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            picPath = data.getStringExtra(ChangePicActivity.KEY_PHOTO_PATH);
            Log.i(FabuActivity.this.toString(), "最终选择的图片=" + picPath);
            smallImageListViewAdapter.addLocalImgAddList(picPath);
            smallImageListViewAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * add_pic 添加图片按钮
     */
    @OnClick({R.id.add_pic, R.id.changeAreaTextView, R.id.publish_btn})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.add_pic:
                //使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
                startActivityForResult(new Intent(FabuActivity.this,
                        ChangePicActivity.class), 1);
                break;
            case R.id.changeAreaTextView:
                showDistrictDialog();
                break;
            case R.id.publish_btn:
                savePublish();
                break;
        }
    }

    private void savePublish() {
        //判断
        publish.setQq(qqTextView.getText().toString());
        publish.setWeixin(weixinCodeTextView.getText().toString());
        publish.setContent(contentTextView.getText().toString());
        publish.setTitle(titleTextView.getText().toString());
        publish.setLevel1Code(parentTypeCode);
        publish.setLevel2Code(typeCode);
        //上传图片
        NetUtil.uploadImg(smallImageListViewAdapter.getImgAddList(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                uploadPublish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(FabuActivity.this, R.string.net_error);
                timeOutHandler.stop();
            }

            @Override
            public void onStart() {
                timeOutHandler.start(null);
            }
        });
    }

    private void uploadPublish() {
        //上传
        NetUtil.publish(publish, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                timeOutHandler.stop();
                new AlertDialog(FabuActivity.this).builder().setTitle("提示").setMsg("发布成功")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FabuActivity.this, FrameActivity.class);
                                intent.putExtra("click", "huangye");
                                startActivity(intent);
                            }
                        });

            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(FabuActivity.this, R.string.net_error);
                timeOutHandler.stop();
            }
        });
    }

    /**
     * 显示选择行政区划弹出框
     */
    private void showDistrictDialog() {
        NetUtil.queryDistrictList(true, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                //初始化数据
                List<District> removeDistrictList = District.parseMap(stringResponseInfo.result);
                List<ViewBean> mapList = District.convertDistrictToViewBean(removeDistrictList);
                //初始化filterTabLayout
                ViewBean viewBean = ViewBean.getDistrictViewBean();
                viewBean.setBizAreaList(mapList);
                puCountyWindow = new SpinnerPopWindow(FabuActivity.this);
                puCountyWindow.initData(viewBean);
                puCountyWindow.setItemSelectListener(new SpinnerPopWindow.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(String tag, ViewBean viewBean, int position) {
                        changeAreaTextView.setText(viewBean.getText());
                        publish.addBizArea(viewBean.getId());
                    }
                });
                puCountyWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                puCountyWindow.update();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.showShort(FabuActivity.this, R.string.net_error);
            }
        });
    }
}
