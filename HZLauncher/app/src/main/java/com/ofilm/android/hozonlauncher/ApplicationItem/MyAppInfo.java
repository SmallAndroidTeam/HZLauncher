package com.ofilm.android.hozonlauncher.ApplicationItem;

import android.graphics.drawable.Drawable;
import android.util.Log;



/**
 * Create By rongxinglan IN 2018/9/3
 */
public class MyAppInfo {
    private Drawable mIcon;
    private CharSequence mLabel;
    private String  mPackageName;

    public Drawable getIcon(){
        return mIcon;
    }

    public void setIcon(Drawable icon){
        this.mIcon=icon;
    }
    public CharSequence getLabel(){

        return mLabel;
    }
  public void setLabel(CharSequence label){
        this.mLabel=label;
  }
    public String getPackageName() {
        return mPackageName;
    }
    public void setPackageName(String packageName){
        this.mPackageName=packageName;
    }
}
