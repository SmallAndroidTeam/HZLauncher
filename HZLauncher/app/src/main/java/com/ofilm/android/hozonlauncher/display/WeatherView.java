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

public class WeatherView extends CustomViewGroup{

    ImageView weatherIcon;
    TextView middleTextView;
    TextView bottomTextView;
    public WeatherView(Context context) {
        super(context);
        weatherIcon = new ImageView(context);
        weatherIcon.setImageDrawable(context.getDrawable(R.mipmap.weather_picture));
        weatherIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(weatherIcon);
        middleTextView = new TextView(context);
        middleTextView.setText(R.string.weather_report_title);
        middleTextView.setTextSize(20);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        bottomTextView = new TextView(context);
        bottomTextView.setText(R.string.weather_report_detail);
        bottomTextView.setTextSize(18);
        bottomTextView.setGravity(Gravity.CENTER);
        this.addView(bottomTextView);
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

    public void shownExtendWeatherView() {
        this.removeAllViews();
        middleTextView.setText(R.string.weather_report);
        middleTextView.setGravity(Gravity.CENTER);
        this.addView(middleTextView);
        invalidate();
    }

    @Override
    public void restoreViewForInitialState(Context context) {
        this.removeAllViews();
        if (weatherIcon != null) {
            this.addView(weatherIcon);
        } else {
            weatherIcon = new ImageView(context);
            weatherIcon.setImageDrawable(context.getDrawable(R.mipmap.weather_picture));
            this.addView(weatherIcon);
        }
        if (middleTextView != null) {
            middleTextView.setText(R.string.weather_report_title);
            this.addView(middleTextView);
        } else {
            middleTextView = new TextView(context);
            middleTextView.setText(R.string.weather_report_title);
            middleTextView.setTextSize(20);
            middleTextView.setGravity(Gravity.CENTER);
            this.addView(middleTextView);
        }
        if (bottomTextView != null) {
            this.addView(bottomTextView);
        } else {
            bottomTextView = new TextView(context);
            bottomTextView.setText(R.string.weather_report_detail);
            bottomTextView.setTextSize(18);
            bottomTextView.setGravity(Gravity.CENTER);
            this.addView(bottomTextView);
        }
        invalidate();
    }
}
