
package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nuobuluo.huangye.activity.HuangyeListActivity;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.bean.KeySearchResult;

import java.util.List;

public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<KeySearchResult> data;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     */
    public SwipeAdapter(Context ctx, List<KeySearchResult> data) {
        mContext = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sms, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tv_count = (TextView)convertView.findViewById(R.id.tv_count);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }
        final KeySearchResult msg = data.get(position);
        final String typeCode = msg.getTypeCode();
        holder.tv_title.setText(msg.getTypeName());
        holder.tv_count.setText(msg.getCount().toString());
        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HuangyeListActivity.class);
                intent.putExtra("typeCode",typeCode);
                intent.putExtra("key",key);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    public void setDate(List<KeySearchResult> searchResults) {
        this.data = searchResults;
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_count;
        RelativeLayout item_right;
    }
}
