package com.nuo.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nuo.bean.MsgInfo;
import com.nuo.activity.R;
import com.nuo.model.Model;
import com.nuo.utils.LoadImg;
import com.nuo.utils.LoadImg.ImageDownloadCallBack;
import com.nuo.utils.PreferenceConstants;

/**
 * 商铺列表的适配器
 * @author 苦涩
 *
 */

public class ShopAdapter extends BaseAdapter {

    private List<MsgInfo> list;
    private Context ctx;
    private LoadImg loadImg;

    public ShopAdapter(List<MsgInfo> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
        // 实例化获取图片的类
        loadImg = new LoadImg(ctx);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        final Holder hold;
        if (arg1 == null) {
            hold = new Holder();
            arg1 = View.inflate(ctx, R.layout.item_shop, null);
            hold.mTitle = (TextView) arg1.findViewById(R.id.ShopItemTextView);
            hold.mImage = (ImageView) arg1.findViewById(R.id.ShopItemImage);
          /*  hold.mMoney = (TextView) arg1.findViewById(R.id.ShopItemMoney);*/
            hold.mAddress = (TextView) arg1.findViewById(R.id.ShopItemAddress);
            arg1.setTag(hold);
        } else {
            hold = (Holder) arg1.getTag();
        }
        hold.mTitle.setText(list.get(arg0).getTitle());
        hold.mImage.setTag(PreferenceConstants.DEFAULT_HTTP_SERVER_HOST+ list.get(arg0).getPhoto());
        hold.mAddress.setText(list.get(arg0).getContent());

        // 设置默认显示的图片
        hold.mImage.setImageResource(R.drawable.shop_photo_frame);
        // 网络获取图片
        if (list.get(arg0).getPhoto() != null) {
            BitmapUtils bitmapUtils = new BitmapUtils(ctx);
            bitmapUtils.display(hold.mImage,PreferenceConstants.DEFAULT_HTTP_SERVER_HOST+ list.get(arg0).getPhoto());
        }
        return arg1;
    }

    static class Holder {
        TextView mTitle, mMoney, mAddress, mStytle;
        ImageView mImage, mStar, mTuan, mQuan, mDing, mCard;
    }

}
