package com.example.umeng_share_login_count;

import android.app.Application;
import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Administrator on 2017/6/17.
 */

public class AppApplication extends Application {
    private static AppApplication instance;
    public static Context context;
    {
        //友盟分享第三方平台
        PlatformConfig.setWeixin("wxc92f14b5fa5c03eb", "f6240ad29149a1b0de4717263eee41a2");
        PlatformConfig.setQQZone("1106196388", "KQSgeDR8o26ehv3h");
        PlatformConfig.setSinaWeibo("1029378933", "ede65e9660b26fd2ad0de09fa3776684", "http://return.api.bojuwang.net/");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //友盟分享第三方平台初始化
        UMShareAPI.get(this);
    }
    public static AppApplication getInstance() {
        if (instance == null) {
            synchronized (AppApplication.class) {
                if (instance == null) {
                    instance = new AppApplication();
                }
            }
        }

        return instance;
    }
}
