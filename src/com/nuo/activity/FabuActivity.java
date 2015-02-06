package com.nuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fujie.module.activity.AbstractTemplateActivity;
import com.fujie.module.horizontalListView.HorizontalListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Map;

/**
 * Created by zxl on 2015/1/30.
 */
public class FabuActivity extends AbstractTemplateActivity {

    /**
     * 添加后的图片显示区域*
     */
    @ViewInject(R.id.horizontalListView)
    private HorizontalListView horizontalListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        initView();
    }

    private void initView() {

    }
    /**
     * add_pic 添加图片按钮
     */
    @OnClick({R.id.add_pic})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.add_pic:
                /*ActionSheetDialog holeDialog= new ActionSheetDialog(BallSettingActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true);
                for(final Map.Entry<Integer,String> entry: GsoftApplication.getInstance().getBeginHole().entrySet()){
                    holeDialog.addSheetItem(entry.getValue(),ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    setting.setTuiType(entry.getKey());
                                    holeValue.setText(entry.getValue());
                                }
                            });
                }*/
                break;
        }
    }
}
