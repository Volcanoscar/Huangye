
package com.nuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nuo.activity.R;
import com.nuo.bean.notebook.NoteBookLabel;
import com.nuo.utils.T;

import java.util.List;

public class TagListViewAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<NoteBookLabel> data;
    private String key;

    private CheckListener listener;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     */
    public TagListViewAdapter(Context ctx, List<NoteBookLabel> data) {
        mContext = ctx;
        this.data = data;
    }

    public void setData(List<NoteBookLabel> data) {
        this.data = data;
    }
    public CheckListener getListener() {
        return listener;
    }

    public void setListener(CheckListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_no_tag, null);
            holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.itemLayout);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.checkUser = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getName());
        holder.checkUser.setChecked(data.get(position).isHasTag());
        final ViewHolder finalHolder = holder;
        holder.checkUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.get(position).setHasTag(b);
                listener.onCheckedChanged(data.get(position));
            }
        });
        return convertView;
    }


    public final class ViewHolder {
        public RelativeLayout itemLayout;
        public TextView name;
        public CheckBox checkUser;
    }
    public static interface CheckListener {
        void onCheckedChanged(NoteBookLabel noteBookLabel);
    }
}


