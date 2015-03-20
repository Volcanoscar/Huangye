package com.nuobuluo.huangye.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.cursor.ContactsCursor;
import com.nuobuluo.huangye.utils.Utils;

import java.util.ArrayList;

/**
* Created by zxl on 14-8-12.
*/
public class FilterAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ContactsCursor.SortEntry> data;
    private Context context;
    private String keyword = "";

    public FilterAdapter(Context context,
                         ArrayList<ContactsCursor.SortEntry> data, String keyword) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.keyword=keyword;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        arg1 = mInflater.inflate(R.layout.contacts_list_item, null);

        //姓名显示
        TextView nameCtrl = (TextView)arg1.findViewById(R.id.contacts_name);
        String strName = data.get(arg0).mName;
        nameCtrl.setText(strName);

        //电话号码显示
        TextView numberCtrl = (TextView)arg1.findViewById(R.id.contacts_number);
        String lastKeyword= "";
        for(String key: Utils.mPersons.get(arg0).getmNums()) {
            if (key.indexOf(keyword) != -1) {
                if (lastKeyword.equals("")) {
                    lastKeyword += key;
                }else{
                    lastKeyword ="\n"+ key;
                }
            }
        }
        numberCtrl.setText(Html.fromHtml(lastKeyword.replace(keyword,"<font color=\"#0000cc\">"+keyword+"</font>")));

        ImageView chooseView = (ImageView)arg1.findViewById(R.id.choose_contact);
        chooseView.setVisibility(View.GONE);

        return arg1;
    }
}
