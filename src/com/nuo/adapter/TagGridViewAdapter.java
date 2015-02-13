
package com.nuo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nuo.activity.HuangyeListActivity;
import com.nuo.activity.R;
import com.nuo.bean.KeySearchResult;
import com.nuo.bean.notebook.NoteBookLabel;

import java.util.List;

public class TagGridViewAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<NoteBookLabel> data;
    private String key;
    private TagListViewAdapter.CheckListener checkListener;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<NoteBookLabel> getData() {
        return data;
    }

    public void setData(List<NoteBookLabel> data) {
        this.data = data;
    }

    public TagListViewAdapter.CheckListener getCheckListener() {
        return checkListener;
    }

    public void setCheckListener(TagListViewAdapter.CheckListener checkListener) {
        this.checkListener = checkListener;
    }

    /**
     */
    public TagGridViewAdapter(Context ctx, List<NoteBookLabel> data) {
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
    	
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_has_tag, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView)convertView.findViewById(R.id.tagtext);
            holder.delete = (ImageView)convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }
        final NoteBookLabel msg = data.get(position);
        holder.tv_title.setText(msg.getName());
        holder.delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.onCheckedChanged(data.get(position));
            }
        });
        return convertView;
    }
    static class ViewHolder {
        TextView tv_title;
        public ImageView delete;
    }
}
