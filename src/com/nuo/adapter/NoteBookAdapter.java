package com.nuo.adapter;

import java.util.List;

import com.fujie.module.gridview.NoScrollGridView;
import com.fujie.module.horizontalListView.ViewBean;
import com.nuo.activity.ImagePagerActivity;
import com.nuo.activity.PreviewNoteBookActivity;
import com.nuo.activity.R;
import com.nuo.bean.notebook.NoteBook;
import com.nuo.utils.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoteBookAdapter extends BaseAdapter{

	private List<NoteBook> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public NoteBookAdapter(Context context,List<NoteBook> list) {
		mInflater = LayoutInflater.from(context);
		mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public NoteBook getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_notebook, null);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.gridView=(NoScrollGridView)convertView.findViewById(R.id.gridView);
			holder.notebook_layout=(RelativeLayout)convertView.findViewById(R.id.notebook_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final NoteBook bean = getItem(position);
		
		/*ImageLoader.getInstance().displayImage(bean.avator, holder.avator);*/
		holder.name.setText(bean.getTitle());
		holder.content.setText(bean.getContent());
		String dateStr = DateUtil.format(bean.getCreate_time());
		if (dateStr==null) {
            holder.time.setText(DateUtil.dateToStr(bean.getCreate_time(), "MM.dd")); //创建时间还修改时间？
        }else{
            holder.time.setText(dateStr);
        }
		if(bean.urls!=null&&bean.urls.length>0){
			holder.gridView.setVisibility(View.VISIBLE);
			holder.gridView.setAdapter(new GridAdapter(bean.urls, mContext));
			holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					imageBrower(position,bean.urls);
				}
			});
		}else{
			holder.gridView.setVisibility(View.GONE);
		}
        holder.notebook_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PreviewNoteBookActivity.class);
                intent.putExtra("id", bean.getId());
                mContext.startActivity(intent);
            }
        });
		return convertView;
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
	private static class ViewHolder {

		public TextView name;
		public ImageView avator;
		TextView content;
		NoScrollGridView gridView;
        public TextView time;
        public RelativeLayout notebook_layout;
	}
}
