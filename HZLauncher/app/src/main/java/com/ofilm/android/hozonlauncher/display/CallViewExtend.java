package com.ofilm.android.hozonlauncher.display;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ofilm.android.hozonlauncher.R;

public class CallViewExtend extends ViewGroup {

    private ImageView callerView;

    public CallViewExtend(Context context) {
        super(context);
        callerView = new ImageView(context);
        callerView.setImageDrawable(context.getDrawable(R.mipmap.caller_extend_page));
        callerView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(callerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0 ; i < childCount; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            getChildAt(i).layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft = childLeft + childWidth;
        }
    }
}
