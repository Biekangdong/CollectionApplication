package com.example.collectionapplication.layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;


/**
 * Created by Administrator on 2016/7/5.
 */
public class LayoutBrowserActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_back;
    TextView tv_title;
    WebView webView;
    protected int getLayoutId() {
        return R.layout.activity_layout_browser;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
               finish();
                break;
        }
    }


    @Override
    protected String setmToolbar() {
        String toolbarTitle="布局选择";
        return toolbarTitle;
    }
    @Override
    public void initView() {
        super.initView();
        tv_back=(TextView)findViewById(R.id.tv_back);
        tv_title=(TextView)findViewById(R.id.tv_title);
        webView=(WebView)findViewById(R.id.webview);
        initWebView();
    }

    @Override
    public void initData() {
        super.initData();
        tv_back.setOnClickListener(this);

    }

    public void initWebView() {
        //启用JS脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        //启用内置缩放装置
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(false);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        String url =getIntent().getStringExtra("url");
        String title =getIntent().getStringExtra("title");
        tv_title.setText(title);
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (uri.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    startActivity(intent);
                } else{
                    view.loadUrl(uri);
                }
                return true;
            }
        });
        //WebView 事件回调监听
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//
//                if (newProgress == 100) {
//                    ll_view.setVisibility(View.GONE);
//                } else {
//                    ll_view.setVisibility(View.VISIBLE);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面
        } else {
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
