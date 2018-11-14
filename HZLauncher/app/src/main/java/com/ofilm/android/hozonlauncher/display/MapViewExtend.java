package com.ofilm.android.hozonlauncher.display;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ofilm.android.hozonlauncher.R;

public class MapViewExtend extends ViewGroup {

    private ImageView mapView;

    public MapViewExtend(Context context) {
        super(context);
        mapView = new ImageView(context);
        mapView.setImageDrawable(context.getDrawable(R.mipmap.map_view));
        mapView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(mapView);
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
