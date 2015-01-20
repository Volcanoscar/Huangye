package com.nuo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nuo.activity.ImagePagerActivity;
import com.nuo.activity.R;

public class SmallImageListViewAdapter extends BaseAdapter {
    private Context context;

    public SmallImageListViewAdapter(Context con) {
        this.context = con;
        mInflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return 5;
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        return position;
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
        vh.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                String[]urls = new String[]{
                        "http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg",
                        "http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg",
                        "http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg",
                        "http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg",
                        "http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg"
                };
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}