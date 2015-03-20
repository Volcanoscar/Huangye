package com.nuobuluo.module.horizontalListView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nuobuluo.huangye.common.SystemMethod;
import com.nuobuluo.module.tab.FilterTabClickListener;
import com.nuobuluo.module.tab.SearchMoreAdapter;
import com.nuobuluo.module.tab.SpinnerPopWindow;
import com.nuobuluo.module.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorizontalListViewAdapter extends BaseAdapter {

    private List<ViewBean> viewBeanList;
    private LayoutInflater mInflater;
    private ViewHolder vh = new ViewHolder();
    private Context context;
    private boolean menu_is_show = false;
    private FilterTabClickListener tabClickListener;

    public HorizontalListViewAdapter(Context con, FilterTabClickListener listener,List<ViewBean> viewBeanList) {
        mInflater = LayoutInflater.from(con);
        this.viewBeanList = viewBeanList;
        this.tabClickListener = listener;
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

    public void setFilterTabClickListener(FilterTabClickListener filterTabClickListener) {
        this.tabClickListener = filterTabClickListener;
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
                setPopWindow(view, position);
                if (bizAreaAdapter != null) {
                    bizAreaAdapter.notifyDataSetChanged();
                }
                menu_is_show = true;
            } else {
                drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_down_black);
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

    private Map<String,ViewBean> selectedTab = new HashMap<String,ViewBean>();

    /**
     *  设置弹出框 TODO: 待优化，现在每一次都会创建一个弹出框对象
     * @param titleBarView
     * @param position
     */
    public void setPopWindow(final View titleBarView, int position) {
        final SpinnerPopWindow  puCountyWindow= new SpinnerPopWindow(context,viewBeanList.get(position));
        puCountyWindow.setItemSelectListener(new SpinnerPopWindow.IOnItemSelectListener() {
            @Override
            public void onItemClick(String tag,ViewBean viewBean,int position) {
                //恢复
                if (titleBarView instanceof TextView) {
                    ((TextView)titleBarView).setText(viewBean.getText());
                }
                selectedTab.put(tag, viewBean); //有的覆盖，没有的添加
                if (tabClickListener != null&& !selectedTab.isEmpty()) {
                    List<ViewBean> viewBeanList = new ArrayList<ViewBean>();
                    for (Map.Entry<String, ViewBean> temp : selectedTab.entrySet()) {
                        viewBeanList.add(temp.getValue());
                    }
                    tabClickListener.onClick(viewBeanList);
                }
            }
        });
        puCountyWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复
                Drawable drawable = context.getResources().getDrawable(
                        R.drawable.ic_arrow_down_black);
                menu_is_show = false;
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                if (titleBarView instanceof TextView) {
                    ((TextView)titleBarView).setCompoundDrawables(null, null,
                            drawable, null);
                }
            }
        });

        puCountyWindow.setWidth(SystemMethod.getWidth(context));
        puCountyWindow.showAsDropDown(titleBarView,""+position);
        puCountyWindow.initData(viewBeanList.get(position));
    }

    private SearchMoreAdapter bizAreaAdapter = null;
}