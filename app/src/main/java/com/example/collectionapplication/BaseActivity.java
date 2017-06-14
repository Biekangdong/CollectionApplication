package com.example.collectionapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Administrator on 2017/5/19.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements  View.OnClickListener,BaseViewInterface{
    public Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(getLayoutId());
        initView();
        initData();
        initToolBar();
    }

    @Override
    public void initView() {
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void initData() {

    }

    protected abstract String setmToolbar();

    public  void initToolBar(){
        mToolbar.setTitle(setmToolbar());
        mToolbar.setTitleTextColor(Color.WHITE);
        //设置导航菜单
        mToolbar.setNavigationIcon(R.drawable.back);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    /**
     * 布局ID
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }




    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
