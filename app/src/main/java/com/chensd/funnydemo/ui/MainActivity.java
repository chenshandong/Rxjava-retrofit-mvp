package com.chensd.funnydemo.ui;

import android.Manifest;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chensd.funnydemo.R;
import com.chensd.funnydemo.model.ImageInfo;
import com.chensd.funnydemo.utils.FileUtil;
import com.chensd.funnydemo.view.BaseView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.viewpager)
    ViewPager mVp;

    @Bind(R.id.tabs)
    TabLayout mTabs;

    private long ExitTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        
        initViewPager();

        /**要求权限*/
        AndPermission.with(this).requestCode(100).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .send();
    }

    private void initViewPager() {
        mVp.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        mTabs.setupWithViewPager(mVp);
        mTabs.setTabTextColors(Color.GRAY, Color.parseColor("#56abe4"));
        mTabs.setSelectedTabIndicatorColor(Color.parseColor("#56abe4"));

    }

    @Override
    protected int getAlertLayoutId() {
        return R.layout.help_dialog;
    }

    @Override
    protected String getAlertTitle() {
        return "说明";
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter{

        String[] titles = {"主页","最近使用"};

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new RecentFragment();
                default:
                    return new MainFragment();
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onBackPressed() {

        /** ----------按两次退出------- */
        if (System.currentTimeMillis() - ExitTimes > 2000)
        {
            showToast("再按一次退出栋栋斗图");
            ExitTimes = System.currentTimeMillis();
            return;
        }

        super.onBackPressed();
    }

    @PermissionYes(100)
    private void getPermissionYes(){
        showToast("权限获取成功");
        FileUtil.createAppDir();
    }

    @PermissionNo(100)
    private void getPermissionNo(List<String> deniedPermissions){
        showToast("权限获取失败");

        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, 300).show();

            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
