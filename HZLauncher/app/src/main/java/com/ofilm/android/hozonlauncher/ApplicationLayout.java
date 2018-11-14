package com.ofilm.android.hozonlauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;


import com.ofilm.android.hozonlauncher.ApplicationItem.AppAdapter;
import com.ofilm.android.hozonlauncher.ApplicationItem.MyAppInfo;

import java.util.ArrayList;
import java.util.List;

public class ApplicationLayout extends LinearLayout {

    public static  List<MyAppInfo> mList;
    private GridView mGridView;
    private PackageManager pManager;
    private boolean isShowDelete = false;
    private static final String TAG = "FragmentActivity";
    public ApplicationLayout(Context context) {
        super(context);
//        onBackPressed();
        // this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.application_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(layout, params);
        mGridView = (GridView) findViewById(R.id.gridView);
        //获取图片、应用名、包名
        pManager = context.getPackageManager();
        List<PackageInfo> appList = getAllApps(context);
        mList = new ArrayList<MyAppInfo>();
        int count = appList.size();
        if (count < 16) {
            count = appList.size();
        } else {
            count = 16;
        }
        for (int i = 0; i < count; i++) {
            PackageInfo pinfo = appList.get(i);
            MyAppInfo shareItem = new MyAppInfo();
            //设置图片
            shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
            //设置应用程序名称
            shareItem.setLabel(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
            //设置应用程序的包名
            shareItem.setPackageName(pinfo.applicationInfo.packageName);
            mList.add(shareItem);
        }
        //设置GridView的adapter
        AppAdapter mbaseAdapter = new AppAdapter(context);
        mGridView.setAdapter(mbaseAdapter);
    }

//    public void onBackPressed() {
//        if (mAppAdapter.getShowDelete() == true) {
//            mAppAdapter.setShowDelete(false);
//        } else {
//            context.finish();
//        }
//    }

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        //获取当前应用的包名
        ActivityManager am = (ActivityManager)context.getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn =am.getRunningTasks(1).get(0).topActivity;
        String pac = cn.getPackageName();

        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            if((pak.packageName.equals(pac))){
                continue;
            }else {
                //判断是否为自己安装应用程序，不是则过滤
                if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                    //添加自己已经安装的应用程序
                    apps.add(pak);
                }
            }
        }
        return apps;
    }
}
