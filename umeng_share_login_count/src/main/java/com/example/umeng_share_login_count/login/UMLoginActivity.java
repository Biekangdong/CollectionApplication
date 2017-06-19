package com.example.umeng_share_login_count.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.umeng_share_login_count.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/6/17.
 */

public class UMLoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_um_qq_login,btn_um_wx_login,btn_um_sina_login;
    //登录平台
    private SHARE_MEDIA[] medias = {SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA};
    private SHARE_MEDIA getSHARE_MEDIA;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umlogin);
        initView();
        initData();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
    public void initView(){
        btn_um_qq_login=(Button)findViewById(R.id.btn_um_qq_login);
        btn_um_wx_login=(Button)findViewById(R.id.btn_um_wx_login);
        btn_um_sina_login=(Button)findViewById(R.id.btn_um_sina_login);
        dialog = new ProgressDialog(this);
    }
    public void initData(){
        btn_um_qq_login.setOnClickListener(this);
        btn_um_wx_login.setOnClickListener(this);
        btn_um_sina_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.btn_um_qq_login:
              auth(medias[0]);
              getSHARE_MEDIA=medias[0];
              break;
          case R.id.btn_um_wx_login:
              auth(medias[1]);
              getSHARE_MEDIA=medias[1];
              break;
          case R.id.btn_um_sina_login:
              auth(medias[2]);
              getSHARE_MEDIA=medias[2];
              break;
      }
    }


    //三方登录方法
    public void auth(SHARE_MEDIA mPlatform){
        UMShareAPI.get(getApplicationContext()).doOauthVerify(this, mPlatform, authListener);
    }
    //三方登录回调监听
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(UMLoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
            UMShareAPI.get(getApplicationContext()).getPlatformInfo(UMLoginActivity.this, getSHARE_MEDIA, authInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(UMLoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(UMLoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //获取用户信息回调监听
    UMAuthListener authInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            String temp = "";
//            for (String key : data.keySet()) {
//                temp = temp + key + " : " + data.get(key) + "\n";
//            }
//            result.setText(temp);

            String uid=data.get("uid")+"";//用户唯一标识
            String screen_name=data.get("screen_name")+"";//用户昵称
            String gender=data.get("gender")+"";//用户性别
            String iconurl=data.get("iconurl")+"";//用户头像
            Intent intent=new Intent();
            intent.putExtra("uid",uid);
            intent.putExtra("screen_name",screen_name);
            intent.putExtra("gender",gender);
            intent.putExtra("iconurl",iconurl);
            setResult(200,intent);
            UMLoginActivity.this.finish();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(UMLoginActivity.this, "错误" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
