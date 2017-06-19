package com.example.update.outer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.update.R;
import com.example.update.bean.UpdateAppInfo;
import com.example.update.utils.CheckUpdateUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/17.
 */

public class OuterUpdateActivity extends AppCompatActivity {
    private AlertDialog.Builder mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_update);
        //网络检查版本是否需要更新
        String isForce="1";//是否需要强制更新
        String downUrl= "http://openbox.mobilem.360.cn/index/d/sid/3706713";//apk下载地址
        String updateinfo = "aaaaaaaaaaaaaaaaaa";//apk更新详情
        String appName = "外部更新";
        if(isForce.equals("1")&& !TextUtils.isEmpty(updateinfo)){//强制更新
            forceUpdate(OuterUpdateActivity.this,appName,downUrl,updateinfo);
        }else{//非强制更新
            //正常升级
            normalUpdate(OuterUpdateActivity.this,appName,downUrl,updateinfo);
        }
//        CheckUpdateUtils.checkUpdate("apk", "1.2.0", new CheckUpdateUtils.CheckCallBack() {
//            @Override
//            public void onSuccess(UpdateAppInfo updateInfo) {
//                String isForce=updateInfo.data.getLastForce();//是否需要强制更新
//                String downUrl= updateInfo.data.getUpdateurl();//apk下载地址
//                String updateinfo = updateInfo.data.getUpgradeinfo();//apk更新详情
//                String appName = updateInfo.data.getAppname();
//                if(isForce.equals("1")&& !TextUtils.isEmpty(updateinfo)){//强制更新
//                    forceUpdate(OuterUpdateActivity.this,appName,downUrl,updateinfo);
//                }else{//非强制更新
//                    //正常升级
//                    normalUpdate(OuterUpdateActivity.this,appName,downUrl,updateinfo);
//                }
//            }
//
//            @Override
//            public void onError() {
//                noneUpdate(OuterUpdateActivity.this);
//            }
//        });
    }
    /**
     * 强制更新
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName+"又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                DownloadManager.download(OuterUpdateActivity.this,downUrl,updateinfo,appName);
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 正常更新
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName+"又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                DownloadManager.download(OuterUpdateActivity.this,downUrl,updateinfo,appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }

    /**
     * 无需跟新
     * @param context
     */
    private void noneUpdate(Context context) {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false) .create().show();
    }



    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}