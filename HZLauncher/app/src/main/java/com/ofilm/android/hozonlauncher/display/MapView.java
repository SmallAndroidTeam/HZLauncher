package com.ofilm.android.hozonlauncher.display;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ofilm.android.hozonlauncher.R;


public class MapView extends CustomViewGroup{

    private ImageView navigationIcon;
    private TextView middleTextView;
    View bottomView;

    public MapView(Context context) {
        super(context);
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.map_view, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(layout, params);*/
        navigationIcon = new ImageView(context);
        navigationIcon.setImageDrawable(context.getDrawable(R.mipmap.turn_right_picture));
        navigationIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        //this.addView(navigationIcon);
        middleTextView = new TextView(context);
        middleTextView.setText(R.string.direction_des);
        middleTextView.setTextSize(20);
        middleTextView.setGravity(Gravity.CENTER);
        //this.addView(middleTextView);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bottomView = inflater.inflate(R.layout.map_bottom_view, null);
        //this.addView(bottomView);
        shownExtendMapView();
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

    public void shownExtendMapView() {
        this.removeAllViews();
        middleTextView.setText(R.string.navigation);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        invalidate();
    }

    @Override
    public void restoreViewForInitialState(Context context) {
        this.removeAllViews();
        if (navigationIcon != null) {
            this.addView(navigationIcon);
        } else {
            navigationIcon = new ImageView(context);
            navigationIcon.setImageDrawable(context.getDrawable(R.mipmap.navigation));
            this.addView(navigationIcon);
        }
        if (middleTextView != null) {
            middleTextView.setText(R.string.direction_des);
            this.addView(middleTextView);
        } else {
            middleTextView = new TextView(context);
            middleTextView.setText(R.string.direction_des);
            middleTextView.setTextSize(20);
            middleTextView.setGravity(Gravity.CENTER);
            this.addView(middleTextView);
        }
        if (bottomView != null) {
            this.addView(bottomView);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            bottomView = inflater.inflate(R.layout.map_bottom_view, null);
            this.addView(bottomView);
        }
        invalidate();
    }

    /*@Override
    public void onClick(View v) {
        //Invalidate the whole view. If the view is visible, onDraw(Canvas) will be called at some point in the future.
        //This must be called from a UI thread. To call from a non-UI thread, call postInvalidate().
        //invalidate();
        this.removeAllViews();
        middleTextView.setText(R.string.navigation);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        invalidate();
    }*/
}
