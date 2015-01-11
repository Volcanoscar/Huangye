package com.fujie.module.tab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fujie.module.horizontalListView.ViewBean;
import com.fujie.module.titlebar.R;

import java.util.List;

/**
 * 查找中的更多的界面中左边listview的适配器
 */

public class SearchMainAdapter extends BaseAdapter {

    private Context ctx;
    private List<ViewBean> viewBeanList;
    private int position = 0;
    private boolean islodingimg = true;
    private int layout = R.layout.search_more_mainlist_item;

    public List<ViewBean> getViewBeanList() {
        return viewBeanList;
    }

    public void setViewBeanList(List<ViewBean> viewBeanList) {
        this.viewBeanList = viewBeanList;
    }

    public SearchMainAdapter(Context ctx, List<ViewBean> list) {
        this.ctx = ctx;
        this.viewBeanList = list;
    }

    public SearchMainAdapter(Context ctx, List<ViewBean> list,
                             int layout, boolean islodingimg) {
        this.ctx = ctx;
        this.viewBeanList = list;
        this.layout = layout;
        this.islodingimg = islodingimg;
    }

    public int getCount() {
        return viewBeanList.size();
    }

    public Object getItem(int arg0) {
        return viewBeanList.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        Holder hold;
        if (arg1 == null) {
            hold = new Holder();
            arg1 = View.inflate(ctx, layout, null);
            hold.txt = (TextView) arg1
                    .findViewById(R.id.Search_more_mainitem_txt);
            hold.img = (ImageView) arg1
                    .findViewById(R.id.Search_more_mainitem_img);
            hold.layout = (LinearLayout) arg1
                    .findViewById(R.id.Search_more_mainitem_layout);
            arg1.setTag(hold);
        } else {
            hold = (Holder) arg1.getTag();
        }
        if(islodingimg == true){
            hold.img.setImageResource(Integer.parseInt(viewBeanList.get(arg0).getImg()
                    .toString()));
        }
        hold.txt.setText(viewBeanList.get(arg0).getText());
        hold.layout
                .setBackgroundResource(R.drawable.search_more_mainlistselect);
        if (arg0 == position) {
            hold.layout.setBackgroundResource(R.drawable.list_bkg_line_u);
        }
        return arg1;
    }

    public void setSelectItem(int i) {
        position = i;
    }

    public int getSelectItem() {
        return position;
    }

    private static class Holder {
        LinearLayout layout;
        ImageView img;
        TextView txt;
    }
}
