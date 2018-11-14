package com.ofilm.android.hozonlauncher;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {
    private static final String TAG = "ScrollLayout";
    private int VIEW_WIDTH = 1280;
    private int SCREEN_INTERVAL = 0;
    private Scroller mScroller;
    private boolean isInit = false;
    private int mCurScreen = 2;
    int duration = 0;
    int scrollScreen = 2;
    int touchDownX = 0;
    int touchDownY = 0;
    long touchDownStartTime;
    int mActivePointerId;
    private boolean isActionMove;
    private int mScaledTouchSlop;
    private float downX;
    private float downY;
    private int screenHeigt;
    private int screenWidth;


    public ScrollLayout(Context context) {
        super(context);
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        screenHeigt=windowManager.getDefaultDisplay().getHeight();
        screenWidth=windowManager.getDefaultDisplay().getWidth();
        VIEW_WIDTH = screenWidth;
        mScroller = new Scroller(context, new AccelerateInterpolator(), true);
        this.setBackgroundColor(context.getResources().getColor(R.color.screenPrimary));
        //Distance in pixels a touch can wander before we think the user is scrolling
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        ButtonLayout btnLayout = new ButtonLayout(context);
        DisplayLayout disLayout = new DisplayLayout(context);
        ApplicationLayout appLayout = new ApplicationLayout(context);
        this.addView(btnLayout);
        this.addView(disLayout);
        this.addView(appLayout);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isActionMove = false;
                int index = MotionEventCompat.getActionIndex(ev);
                downX = MotionEventCompat.getX(ev, index);
                downY = MotionEventCompat.getY(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return false;
                }
                int pointIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointIndex < 0) {
                    return false;
                }
                int currentX = (int) MotionEventCompat.getX(ev, pointIndex);
                //int currentY = (int) MotionEventCompat.getY(ev, pointIndex);
                float dx = downX - currentX;
                //float dy = downY - currentY;
                if (!isActionMove) {
                    if (Math.abs(dx) > mScaledTouchSlop) {
                        isActionMove = true;
                        downX = currentX;
                        //downY = currentY;
                    }
                }
                break;
        }
        return isActionMove;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                int index = event.getActionIndex();
                touchDownStartTime = SystemClock.currentThreadTimeMillis();
                mActivePointerId = event.getPointerId(index);
                touchDownX = (int) MotionEventCompat.getX(event, index);
                touchDownY = (int) MotionEventCompat.getY(event, index);
                Log.d(TAG,"ACTION DOWN touchDownX = " + touchDownX + " touchDownY = " + touchDownY);
                break;
            case MotionEvent.ACTION_MOVE :
                if (mActivePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return false;
                }
                int pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                int touchCurrentX = (int) MotionEventCompat.getX(event, pointerIndex);
                int touchCurrentY = (int) MotionEventCompat.getY(event, pointerIndex);
                float dx = touchCurrentX - touchDownX;
                float dy = touchCurrentY - touchDownY;
                Log.d(TAG,"ACTION MOVE dx = " + dx);
                scrollScreen = judgeScrollScreen(dx);
                Log.d(TAG,"ACTION MOVE scrollScreen = " + scrollScreen);
                //scrollScreen = Math.max(1, Math.min(scrollScreen, getChildCount()));
                int scrollX = getScrollX(); //The left edge of the displayed part of your view, in pixels.
                int nextScreenStartWidth = (scrollScreen - 1) * VIEW_WIDTH;
                if (scrollX != nextScreenStartWidth) {
                    int delta = 0;
                    int startX = 0;
                    if (scrollScreen > mCurScreen) { //右移
                        delta = nextScreenStartWidth - scrollX;
                        startX = scrollX;

                    } else if (scrollScreen < mCurScreen) { //左移
                        delta = -VIEW_WIDTH;
                        startX = scrollX;
                    } else {
                        startX = scrollX;
                        delta = nextScreenStartWidth - scrollX;
                    }
                    duration = 350;
                    /*Start scrolling by providing a starting point, the distance to travel, and the duration of the scroll.Parameters:
                        startX - Starting horizontal scroll offset in pixels. Positive numbers will scroll the content to the left.
                        startY - Starting vertical scroll offset in pixels. Positive numbers will scroll the content up.
                        dx - Horizontal distance to travel. Positive numbers will scroll the content to the left.
                        dy - Vertical distance to travel. Positive numbers will scroll the content up.
                        duration - Duration of the scroll in milliseconds.*/
                    mScroller.startScroll(startX, 0, delta, 0, duration); //移动
                    touchDownX = touchCurrentX;
                    invalidate(); // Redraw the layout
            }
            break;
            case MotionEvent.ACTION_UP:
                scrollTo((scrollScreen - 1) * VIEW_WIDTH, 0);
                mCurScreen = scrollScreen;
                break;
        }
        return true;
    }

    public int getCurScreen() {
        return mCurScreen;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    private int judgeScrollScreen(float dx) {
        Log.d(TAG, "dx = " + dx + " mScaledTouchSlop = " + mScaledTouchSlop + " scrollScreen = " + scrollScreen);
        if (Math.abs(dx) < mScaledTouchSlop) {
            return scrollScreen;
        }
        if (mCurScreen == 2) {
            if (dx < 0) {
                scrollScreen = getChildCount();
            } else if (dx > 0) {
                scrollScreen = 1;
            }
        } else if (mCurScreen == 1) {
            if (dx < 0) {
                scrollScreen = 2;
            } else if (dx > 0) {
                scrollScreen = 1;
            }
        } else if (mCurScreen == 3) {
            if (dx < 0) {
                scrollScreen = getChildCount();
            } else if (dx > 0) {
                scrollScreen = 2;
            }
        }
        return scrollScreen;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = VIEW_WIDTH;
        int measueWidth = MeasureSpec.getMode(widthMeasureSpec) + width;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(measueWidth, heightMeasureSpec);
        }
        if (!isInit) {
            scrollTo((mCurScreen - 1) * width, 0);
            isInit = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }

        }
    }
}
