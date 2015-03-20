package com.nuobuluo.huangye.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nuobuluo.huangye.R;
import com.nuobuluo.huangye.utils.T;

public class UserDetailActivity extends Activity{
    private TextView accountTextView;
    private Button addFriendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ActionBar bar = getActionBar();
        bar.setTitle(R.string.action_usercenter);
        bar.setDisplayHomeAsUpEnabled(true);
        findView();
        init();
    }

    private void findView() {
        accountTextView = (TextView) findViewById(R.id.account);
        addFriendBtn = (Button) findViewById(R.id.add_friend);
    }

    private void init() {
        //final User user = (User) getIntent().getSerializableExtra("user");
        /*if (user != null) {
            accountTextView.setText(user.getUserId());
        }*/
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showLong(UserDetailActivity.this, R.string.add_friend_sucess);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
