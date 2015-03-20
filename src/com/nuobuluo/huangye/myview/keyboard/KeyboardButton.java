package com.nuobuluo.huangye.myview.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.nuobuluo.huangye.R;

/**
 * Created by Administrator on 15-1-21.
 */
public class KeyboardButton extends TextView implements View.OnClickListener {
    private static final String TAG = "KeyboardButton";

    public static final int TEXT_ALIGN_LEFT              = 0x00000001;
    public static final int TEXT_ALIGN_RIGHT             = 0x00000010;
    public static final int TEXT_ALIGN_CENTER_VERTICAL   = 0x00000100;
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL = 0x00001000;
    public static final int TEXT_ALIGN_TOP               = 0x00010000;
    public static final int TEXT_ALIGN_BOTTOM            = 0x00100000;


    /** �ؼ����� */
    private Paint paint;
    /** ���ֵķ�λ */
    private int textAlign;
    /** ���ֵ���ɫ */
    private int textColor;
    /** �ؼ��Ŀ�� */
    private int viewWidth;
    /** �ؼ��ĸ߶� */
    private int viewHeight;
    /** �ı�������X���� */
    private float textCenterX;
    /** �ı�baseline��Y���� */
    private float textBaselineY;

    private String text;

    private Paint.FontMetrics fm;

    private Context mContext;
    private boolean checked = false;

    public KeyboardButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public KeyboardButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public KeyboardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * ������ʼ�� 
     */
    private void init() {
        setOnClickListener(this);
        text = getText().toString();
        setText("");
        paint = new Paint();
        paint.setTextSize(22);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        //Ĭ����������־�����ʾ  
        textAlign = TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL;
        //Ĭ�ϵ��ı���ɫ�Ǻ�ɫ  
        textColor = Color.BLACK;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSize, hSize);
        Log.i(TAG, "onMeasure()--wMode=" + wMode + ",wSize=" + wSize + ",hMode=" + hMode + ",hSize=" + hSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        Log.i(TAG, "onLayout");
        viewWidth = right - left;
        viewHeight = bottom - top;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //���ƿؼ�����  
        setTextLocation(text);
        canvas.drawText(text, textCenterX, textBaselineY, paint);
    }

    /**
     * ��λ�ı����Ƶ�λ�� 
     */
    private void setTextLocation(String text) {
//        paint.setTextSize(textSize);  
        paint.setColor(textColor);
        fm = paint.getFontMetrics();
        //�ı��Ŀ��  
        float textWidth = paint.measureText(text);
        float textCenterVerticalBaselineY = viewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        switch (textAlign) {
            case TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = (float)viewWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_LEFT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = textWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_RIGHT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = viewWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = viewWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_LEFT:
                textCenterX = textWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_LEFT:
                textCenterX = textWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_RIGHT:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_RIGHT:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
        }
    }

    public interface OnClickListener {
        void onClick(View v, boolean checked);
    }
    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        checked = !checked;
        setBackgroundResource(checked ? 0 : R.drawable.dial_keyboard_button_3_pressed);
        if (mListener != null) {
            mListener.onClick(v, checked);
        }
    }

    public String getTextString() {
        return text;
    }

}  
