package com.ofilm.android.hozonlauncher.ApplicationItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;




public class AppReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();
   // private Context mContext;
    PackageInfo mPackageInfo =null;
    private MyAppInfo mAppInfoList = new MyAppInfo();
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            //获取应用名
            try {
                mPackageInfo = pm.getPackageInfo(packageName,0);
               mAppInfoList.setIcon(mPackageInfo.applicationInfo.loadIcon(pm));
               mAppInfoList.setLabel(mPackageInfo.applicationInfo.loadLabel(pm));
               mAppInfoList.setPackageName(packageName);
               //
            } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            }
         //  LogUtils.printInfo(TAG, "--------安装成功" + packageName);
           // MainActivity.mAppAdapter.setData(MainActivity.mList);
            Toast.makeText(context, "已安装" + packageName, Toast.LENGTH_LONG).show();

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
           // LogUtils.printInfo(TAG, "--------替换成功" + packageName);
            Toast.makeText(context, "已替换" + packageName, Toast.LENGTH_LONG).show();

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
           // LogUtils.printInfo(TAG, "--------卸载成功" + packageName);
            Toast.makeText(context, "已卸载" + packageName, Toast.LENGTH_LONG).show();
        }
    }

}



