package com.nuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujie.common.SystemMethod;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.nuo.activity.R;
import com.nuo.bean.notebook.NoteBookRecord;
import com.nuo.utils.FileUtiles;
import com.nuo.utils.XutilHelper;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2014/11/5.
 */
public class RecordAdapter extends BaseAdapter{

    private final Context context;
    private LayoutInflater mInflater;
    private List<NoteBookRecord> recordList;
    private DbUtils dbUtils;


    public RecordAdapter(Context context, List<NoteBookRecord> stadiumList) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.recordList = stadiumList;
        dbUtils = XutilHelper.getDB(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return recordList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return recordList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record, null);
           /* holder.score = (TextView) convertView.findViewById(R.id.score);*/
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NoteBookRecord noteBookRecord = recordList.get(position);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(SystemMethod.dip2px(context,200), LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        holder.name.setText(noteBookRecord.getName());
        if (noteBookRecord.getCreateTime() != null) {
            holder.time.setText(String.valueOf(noteBookRecord.getLength()));
        }

        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(noteBookRecord.getPath());
                if (file.exists()) {
                    file.delete();
                }
                if (noteBookRecord.getId() != null) {
                    try {
                        dbUtils.deleteById(NoteBookRecord.class, noteBookRecord.getId());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                recordList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public TextView score;
        public TextView name;
        public TextView time;
        public RelativeLayout item_right;
        public RelativeLayout item_left;
    }
}
