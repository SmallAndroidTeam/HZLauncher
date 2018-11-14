package com.ofilm.android.hozonlauncher;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import com.ofilm.android.hozonlauncher.display.CallView;
import com.ofilm.android.hozonlauncher.display.CallViewExtend;
import com.ofilm.android.hozonlauncher.display.CustomViewGroup;
import com.ofilm.android.hozonlauncher.display.MapView;
import com.ofilm.android.hozonlauncher.display.MapViewExtend;
import com.ofilm.android.hozonlauncher.display.MediaView;
import com.ofilm.android.hozonlauncher.display.MediaViewExtend;
import com.ofilm.android.hozonlauncher.display.WeatherView;
import com.ofilm.android.hozonlauncher.display.WeatherViewExtend;

import java.util.ArrayList;
import java.util.List;

public class DisplayLayout extends ViewGroup {

    private static final int VIEW_INTERNAL = 10;
    //private static final int MAIN_VIEW_LENGTH = 500;
    private static final int VIEW_COUNT = 4;
    private static final int EXTEND_VIEW_COUNT = 4;

    private Context mContext;
    private MapView mapView;
    private MapViewExtend mapViewExtend;
    private MediaView mediaView;
    private MediaViewExtend mediaViewExtend;
    private CallView callView;
    private CallViewExtend callViewExtend;
    private WeatherView weatherView;
    private WeatherViewExtend weatherViewExtend;

    List<CustomViewGroup> viewList = new ArrayList<CustomViewGroup>();
    List<ViewGroup> extendViewList = new ArrayList<ViewGroup>();


    public DisplayLayout(Context context) {
        super(context);
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.display_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(layout, params);*/
        mContext = context;
        mapView = new MapView(context);
        mapView.setOnClickListener(mapViewClickListener);
        mapViewExtend = new MapViewExtend(context);
        mapViewExtend.setTag("view_extend");
        mapViewExtend.setVisibility(View.VISIBLE);
        mediaView = new MediaView(context);
        mediaView.setOnClickListener(mediaViewClickListener);
        mediaViewExtend = new MediaViewExtend(context);
        mediaViewExtend.setTag("view_extend");
        mediaViewExtend.setVisibility(View.GONE);
        callView = new CallView(context);
        callView.setOnClickListener(callViewClickListener);
        callViewExtend = new CallViewExtend(context);
        callViewExtend.setTag("view_extend");
        callViewExtend.setVisibility(View.GONE);
        weatherView = new WeatherView(context);
        weatherView.setOnClickListener(weatherViewClickListener);
        weatherViewExtend = new WeatherViewExtend(context);
        weatherViewExtend.setTag("view_extend");
        weatherViewExtend.setVisibility(View.GONE);
        this.addView(mapViewExtend);
        this.addView(mapView);
        this.addView(mediaViewExtend);
        this.addView(mediaView);
        this.addView(callViewExtend);
        this.addView(callView);
        this.addView(weatherViewExtend);
        this.addView(weatherView);
        this.viewList.add(mapView);
        this.viewList.add(mediaView);
        this.viewList.add(callView);
        this.viewList.add(weatherView);
        this.extendViewList.add(mapViewExtend);
        this.extendViewList.add(mediaViewExtend);
        this.extendViewList.add(callViewExtend);
        this.extendViewList.add(weatherViewExtend);
    }

    View.OnClickListener mapViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mapViewExtend.getVisibility() == View.GONE) {
                mapViewExtend.setVisibility(View.VISIBLE);
                mapView.shownExtendMapView();
                restoreOtherViews(mapView);
                hideOtherExtendViews(mapViewExtend);
                invalidate();
            }
        }
    };

    View.OnClickListener mediaViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mediaViewExtend.getVisibility() == View.GONE) {
                mediaViewExtend.setVisibility(View.VISIBLE);
                mediaView.shownExtendMediaView();
                restoreOtherViews(mediaView);
                hideOtherExtendViews(mediaViewExtend);
                invalidate();
            }

        }
    };

    View.OnClickListener callViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(callViewExtend.getVisibility() == View.GONE) {
                callViewExtend.setVisibility(View.VISIBLE);
                callView.shownExtendCallView();
                restoreOtherViews(callView);
                hideOtherExtendViews(callViewExtend);
                invalidate();
            }

        }
    };

    View.OnClickListener weatherViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(weatherViewExtend.getVisibility() == View.GONE) {
                weatherViewExtend.setVisibility(View.VISIBLE);
                weatherView.shownExtendWeatherView();
                restoreOtherViews(weatherView);
                hideOtherExtendViews(weatherViewExtend);
                invalidate();
            }

        }
    };

    private void restoreOtherViews(CustomViewGroup currentView) {
        for(int i = 0; i < viewList.size(); i++) {
            CustomViewGroup view = viewList.get(i);
            if (view != currentView) {
                view.restoreViewForInitialState(mContext);
            }
        }
    }

    private void hideOtherExtendViews(ViewGroup currentExtendView) {
        for (int i = 0; i < extendViewList.size(); i++) {
            ViewGroup view = extendViewList.get(i);
            if (view != currentExtendView) {
                view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getMeasuredWidth() The raw measured height of this view.
        int pageWidth = getMeasuredWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int pageHeight = getMeasuredHeight();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        childCount = childCount - EXTEND_VIEW_COUNT;
        int childMeasureWidthSpec = MeasureSpec.makeMeasureSpec((pageWidth/2 - ((childCount - 1) * VIEW_INTERNAL))/childCount, widthMode);
        int childMeasureHeightSpec = heightMode + pageHeight;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if(childView.getTag() != null && childView.getTag().equals("view_extend")) {
                int childExtendMeasureWidthSpec = MeasureSpec.makeMeasureSpec(pageWidth/2, MeasureSpec.EXACTLY);
                childView.measure(childExtendMeasureWidthSpec, childMeasureHeightSpec);
            } else {
                childView.measure(childMeasureWidthSpec, childMeasureHeightSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                childView.setBackgroundColor(Color.GRAY);
                int childWidth = childView.getMeasuredWidth();
                // getWidth() The width of your view, in pixels.
                //childWidth = childView.getWidth();
                int childHeight = childView.getMeasuredHeight();
                if(childView.getTag() != null && childView.getTag().equals("view_extend")) {
                    childView.layout(childLeft, 0, childLeft + childWidth, childHeight);
                    childView.setBackgroundResource(R.color.mediaBackground);
                    childLeft = childLeft + childWidth;
                } else {
                    childView.layout(childLeft, 0, childLeft + childWidth, childHeight);
                    childLeft = childLeft + childWidth + VIEW_INTERNAL;
                }
            }
        }
    }

}
