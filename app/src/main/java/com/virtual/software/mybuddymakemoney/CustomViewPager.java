package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private boolean swipeEnabled = true;
    private OnPageChangeListener onPageChangeListener;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getY();
                float deltaY = currentY - lastY;
                if (deltaY > 0) {
                    // Scrolling up
                    // Handle scroll up event here
                }
                lastY = currentY;
                break;
        }

        // Check if swipe is enabled
        if (this.swipeEnabled) {
            return super.onTouchEvent(event);
        }
        // If swipe is disabled, return false to indicate that we don't want to consume the event
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Check if swipe is enabled
        if (this.swipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        // If swipe is disabled, return false to indicate that we don't want to intercept the touch event
        return false;
    }

    // Method to enable/disable swipe
    public void setSwipeEnabled(boolean enabled) {
        this.swipeEnabled = enabled;
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        this.onPageChangeListener = listener;
    }
}
