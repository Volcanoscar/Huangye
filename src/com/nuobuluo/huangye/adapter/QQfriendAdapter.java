package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.nuobuluo.module.listview.CustomListView;
import com.nuobuluo.module.sort.SortModel;
import com.nuobuluo.module.util.ImgUtil;
import com.nuobuluo.huangye.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QQfriendAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context mContext;
	private Map<String, String> mCallRecords;
	private CustomListView mCustomListView;
	private HashMap<String, SoftReference<Bitmap>> hashMaps = new HashMap<String, SoftReference<Bitmap>>();
	
	public QQfriendAdapter(Context mContext, List<SortModel> list, Map<String, String> callRecods, CustomListView customListView) {
		this.mContext = mContext;
		this.list = list;
		this.mCallRecords=callRecods;
		this.mCustomListView=customListView;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
        final boolean[] is_show = {false};
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.fragment_phone_constacts_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.iconView=(ImageView) view.findViewById(R.id.icon);
			viewHolder.more=(TextView) view.findViewById(R.id.more);
			viewHolder.bottomlayout=(LinearLayout) view.findViewById(R.id.bottomlayout);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_show[0]) {
                    finalViewHolder.bottomlayout.setVisibility(View.GONE);
                    is_show[0] =false;
                }else{
                    finalViewHolder.bottomlayout.setVisibility(View.VISIBLE);
                    is_show[0] =true;
                }
            }
        });
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		viewHolder.tvLetter.setVisibility(View.GONE);
	
		String path = mCallRecords.get(list.get(position).getName());
			if (hashMaps.containsKey(path)) {
				viewHolder.iconView.setImageBitmap(hashMaps.get(path).get());
				//另一个地方缓存释放资源
				ImgUtil.getInstance().reomoveCache(path);
			} else {
				viewHolder.iconView.setTag(path);
				/*ImgUtil.getInstance().loadBitmap(path,
						new ImgUtil.OnLoadBitmapListener() {
							@Override
							public void loadImage(Bitmap bitmap, String path) {
								ImageView iv = (ImageView) mCustomListView
										.findViewWithTag(path);
								if (bitmap != null && iv != null) {
									bitmap = SystemMethod.toRoundCorner(bitmap, 15);
									iv.setImageBitmap(bitmap);

									if (!hashMaps.containsKey(path)) {
										hashMaps.put(path,
												new SoftReference<Bitmap>(bitmap));
									}
								}
							}
						});*/
			}
			
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView iconView;
        public TextView more;
        public LinearLayout bottomlayout;
    }


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}