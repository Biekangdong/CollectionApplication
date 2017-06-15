package com.example.collectionapplication.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class LayoutBannerActivity extends BaseActivity implements View.OnClickListener{
    Banner banner;
    //将网址存入到集合中
    private String[] imageUrls = {
            "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
            "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
            "http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg",
            "http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg",
            "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"};

    @Override
    public void initView() {
        super.initView();
        banner=(Banner)findViewById(R.id.banner);
        initBanner();
    }
    @Override
    protected void setmToolbar() {
        toolbarTitle="轮播图";
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_layout_banner;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
    //初始化Banner轮播图
    private void initBanner() {
        List<String> listUrl = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            listUrl.add(imageUrl);
        }
        //设置banner样式
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR    显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE    显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE    显示圆形指示器和标题
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片集合
        banner.setImages(listUrl);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                /**
                 常用的图片加载库：
                 Universal Image Loader：一个强大的图片加载库，包含各种各样的配置，最老牌，使用也最广泛。
                 Picasso: Square出品，必属精品。和OkHttp搭配起来更配呦！
                 Volley ImageLoader：Google官方出品，可惜不能加载本地图片~
                 Fresco：Facebook出的，天生骄傲！不是一般的强大。
                 Glide：Google推荐的图片加载库，专注于流畅的滚动。
                 */
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置banner动画效果
        //banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        //Banner.LEFT    指示器居左
        //Banner.CENTER    指示器居中
        //Banner.RIGHT    指示器居右
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件，下标是从1开始
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(LayoutBannerActivity.this, "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
