package com.nuobuluo.module.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nuobuluo.module.R;

/**
 * Created by zxl on 14-9-19.
 */
public class ConfirmDialog{
    private Context context;
    AlertDialog ad;
    TextView messageView;
    Button leftBtn;
    Button rightBtn;
    public  ConfirmDialog(Context context) {
        this.context=context;
        replaceLayout();
    }
    //替换布局
    private void replaceLayout(){
        ad=new AlertDialog.Builder(context).create();
        ad.show();
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.dialog_confirm);
        messageView=(TextView)window.findViewById(R.id.message);
        leftBtn = (Button)window.findViewById(R.id.btn_queding);
        rightBtn = (Button)window.findViewById(R.id.btn_quxiao);

    }

    public void setMessage(int resId) {
        messageView.setText(resId);
    }

    public void setMessage(String message)
    {
        messageView.setText(message);
    }
    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setLeftButton(String text, final View.OnClickListener listener)
    {
        leftBtn.setText(text);
        leftBtn.setTextColor(Color.WHITE);
        leftBtn.setTextSize(20);
        leftBtn.setOnClickListener(listener);
        //左边是取消按钮
        ad.dismiss();
    }

    /**
     * 设置按钮
     * @param text
     * @param listener
     */
    public void setRightButton(String text, final View.OnClickListener listener)
    {
        rightBtn.setText(text);
        rightBtn.setTextColor(Color.WHITE);
        rightBtn.setTextSize(20);
        rightBtn.setOnClickListener(listener);
    }
    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }

    public void show(){
        ad.show();
    }

}
