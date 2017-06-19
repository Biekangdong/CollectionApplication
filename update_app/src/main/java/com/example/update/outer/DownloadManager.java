package com.example.update.outer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * User: Losileeya (847457332@qq.com)
 * Date: 2016-09-27
 * Time: 16:56
 * 类描述：
 *
 * @version :
 */
public class DownloadManager {
    public static final String TAG = DownloadManager.class.getSimpleName();
    private android.app.DownloadManager downloadManager;
    private Context context;
    private static DownloadManager instance;

    private DownloadManager(Context context) {
        downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context.getApplicationContext();
    }

    public static DownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadManager(context);
        }
        return instance;
    }

    /**
     * @param uri
     * @param title
     * @param description
     * @return download id
     */
    public long startDownload(String uri, String title, String description,String appName) {
        android.app.DownloadManager.Request req = new android.app.DownloadManager.Request(Uri.parse(uri));

        req.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);

        req.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, appName+".apk");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        //req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第三种 自定义文件路径
        //req.setDestinationUri()


        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        //req.setMimeType("application/vnd.android.package-archive");
        return downloadManager.enqueue(req);//异步
        //dm.openDownloadedFile()
    }


    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see DownloadManager#getDownloadUri(long)
     */
    public String getDownloadPath(long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }


    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see DownloadManager#getDownloadPath(long)
     */
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    public android.app.DownloadManager getDownloadManager() {
        return downloadManager;
    }


    /**
     * 获取下载状态
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return int
     * @see android.app.DownloadManager#STATUS_PENDING
     * @see android.app.DownloadManager#STATUS_PAUSED
     * @see android.app.DownloadManager#STATUS_RUNNING
     * @see android.app.DownloadManager#STATUS_SUCCESSFUL
     * @see android.app.DownloadManager#STATUS_FAILED
     */
    public int getDownloadStatus(long downloadId) {
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }


    public static void download(Context context, String url, String title,final String appName) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downloadId =sp.getLong(android.app.DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        if (downloadId != -1L) {
            DownloadManager fdm = DownloadManager.getInstance(context);
            int status = fdm.getDownloadStatus(downloadId);
            if (status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                //启动更新界面
                Uri uri = fdm.getDownloadUri(downloadId);
                if (uri != null) {
                    if (compare(getApkInfo(context, uri.getPath()), context)) {
                        startInstall(context, uri);
                        return;
                    } else {
                        fdm.getDownloadManager().remove(downloadId);
                    }
                }
                start(context, url, title,appName);
            } else if (status == android.app.DownloadManager.STATUS_FAILED) {
                start(context, url, title,appName);
            } else {
                Log.d(TAG, "apk is already downloading");
            }
        } else {
            start(context, url, title,appName);
        }
    }

    private static void start(Context context, String url, String title,String appName) {
        long id = DownloadManager.getInstance(context).startDownload(url,
                title, "下载完成后点击打开",appName);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(android.app.DownloadManager.EXTRA_DOWNLOAD_ID,id).commit();
        Log.d(TAG, "apk start download " + id);
    }

    public static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }


    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
