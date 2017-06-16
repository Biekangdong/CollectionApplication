package com.example.collectionapplication.layout;

import android.content.Intent;
import android.view.View;

import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;
import com.example.collectionapplication.coordinator.LayoutCoordinatorActivity;

/**
 * Created by Administrator on 2017/6/14.
 */

public class LayoutMainActivity extends BaseActivity implements View.OnClickListener{
    View rl_banner,rl_browser,rl_CoordinatorLayout;

    @Override
    public void initView() {
        super.initView();
        rl_banner=findViewById(R.id.rl_banner);
        rl_browser=findViewById(R.id.rl_browser);
        rl_CoordinatorLayout=findViewById(R.id.rl_CoordinatorLayout);
    }

    @Override
    public void initData() {
        super.initData();
        rl_banner.setOnClickListener(this);
        rl_browser.setOnClickListener(this);
        rl_CoordinatorLayout.setOnClickListener(this);
    }

    @Override
    protected void setmToolbar() {
        toolbarTitle="布局选择";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_main;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_banner:
                  startActivity(new Intent(this,LayoutBannerActivity.class));
                break;
            case R.id.rl_browser:
                Intent n = new Intent(this, LayoutBrowserActivity.class);
                String url="http://192.168.0.137:809/video.html";
                n.putExtra("url",url);
                n.putExtra("title", "视频测试");
                startActivity(n);
                break;
            case R.id.rl_CoordinatorLayout:
                startActivity(new Intent(this,LayoutCoordinatorActivity.class));
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
