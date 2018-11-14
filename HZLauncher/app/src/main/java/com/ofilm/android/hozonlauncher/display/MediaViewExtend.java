package com.ofilm.android.hozonlauncher.display;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ofilm.android.hozonlauncher.MainActivity;
import com.ofilm.android.hozonlauncher.R;
import com.ofilm.android.hozonlauncher.musicFragment.MusicHallFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MediaViewExtend extends ViewGroup implements View.OnClickListener {

    private static final String TAG = "MediaViewExtend";

    private View mediaBody;
    private Button myMusicHallBtn;
    private Button myMusicBtn;
    private Button findBtn;
    private Fragment musicHallFragment;
    public MediaViewExtend(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mediaBody = inflater.inflate(R.layout.extend_media_body_view, null);
        mediaBody.setTag("media_body");
        myMusicHallBtn=mediaBody.findViewById(R.id.musicHallButton);
        myMusicBtn=mediaBody.findViewById(R.id.myMusicButton);
        findBtn=mediaBody.findViewById(R.id.findMusicButton);
        initEvent();
        setTab(0);//默认选中第一个Tab
        this.addView(mediaBody);
    }


    private void setTab(final  int i) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(MainActivity.fragmentManager!=null){
                    switch (i){
                        case 0:
                            myMusicHallBtn.callOnClick();
                            break;
                        case 1:
                            myMusicBtn.callOnClick();
                            break;
                        case 2:
                            findBtn.callOnClick();
                            break;
                        default:
                            break;
                    }
                    this.cancel();
                }
            }
        },0,100);

    }

    private void initEvent() {
        myMusicHallBtn.setOnClickListener(this);
        myMusicBtn.setOnClickListener(this);
        findBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
    FragmentManager fragmentManager=MainActivity.fragmentManager;
        final  FragmentTransaction fragmentTransaction;
    if(fragmentManager==null){
        return;
    }else{
       fragmentTransaction=fragmentManager.beginTransaction();
    }
        hideAllFragment(fragmentTransaction);
         switch (view.getId()){
             case R.id.musicHallButton:
                 if(musicHallFragment==null){
                   musicHallFragment=new MusicHallFragment();
                   fragmentTransaction.add(R.id.musicFragmentLayout,musicHallFragment);
                 }else{
                     fragmentTransaction.show(musicHallFragment);
                 }
               break;
             case R.id.myMusicButton:
                 break;
             case R.id.findMusicButton:
                 break;
                 default:
                     break;
         }
         fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction ){
        if(musicHallFragment!=null){
            fragmentTransaction.hide(musicHallFragment);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0 ; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getTag() == null || childView.getTag().equals("")) return;
            if (childView.getTag().equals("media_body")) {
                int mediaBodyWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int mediaBodyHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                childView.measure(mediaBodyWidthSpec, mediaBodyHeightSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            if (childView.getTag() == null || childView.getTag().equals("")) return;
            if (childView.getTag().equals("media_body")) {
                int childLeft = 0;
                int childTop = 0;
                childView.layout(childLeft, childTop, childWidth + childLeft, childHeight + childTop);
            }
        }
    }


}
