package com.example.jpush_project.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光测试通知栏
 */
public class JPushTestCustomeMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("用户自定义打开的Activity");
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            StringBuilder builder=new StringBuilder("Title : " + title + "  " + "Content : " + content+"JPushTest2Activity");
            builder.append("\n");
            builder.append("接收到自定义消息直接跳转该页面\n");
            builder.append("自定义消息不是通知，默认不会被SDK展示到通知栏上，极光推送仅负责透传给SDK");
	        tv.setText(builder.toString());
        }
        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

}
