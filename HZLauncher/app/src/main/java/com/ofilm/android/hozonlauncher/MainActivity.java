package com.ofilm.android.hozonlauncher;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ofilm.android.hozonlauncher.display.MediaViewExtend;

public class MainActivity extends Activity {

    private  static  Context mContext;
    ScrollLayout scrollLayout;
    int screenWidth;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager windowManager = this.getWindowManager();
        screenWidth=windowManager.getDefaultDisplay().getWidth();
        //setContentView(R.layout.activity_main);
        scrollLayout = new ScrollLayout(mContext);

        setContentView(scrollLayout);
    }


    @Override
    protected void onResume() {
        super.onResume();
        fragmentManager =MainActivity.this.getFragmentManager();
        if (scrollLayout != null) {
            int mCurScreen = scrollLayout.getCurScreen();
            if (mCurScreen != 2) {
                scrollLayout.scrollTo(screenWidth, 0);
            }
        }
    }


}
