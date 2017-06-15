package com.example.collectionapplication.camera;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;
import com.example.collectionapplication.easypermissions.AfterPermissionGranted;
import com.example.collectionapplication.easypermissions.AppSettingsDialog;
import com.example.collectionapplication.easypermissions.EasyPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by Administrator on 2017/6/14.
 */

public class SelectImageActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    static final String TAG = "SelectImageActivity";
    Button btn_single_choose, btn_multi_choose;
    CircleImageView iv_circle;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int READ_CAMERA = 123;//请求手机信息权限
    private static final int RC_SETTINGS_SCREEN = 125;//请求码
    protected static Uri tempUri;
    private String mImagePath = Environment.getExternalStorageDirectory()+"/meta/";

    @Override
    public void initView() {
        super.initView();
        btn_single_choose = (Button) findViewById(R.id.btn_single_choose);
        btn_multi_choose = (Button) findViewById(R.id.btn_multi_choose);
        iv_circle = (CircleImageView) findViewById(R.id.iv_circle);
    }

    @Override
    public void initData() {
        super.initData();
        btn_single_choose.setOnClickListener(this);
        btn_multi_choose.setOnClickListener(this);
    }

    @Override
    protected void setmToolbar() {
        toolbarTitle="图片选择";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_single_choose:
                requestCameraPermission();
                break;
            case R.id.btn_multi_choose:
               startActivity(new Intent(SelectImageActivity.this,SelectMultiActivity.class));
                break;
        }
    }

    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                    case CHOOSE_PICTURE: // 选择本地照片
                        choosePicture();
                        break;
                }
            }
        });
        builder.show();
    }

    //拍照
    private void takePicture() {
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file=new File(mImagePath, "user_info.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri= FileProvider.getUriForFile(this, getPackageName()+".provider", file);
            Log.d(TAG,"filePath = "+tempUri.toString());
        }else {
            tempUri = Uri.fromFile(file);
            // 将拍照所得的相片保存到SD卡根目录
        }
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //相册
    private void choosePicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, CHOOSE_PICTURE);
    }

    //裁剪
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap mBitmap = extras.getParcelable("data");
            iv_circle.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
            saveImage(mBitmap);
        }
    }

    /**
     * 保存图片到本地
     */
    public  void saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "meta");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 请求存储和拍照权限
     */
    @AfterPermissionGranted(READ_CAMERA)
    public void requestCameraPermission() {
        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA})) {
            showChoosePicDialog();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.camera_permission),
                    READ_CAMERA, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA});
        }
    }

    //请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        showChoosePicDialog();
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }


}
