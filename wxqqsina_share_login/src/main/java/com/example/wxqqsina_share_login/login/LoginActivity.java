package com.example.wxqqsina_share_login.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxqqsina_share_login.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_qq_login, btn_wx_login, btn_sina_login;
    TextView tvNickName;
    ImageView headerLogo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        btn_qq_login = (Button) findViewById(R.id.btn_qq_login);
        btn_wx_login = (Button) findViewById(R.id.btn_wx_login);
        btn_sina_login = (Button) findViewById(R.id.btn_sina_login);

        tvNickName = (TextView) findViewById(R.id.tvNickName);
        headerLogo= (ImageView) findViewById(R.id.headerLogo);
    }

    private void initData() {
        btn_qq_login.setOnClickListener(this);
        btn_wx_login.setOnClickListener(this);
        btn_sina_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qq_login://qq登录
                qqLogin();
                break;
            case R.id.btn_wx_login://微信登录
                wxLogin();
                break;
            case R.id.btn_sina_login://新浪微博登录
                sinaLogin();
                break;
        }
    }

    private void wxLogin() {
        //注册微信
        IWXAPI api = WXAPIFactory.createWXAPI(this, "你的应用在微信上申请的app_id", true);
        api.registerApp("你的应用在微信上申请的app_id");
        if (api != null && api.isWXAppInstalled()) {
            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";//应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
            req.state = "wechat_sdk_demo_test";//用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
            api.sendReq(req);
        } else {
            Toast.makeText(this, "用户未安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //需要腾讯提供的一个Tencent类
    private Tencent mTencent;
    //还需要一个IUiListener 的实现类（LogInListener implements IUiListener）
    private LogInListener mListener;

    private void qqLogin() {
        //首先需要用APP ID 获取到一个Tencent实例
        mTencent = Tencent.createInstance("QQ_APP_ID", this.getApplicationContext());
        //初始化一个IUiListener对象，在IUiListener接口的回调方法中获取到有关授权的某些信息
        // （千万别忘了覆写onActivityResult方法，否则接收不到回调）
        mListener = new LogInListener();
        //调用QQ登录，用IUiListener对象作参数（点击登录按钮时执行以下语句）
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", mListener);
        }

        //登出比较简单
        //mTencent.logout(LoginActivity.this);

    }

    private class LogInListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_LONG).show();

            JSONObject jsonObject = (JSONObject) o;

            //设置openid和token，否则获取不到下面的信息
            initOpenidAndToken(jsonObject);
            //获取QQ用户的各信息
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {

            Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
        }
    }
    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }



    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String openid = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");

            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);
        userInfo.getUserInfo(new IUiListener() {
                                 @Override
                                 public void onComplete(final Object o) {
                                     JSONObject userInfoJson = (JSONObject) o;
                                     Message msgNick = new Message();
                                     msgNick.what = 0;//昵称
                                     try {
                                         msgNick.obj = userInfoJson.getString("nickname");//直接传递一个昵称的内容过去
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }
                                     mHandler.sendMessage(msgNick);
                                     //子线程 获取并传递头像图片，由Handler更新
                                     new Thread(new Runnable() {
                                         @Override
                                         public void run() {
                                             Bitmap bitmapHead = null;
                                             if (((JSONObject) o).has("figureurl")) {
                                                 try {
                                                     String headUrl = ((JSONObject) o).getString("figureurl_qq_2");
                                                     bitmapHead = Util.getbitmap(headUrl);
                                                 } catch (JSONException e) {
                                                     e.printStackTrace();
                                                 }
                                                 Message msgHead = new Message();
                                                 msgHead.what = 1;
                                                 msgHead.obj = bitmapHead;
                                                 mHandler.sendMessage(msgHead);
                                             }
                                         }
                                     }).start();

                                 }

                                 @Override
                                 public void onError(UiError uiError) {
                                     Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onCancel() {
                                     Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                                     Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                                 }
                             }
        );
    }

    //显示获取到的头像和昵称
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//获取昵称
                tvNickName.setText((CharSequence) msg.obj);
            } else if (msg.what == 1) {//获取头像
                headerLogo.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String SINA_WB_APPKEY = "513793262";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";//默认REDIRECT_URL
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;

    private String userId = "";
    private String name = "";
    private String imgUrl = "";
    private void sinaLogin(){
        mAuthInfo = new AuthInfo(this, SINA_WB_APPKEY, REDIRECT_URL, null);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
        mSsoHandler.authorize(new MyWeiboAuthListener());
    }

    class MyWeiboAuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            Log.e("LoginActivity",mAccessToken.toString());
            if (mAccessToken.isSessionValid()) {
                Map<String,String> pramas =  new HashMap<>();
                pramas.put("access_token", mAccessToken.getToken());
                pramas.put("uid", mAccessToken.getUid());
//                HttpUtil.get(mContext, "https://api.weibo.com/2/users/show.json", pramas, new MyStringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.e("LoginActivity",response);
//                        JSONTokener jsonParser = new JSONTokener(response);
//                        try {
//                            JSONObject person = (JSONObject) jsonParser.nextValue();
//                            name = person.getString("name");
//                            userId = person.getString("id");
//                            imgUrl =person.getString("profile_image_url");
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

            } else {
                // 应用程序签名不正确时，就会收到 Code，请确保签名正确
                Log.e("LoginActivity",values.getString("code", ""));
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        @Override
        public void onCancel() {
        }

    }
}
