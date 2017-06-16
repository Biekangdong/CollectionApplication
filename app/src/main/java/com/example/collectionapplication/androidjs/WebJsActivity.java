package com.example.collectionapplication.androidjs;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WebJsActivity extends BaseActivity implements View.OnClickListener{
    private WebView mWebView = null;
    private Button button,button2;
    @Override
    protected void setmToolbar() {
        toolbarTitle="js互调";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webjs;
    }

    @Override
    public void initView() {
        super.initView();
        initWebView();
        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
    }

    @Override
    public void initData() {
        super.initData();
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    public void initWebView(){
        mWebView = (WebView) findViewById(R.id.webview);
        //支持JavaScript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 加载html
        mWebView.loadUrl("file:///android_asset/web.html");

        //设置ChromeClient
        mWebView.setWebChromeClient(new HarlanWebChromeClient());
        //android为flag  jsdi调用Native用
        mWebView.addJavascriptInterface(WebJsActivity.this, "android");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.button://调用js函数
                mWebView.loadUrl("javascript:javaCallJs()");
                break;
            case R.id.button2: // 传递参数调用
                //调用js函数并携带参数
                final String param = "'这是参数，注意这个参数的格式'";
                mWebView.loadUrl("javascript:javaCallJswithParam(" + param + ")");
                break;
        }
    }

    //由于安全原因 需要加 @JavascriptInterface  该函数供js调用，无参
    @JavascriptInterface
    public void startFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(WebJsActivity.this).setMessage("native方法触发").show();
                Toast.makeText(WebJsActivity.this,"native方法触发",Toast.LENGTH_SHORT).show();
            }
        });
    }
     // 该函数供js调用，有返回参数
    @JavascriptInterface
    public void startFunction(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(WebJsActivity.this).setMessage(text).show();
                Toast.makeText(WebJsActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });
    }


    /***
     * webChromeClient主要是将javascript中相应的方法翻译成android本地方法
     * <p>
     * 例如：我们重写了onJsAlert方法，那么当页面中需要弹出alert窗口时，便
     * 会执行我们的代码，按照我们的Toast的形式提示用户。
     */
    class HarlanWebChromeClient extends WebChromeClient {

        /*此处覆盖的是javascript中的alert方法。
         *当网页需要弹出alert窗口时，会执行onJsAlert中的方法
         * 网页自身的alert方法不会被调用。
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            show("onJsAlert");
            result.confirm();
            return true;
        }

        /*此处覆盖的是javascript中的confirm方法。
         *当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
         * 网页自身的confirm方法不会被调用。
         */
        @Override
        public boolean onJsConfirm(WebView view, String url,
                                   String message, JsResult result) {
            show("onJsConfirm");
            result.confirm();
            return true;
        }

        /*此处覆盖的是javascript中的confirm方法。
         *当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
         * 网页自身的confirm方法不会被调用。
         */
        @Override
        public boolean onJsPrompt(WebView view, String url,
                                  String message, String defaultValue,
                                  JsPromptResult result) {
            show("onJsPrompt....");
            result.confirm();
            return true;
        }
    }
    private void show(String warring) {
        Toast.makeText(this, warring, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
