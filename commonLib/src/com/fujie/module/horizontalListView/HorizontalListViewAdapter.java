package com.fujie.module.horizontalListView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fujie.common.SystemMethod;
import com.fujie.module.tab.SearchMainAdapter;
import com.fujie.module.tab.SearchMoreAdapter;
import com.fujie.module.tab.SpinnerPopWindow;
import com.fujie.module.titlebar.R;

import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter {

    private List<ViewBean> viewBeanList;
    private LayoutInflater mInflater;
    private ViewHolder vh = new ViewHolder();
    private Context context;
    private boolean menu_is_show = false;

    public HorizontalListViewAdapter(Context con, List<ViewBean> viewBeanList) {
        mInflater = LayoutInflater.from(con);
        this.viewBeanList = viewBeanList;
        this.context = con;
    }

    @Override
    public int getCount() {
        return viewBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
            vh.tab_title = (TextView) convertView.findViewById(R.id.tab_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tab_title.setText(viewBeanList.get(position).getText());
        //得到全屏宽度计算单个item的宽度
        int width = SystemMethod.getWidth(context);
        vh.tab_title.setWidth(width / viewBeanList.size());
        //tab 点击事件
        vh.position = position;
        vh.tab_title.setOnClickListener(new TabClick(position));
        return convertView;
    }

    public void setViewBeanList(List<ViewBean> viewBeanList) {
        this.viewBeanList = viewBeanList;
    }

    private static class ViewHolder {
        private TextView tab_title;
        public ListView level_two_list;
        public ListView level_one_list;
        public LinearLayout districtLayout;
        public int position;
    }


    /**
     * Tab点击事件
     *  TODO: 弹出菜单项点击事件加不上。。。。待解决。
     */
    private class TabClick implements View.OnClickListener {
        private int position;
        private PopupWindow popupWindow;

        private TabClick() {
        }

        private TabClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            TextView textView=null;
            if (view instanceof TextView) {
                textView=((TextView)view);
            }
            Drawable drawable = null;
            if (!menu_is_show) {
                drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_up_black);
                setPopWindow(popupWindow,view, position);
                if (bizAreaAdapter != null) {
                    bizAreaAdapter.notifyDataSetChanged();
                }
                menu_is_show = true;
            } else {
                drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_down_black);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                menu_is_show = false;
            }
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            if (textView != null) {
                textView.setCompoundDrawables(null, null,
                        drawable, null);
            }
        }
    }

    public void setPopWindow(PopupWindow popupWindow, final View titleBaarView, int position) {
        String []strCounty =context.getResources().getStringArray(R.array.county_item);
        final SpinnerPopWindow  puCountyWindow= new SpinnerPopWindow(context,viewBeanList.get(position));
        //puCountyWindow.setSpinnerAdapter(strCounty);
        puCountyWindow.setItemSelectListener(new SpinnerPopWindow.IOnItemSelectListener() {
            @Override
            public void onItemClick(String tag,ViewBean viewBean,int position) {
                //恢复
                Drawable drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_down_black);
                menu_is_show = false;
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                if (titleBaarView instanceof TextView) {
                    ((TextView)titleBaarView).setText(viewBean.getText());
                    ((TextView)titleBaarView).setCompoundDrawables(null, null,
                            drawable, null);
                }
            }
        });
        puCountyWindow.getContentView().setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                puCountyWindow.setFocusable(false);
                puCountyWindow.dismiss();
                //恢复
                Drawable drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_down_black);
                if (puCountyWindow != null) {
                    puCountyWindow.dismiss();
                }
                menu_is_show = false;
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                if (titleBaarView instanceof TextView) {
                    ((TextView)titleBaarView).setCompoundDrawables(null, null,
                            drawable, null);
                }
                return true;
            }
        });
        puCountyWindow.setWidth(SystemMethod.getWidth(context));
        puCountyWindow.showAsDropDown(titleBaarView,""+position);
        puCountyWindow.initData(viewBeanList.get(position));
    }

    /**
     * 初始化弹出菜单
     *
     * @param position
     */
    private void initPopupWindow(int position) {

        //  }
    }

    private SearchMoreAdapter bizAreaAdapter = null;
    private SearchMainAdapter districtAdapter = null;

    private void initDistrictAdapter(List<ViewBean> viewBeans) {
        if (bizAreaAdapter == null) {
            bizAreaAdapter = new SearchMoreAdapter(context, viewBeans, R.layout.shop_list2_item);
            vh.level_two_list.setAdapter(bizAreaAdapter);
        }
        bizAreaAdapter.setViewBeanList(viewBeans);
        bizAreaAdapter.notifyDataSetChanged();
    }
}