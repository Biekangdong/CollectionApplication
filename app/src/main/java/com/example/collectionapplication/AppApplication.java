package com.example.collectionapplication;

import android.app.Application;
import android.content.Context;
/**
 * Created by Administrator on 2017/5/19.
 * 应用Application
 */

public class AppApplication extends Application{
    private static AppApplication instance;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
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
