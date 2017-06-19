package com.example.umeng_share_login_count.count;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.umeng_share_login_count.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;

/**
 * Created by Administrator on 2017/6/17.
 */

public class UMCountActivity extends BaseActivity implements View.OnClickListener{
    private final String mPageName = "AnalyticsHome";
   Button umeng_example_analytics_signin,umeng_example_analytics_signoff,umeng_example_analytics_social,umeng_example_analytics_make_crash,umeng_example_analytics_event;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_umcount;
    }

    @Override
    public void initView() {
        super.initView();
        umeng_example_analytics_signin=(Button) findViewById(R.id.umeng_example_analytics_signin);
        umeng_example_analytics_signoff=(Button) findViewById(R.id.umeng_example_analytics_signoff);
        umeng_example_analytics_social=(Button) findViewById(R.id.umeng_example_analytics_social);
        umeng_example_analytics_make_crash=(Button) findViewById(R.id.umeng_example_analytics_make_crash);
        umeng_example_analytics_event=(Button) findViewById(R.id.umeng_example_analytics_event);
        umeng_example_analytics_signin.setOnClickListener(this);
        umeng_example_analytics_signoff.setOnClickListener(this);
        umeng_example_analytics_social.setOnClickListener(this);
        umeng_example_analytics_make_crash.setOnClickListener(this);
        umeng_example_analytics_event.setOnClickListener(this);
    }

    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.umeng_example_analytics_signin://用户登录
                MobclickAgent.onProfileSignIn("example_id");
                break;
            case R.id.umeng_example_analytics_signoff://用户退出
                MobclickAgent.onProfileSignOff();
                break;
            case R.id.umeng_example_analytics_social://社交统计
                UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.SINA_WEIBO, "user_id");
                platform.setGender(UMPlatformData.GENDER.MALE); // optional
                platform.setWeiboId("weiboId"); // optional

                MobclickAgent.onSocialEvent(this, platform);
                break;
            case R.id.umeng_example_analytics_make_crash://程序崩溃
                "123".substring(10);
                break;
            case R.id.umeng_example_analytics_event://事件统计
                MobclickAgent.onEvent(this, "click");
                MobclickAgent.onEvent(this, "click", "button");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

}
