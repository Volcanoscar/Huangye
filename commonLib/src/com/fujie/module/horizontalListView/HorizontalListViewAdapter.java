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
        private ListView oneView;

        private TabClick() {
        }

        private TabClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Drawable drawable = null;
            if (!menu_is_show) {
                drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_up_black);
                setPopWindow(oneView,popupWindow,view, position);
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
            vh.tab_title.setCompoundDrawables(null, null,
                    drawable, null);
        }
    }

    public void setPopWindow(ListView level_one_list, PopupWindow popupWindow, View titleBaarView, int position) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View tempPopup = layoutInflater.inflate(R.layout.level_two_menu, null);

            //根据数据判断 是否有二级菜单（目前有三级。第一级是tab,第二级是一级菜单，第三级是三级菜单。）
            ViewBean tabViewBean = viewBeanList.get(position);
            final List<ViewBean> levelOneList = tabViewBean.getBizAreaList();
            final ListView level_two_list = (ListView) tempPopup.findViewById(R.id.level_two_list);
            level_one_list = (ListView) tempPopup.findViewById(R.id.level_one_list);
            LinearLayout districtLayout = (LinearLayout) tempPopup.findViewById(R.id.districtLayout);
            final SearchMainAdapter districtAdapter = new SearchMainAdapter(context, viewBeanList.get(position).getBizAreaList(), R.layout.shop_list1_item, false);
            level_one_list.setAdapter(districtAdapter);
            districtAdapter.setSelectItem(0);

            popupWindow = new PopupWindow(tempPopup, 300,300);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
            popupWindow.showAsDropDown(titleBaarView, 0, -15);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            //做一个不在焦点外的处理事件监听
            final PopupWindow finalPopupWindow2 = popupWindow;
           /* popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    finalPopupWindow2.setFocusable(false);
                    finalPopupWindow2.dismiss();
                    return true;
                }
            });*/

                if (tabViewBean.isParent()) { //如果有二级菜单添加事件
                final PopupWindow finalPopupWindow = popupWindow;
                level_one_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      /*  initDistrictAdapter(levelOneList.get(i).getBizAreaList());
                        districtAdapter.setSelectItem(i);
                        districtAdapter.notifyDataSetChanged();*/
                        Toast.makeText(context, "项事件", Toast.LENGTH_SHORT);
                    }
                });
                vh.level_two_list = level_two_list;
                level_two_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //取消弹出菜单并修改tab名称
                      /*  vh.tab_title.setText(bizAreaAdapter.getViewBeanList().get(i).getText());
                        finalPopupWindow.dismiss();*/
                        Toast.makeText(context, "项事件", Toast.LENGTH_SHORT);
                    }
                });
                initDistrictAdapter(levelOneList.get(position).getBizAreaList());
            } else {//如果是一级菜单点击事件

                final PopupWindow finalPopupWindow1 = popupWindow;
                level_one_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //取消弹出菜单并修改tab名称
                       /* vh.tab_title.setText(levelOneList.get(i).getText());
                        finalPopupWindow1.dismiss();*/
                        Toast.makeText(context, "项事件", Toast.LENGTH_SHORT);
                    }
                });
                level_two_list.setVisibility(View.GONE);
            }
            popupWindow.update();
        }else{
            popupWindow.showAsDropDown(titleBaarView, 0, -15);
        }
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