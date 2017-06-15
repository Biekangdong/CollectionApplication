package com.example.collectionapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.collectionapplication.camera.SelectImageActivity;
import com.example.collectionapplication.layout.LayoutMainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    View rl_layout,rl_camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initBar();
    }


    public void initView() {
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        rl_layout=findViewById(R.id.rl_layout);
        rl_camera=findViewById(R.id.rl_camera);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        initBar();
        //初始化侧边栏
        initDrawLayout();
    }
    private  void initBar(){
        // App Logo
//        mToolbar.setLogo(R.drawable.ic_launcher);
        // 主标题,默认为app_label的名字
        mToolbar.setTitle("首页");
        mToolbar.setTitleTextColor(Color.WHITE);
        // 副标题
//        mToolbar.setSubtitle("Sub title");
//        mToolbar.setSubtitleTextColor(Color.parseColor("#80ff0000"));
        //设置导航菜单
//        mToolbar.setNavigationIcon(R.drawable.ic_launcher_round);
        //取代原本的actionbar
        setSupportActionBar(mToolbar);
        //是否显示显示那个箭头以及设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //隐藏toolbar上的app title
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置NavigationIcon的点击事件,需要放在setSupportActionBar之后设置才会生效,
        //因为setSupportActionBar里面也会setNavigationOnClickListener
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               MainActivity.this.finish();
//            }
//        });

    }
    private void initDrawLayout(){
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this,"设置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(MainActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_location:
                Toast.makeText(MainActivity.this,"点位",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void initData() {
        rl_layout.setOnClickListener(this);
        rl_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.rl_camera:
                startActivity(new Intent(this, SelectImageActivity.class));
                break;
           case  R.id.rl_layout:
               startActivity(new Intent(this, LayoutMainActivity.class));
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
