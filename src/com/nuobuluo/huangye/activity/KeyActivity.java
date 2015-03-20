package com.nuobuluo.huangye.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.myview.keyboard.KeyboardButton;
import com.nuobuluo.huangye.myview.keyboard.SpecailView;

/**
 * Created by Administrator on 15-1-21.
 */
public class KeyActivity extends Activity implements KeyboardButton.OnClickListener{
    private SpecailView layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keybord_layout);

        layout = (SpecailView)findViewById(R.id.specail_view);
        layout.setOnItemClick(this);
    }

    @Override
    public void onClick(View v, boolean checked) {
        String text = ((KeyboardButton)v).getTextString();
        Toast.makeText(this, text + checked, Toast.LENGTH_SHORT).show();
    }
}
