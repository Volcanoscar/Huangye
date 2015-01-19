package com.nuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nuo.activity.R;
import com.nuo.utils.PinyinUtils;
import com.nuo.utils.Utils;

public class ContactsBaseAdapter extends BaseAdapter{
    private Context context;

    public ContactsBaseAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Utils.mPersons.size();
    }

    @Override
    public Object getItem(int position) {
        return Utils.mPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.contacts_list_item_new, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.contacts_name);
        name.setText(Utils.mPersons.get(position).mName);

        /*TextView number = (TextView) convertView.findViewById(R.id.contacts_number);
        number.setText(Utils.mPersons.get(position).mNum);*/

        ImageView chooseView = (ImageView)convertView.findViewById(R.id.choose_contact);
        chooseView.setVisibility(View.GONE);

        //字母提示textview的显示
        TextView letterTag = (TextView)convertView.findViewById(R.id.pb_item_LetterTag);
        //获得当前姓名的拼音首字母
        String firstLetter = PinyinUtils.getPingYin(Utils.mPersons.get(position).mName).substring(0,1).toUpperCase();

        //如果是第1个联系人 那么letterTag始终要显示
        if(position == 0)
        {
            letterTag.setVisibility(View.VISIBLE);
            letterTag.setText(firstLetter);
        }
        else
        {
            //获得上一个姓名的拼音首字母
            String firstLetterPre = PinyinUtils.getPingYin(Utils.mPersons.get(position-1).mName).substring(0,1).toUpperCase();
            //比较一下两者是否相同
            if(firstLetter.equals(firstLetterPre))
            {
                letterTag.setVisibility(View.GONE);
            }
            else
            {
                letterTag.setVisibility(View.VISIBLE);
                letterTag.setText(firstLetter);
            }
        }

        return convertView;
    }
}
