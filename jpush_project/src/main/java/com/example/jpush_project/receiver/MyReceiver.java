package com.example.jpush_project.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.example.jpush_project.ui.JPushTest2Activity;
import com.example.jpush_project.ui.JPushTestActivity;
import com.example.jpush_project.utils.Utils;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private NotificationManager nm;
	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == nm) {
			nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " +  Utils.printBundle(TAG,bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			Log.d(TAG, "JPush用户注册成功");

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接受到推送下来的自定义消息");
            //直接打开Activity
            Intent mIntent = new Intent(context, JPushTest2Activity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接受到推送下来的通知");
			receivingNotification(context,bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "用户点击打开了通知");

			openNotification(context,bundle);

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}


	String  TYPE_THIS="TYPE_THIS";
	String TYPE_ANOTHER="TYPE_ANOTHER";

	private void receivingNotification(Context context, Bundle bundle){
		String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		Log.d(TAG, " title : " + title);
		String message = bundle.getString(JPushInterface.EXTRA_ALERT);
		Log.d(TAG, "message : " + message);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Log.d(TAG, "extras : " + extras);
	}

	//打开通知
	private void openNotification(Context context, Bundle bundle){
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String myValue = "";
		try {
			JSONObject extrasJson = new JSONObject(extras);
			myValue = extrasJson.optString("myKey");
		} catch (Exception e) {
			Log.d(TAG, "Unexpected: extras is not a valid json"+e);
			return;
		}
		if (TYPE_THIS.equals(myValue)) {
			Intent mIntent = new Intent(context, JPushTestActivity.class);
			mIntent.putExtras(bundle);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		} else if (TYPE_ANOTHER.equals(myValue)){
			Intent mIntent = new Intent(context, JPushTest2Activity.class);
			mIntent.putExtras(bundle);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		}else{
			Intent mIntent = new Intent(context, JPushTestActivity.class);
			mIntent.putExtras(bundle);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		}
	}


    /**
     * 通知 vs 自定义消息

     极光推送包含有通知与自定义消息两种类型的推送。本文描述他们的区别，以及建议的应用场景。



     两者的区别 - 功能角度

     通知

     通知（Notification），指在手机的通知栏（状态栏）上会显示的一条通知信息。这是 Android / iOS 的基本功能。
     通知主要用于提示用户。一条通知，简单的填写纯文本的通知内容即可。
     应用加上通知功能，有利于提高应用的活跃度。
     自定义消息

     自定义消息不是通知，默认不会被SDK展示到通知栏上，极光推送仅负责透传给SDK。其内容和展示形式完全由开发者自己定义。
     自定义消息主要用于应用的内部业务逻辑和特殊展示需求。




     两者的区别 - 开发者使用角度

     通知

     简单场景下的通知，用户可以不写一行代码，而完全由 SDK 来负责默认的效果展示，以及默认用户点击时打开应用的主界面。
     JPush Android SDK 提供了 API 让开发者来定制通知栏的效果，请参考：自定义通知栏样式教程；也提供了 接收推送消息Receiver 让你来定制在收到通知时与用户点击通知时的不同行为。
     自定义消息

     SDK 不会把自定义消息展示到通知栏。所以调试时，需要到日志里才可以看到服务器端推送的自定义消息。
     自定义消息一定要由开发者写 接收推送消息Receiver 来处理收到的消息。
     注意：
     当自定义消息内容msg_content为空时，SDK不会对消息进行广播，使得app无法接收到推送的消息，因此建议在使用自定义消息推送时添加内容。
     */
}
