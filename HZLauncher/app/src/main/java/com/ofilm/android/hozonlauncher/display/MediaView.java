package com.ofilm.android.hozonlauncher.display;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ofilm.android.hozonlauncher.R;

public class MediaView extends CustomViewGroup{

    ImageView musicianIcon;
    TextView middleTextView;
    View bottomView;

    public MediaView(Context context) {
        super(context);
        musicianIcon = new ImageView(context);
        musicianIcon.setImageDrawable(context.getDrawable(R.mipmap.musician_icon));
        musicianIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(musicianIcon);
        middleTextView = new TextView(context);
        middleTextView.setText(R.string.media_des);
        middleTextView.setTextSize(20);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bottomView = inflater.inflate(R.layout.media_bottom_view, null);
        this.addView(bottomView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int viewHeight = getMeasuredHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        int childWidthSpec = viewWidth + widthMode;
        if (childCount == 0) return;
        int childHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight/childCount, MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(childWidthSpec, childHeightSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = 0;
        int childLeft = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childTop = childTop + childHeight;
            }
        }
    }

    public void shownExtendMediaView() {
        this.removeAllViews();
        middleTextView.setText(R.string.extend_media_des);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        invalidate();
    }

    @Override
    public void restoreViewForInitialState(Context context) {
        this.removeAllViews();
        if (musicianIcon != null) {
            this.addView(musicianIcon);
        } else {
            musicianIcon = new ImageView(context);
            musicianIcon.setImageDrawable(context.getDrawable(R.mipmap.navigation));
            this.addView(musicianIcon);
        }
        if (middleTextView != null) {
            middleTextView.setText(R.string.media_des);
            this.addView(middleTextView);
        } else {
            middleTextView = new TextView(context);
            middleTextView.setText(R.string.media_des);
            middleTextView.setTextSize(20);
            middleTextView.setGravity(Gravity.CENTER);
            this.addView(middleTextView);
        }
        if (bottomView != null) {
            this.addView(bottomView);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            bottomView = inflater.inflate(R.layout.media_bottom_view, null);
            this.addView(bottomView);
        }
        invalidate();

    }
}
