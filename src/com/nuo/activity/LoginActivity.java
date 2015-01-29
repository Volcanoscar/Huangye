package com.nuo.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nuo.bean.UserInfo;
import com.nuo.db.DBUtil;
import com.nuo.handler.TimeOutHandler;
import com.nuo.utils.*;

import java.util.Map;

/**
 * 登录模块
 * */

public class LoginActivity extends Activity {

    private ImageView mLogin_back;
    private EditText mLogin_user, mLogin_password;
    private TextView mLogin_OK, mLogin_wangjimima, mLogin_zhuce;
    private TimeOutHandler timeOutHandler= new TimeOutHandler(this);
    private String toView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initData();
        initView();
    }

    private void initData() {
       toView= getIntent().getStringExtra("toView");
    }

    private void initView() {
        mLogin_back = (ImageView) findViewById(R.id.Login_back);
        mLogin_user = (EditText) findViewById(R.id.Login_user);
        mLogin_password = (EditText) findViewById(R.id.Login_password);
        mLogin_OK = (TextView) findViewById(R.id.Login_OK);
        mLogin_wangjimima = (TextView) findViewById(R.id.Login_wangjimima);
        mLogin_zhuce = (TextView) findViewById(R.id.Login_zhuce);
        MyOnClickLietener myonclick = new MyOnClickLietener();
        mLogin_back.setOnClickListener(myonclick);
        mLogin_OK.setOnClickListener(myonclick);
        mLogin_wangjimima.setOnClickListener(myonclick);
        mLogin_zhuce.setOnClickListener(myonclick);
    }

    private class MyOnClickLietener implements View.OnClickListener {
        public void onClick(View arg0) {
            int mID = arg0.getId();
            if (mID == R.id.Login_back) {
                LoginActivity.this.finish();
            }
            if (mID == R.id.Login_OK) {
                //检查是否有此电话号码
                NetUtil.checkPhone(mLogin_user.getText().toString(),new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        //testTextView.setText(current + "/" + total);
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if ("false".equals(responseInfo.result)) { //如果是false则说明有此电话号码，可以登录
                            // 登录
                            login();
                        }else{
                            T.showShort(LoginActivity.this,R.string.invalid_phone);
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        T.showShort(LoginActivity.this,R.string.net_error);
                    }
                });

            }
            if (mID == R.id.Login_wangjimima) {
                Toast.makeText(LoginActivity.this, "忘记密码被单击", 1).show();
            }
            if (mID == R.id.Login_zhuce) {
                Intent intent = new Intent(LoginActivity.this,
                        RegistrationActivity.class);
                startActivity(intent);
            }
        }
    }

    private void login() {
        //检查是否有此电话号码
        NetUtil.login(mLogin_user.getText().toString(), mLogin_password.getText().toString(),
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        timeOutHandler.stop();
                        //解析json
                        UserInfo userInfo= UserInfo.parseMap(responseInfo.result);
                        if (userInfo == null) {
                            T.showShort(LoginActivity.this,R.string.login_error);
                        }else{
                            //保存用户名、密码信息(到数据库中)
                            T.showShort(LoginActivity.this,"登录成功");
                            DbUtils dbUtil= XutilHelper.getDB(LoginActivity.this);
                            try {
                                dbUtil.deleteAll(UserInfo.class);//选删除用户信息
                                dbUtil.saveBindingId(userInfo); // 保存用户信息
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            //跳转到个人中心
                            finish();
                            Intent intent = toView();
                            startActivity(intent);
                            save2Preferences(userInfo.getId());
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        timeOutHandler.stop();
                        T.showShort(LoginActivity.this, R.string.net_error);
                    }
                    @Override
                    public void onStart() {
                        timeOutHandler.start(null);
                    }
                });
    }

    private Intent toView() {
        if ("shop".equals(toView)) {
            return new Intent(LoginActivity.this, ShopDetailActivity.class);
        }else{
            return new Intent(LoginActivity.this, MyActivity.class);
        }
    }

    private void save2Preferences(Integer id) {
        PreferenceUtils.setPrefInt(this, PreferenceConstants.ACCOUNT_ID,
                id);
        PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
                mLogin_user.getText().toString());
        PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
                mLogin_password.getText().toString());
        PreferenceUtils.setPrefString(this,
                PreferenceConstants.STATUS_MODE,
                PreferenceConstants.AVAILABLE);//标记为有效
    }
}
