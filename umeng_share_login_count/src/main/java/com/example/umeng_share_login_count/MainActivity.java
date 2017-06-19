package com.example.umeng_share_login_count;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.umeng_share_login_count.count.UMCountActivity;
import com.example.umeng_share_login_count.login.UMLoginActivity;
import com.example.umeng_share_login_count.share.UMShareActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_share,btn_login,btn_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        btn_share=(Button)findViewById(R.id.btn_share);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_count=(Button)findViewById(R.id.btn_count);
    }

    private void initData(){
        btn_share.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_count.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share://友盟分享
                startActivity(new Intent(MainActivity.this,UMShareActivity.class));
                break;
            case R.id.btn_login://友盟登录
                startActivity(new Intent(MainActivity.this,UMLoginActivity.class));
                break;
            case R.id.btn_count://友盟统计
                startActivity(new Intent(MainActivity.this,UMCountActivity.class));
                break;
        }

    }
}
