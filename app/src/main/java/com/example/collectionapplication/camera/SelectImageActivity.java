package com.example.collectionapplication.camera;


import com.example.collectionapplication.BaseActivity;
import com.example.collectionapplication.R;

/**
 * Created by Administrator on 2017/6/14.
 */

public class SelectImageActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }
    @Override
    protected String setmToolbar() {
        String toolbarTitle="布局选择";
        return toolbarTitle;
    }
}
