package com.example.jpush_project;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/6/17.
 */

public class AppApplication extends Application {
    private static AppApplication instance;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
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
