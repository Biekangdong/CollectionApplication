package com.example.collectionapplication.layout;

import android.content.Intent;
import android.view.View;

import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;

/**
 * Created by Administrator on 2017/6/14.
 */

public class LayoutMainActivity extends BaseActivity implements View.OnClickListener{
    View rl_banner,rl_browser;

    @Override
    public void initView() {
        super.initView();
        rl_banner=findViewById(R.id.rl_banner);
        rl_browser=findViewById(R.id.rl_browser);

    }

    @Override
    public void initData() {
        super.initData();
        rl_banner.setOnClickListener(this);
        rl_browser.setOnClickListener(this);
    }

    @Override
    protected String setmToolbar() {
        String toolbarTitle="布局选择";
        return toolbarTitle;
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
                String url="http://chat.baomihua6.com/Uploads/Seller/495/2017-05-16/591a9032a1837.mp4";
                n.putExtra("url",url);
                n.putExtra("title", "视频测试");
                startActivity(n);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
