package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.utils.PreferenceConstants;

import java.util.ArrayList;
import java.util.List;

public class SmallImageListViewAdapter extends BaseAdapter {
    private List<String> imgAddList = new ArrayList<String>();
    private Context context;
    BitmapUtils bitmapUtils ;
    public SmallImageListViewAdapter(Context con,List<String> imgAddList) {
        this.context = con;
        bitmapUtils = new BitmapUtils(context);
        mInflater = LayoutInflater.from(con);
        for (String temp : imgAddList) {
            this.imgAddList.add(PreferenceConstants.DEFAULT_HTTP_SERVER_HOST + temp);
        }
    }
    public List<String> getImgAddList() {
        return imgAddList;
    }

    public void setImgAddList(List<String> imgAddList) {
        this.imgAddList.clear();
        for (String temp : imgAddList) {
            this.imgAddList.add(PreferenceConstants.DEFAULT_HTTP_SERVER_HOST + temp);
        }
    }

    public void addLocalImgAddList(String img) {
        this.imgAddList.add(img);
    }

    @Override
    public int getCount() {
        return imgAddList.size();
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        return imgAddList.get(position);
    }

    private ViewHolder vh = new ViewHolder();

    private static class ViewHolder {
        private ImageView im;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.small_image_item, null);
            vh.im = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        // 加载网络图片
        bitmapUtils.display(vh.im, imgAddList.get(position));
        /*vh.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] urls = new String[imgAddList.size()];
                urls = imgAddList.toArray(urls);
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });*/
        return convertView;
    }
}