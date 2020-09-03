package com.range.venus.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import com.google.android.material.tabs.TabLayout;


public class CenterFocusTabLayout extends TabLayout {

    ViewGroup parent = (ViewGroup)getChildAt(0);
    Runnable scrollStoppedTask;
    OnScrollStoppedListener listener;
    private int initialPosition;
    private int newCheck = 0;

    public CenterFocusTabLayout(Context context) {
        super(context);
        initScrollActivity();

    }

    public CenterFocusTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScrollActivity();
    }

    public CenterFocusTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollActivity();
    }

    public void startScrollerTask(){
        initialPosition = getScrollX();
        this.postDelayed(scrollStoppedTask, newCheck);
    }

    void initScrollActivity(){
        scrollStoppedTask = new Runnable() {
            @Override
            public void run() {

                if(initialPosition == getScrollX()) {
                    if (listener != null)
                        listener.onScrollStopped();
                }
                else {
                    initialPosition = getScrollX();
                    CenterFocusTabLayout.this.postDelayed(scrollStoppedTask, newCheck);
                }
            }
        };
        listener = new OnScrollStoppedListener() {
            @Override
            public void onScrollStopped() {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    Tab tab = getTabAt(i);
                    if (getScrollX() >= getChildLeft(i) && (i == parent.getChildCount() - 1 || getScrollX() < getChildLeft(i + 1))) {
                        Log.d("scroll changed", String.valueOf(getScrollX()));
                        if (tab != null) {
                            if (!tab.isSelected()) {
                                tab.select();
                            }
                        }
                        break;
                    }
                }

            }
        };
    }

    int getChildLeft(int position){

        if(position == 1){
            return parent.getChildAt(0).getWidth()/2;
        }
        else if(position == 0)
            return 0;
        return parent.getChildAt(position-1).getWidth() + getChildLeft(position - 1);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

                super.onScrollChanged(l, t, oldl, oldt);

    }

    public interface OnScrollStoppedListener{
        void onScrollStopped();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

            startScrollerTask();
        return super.onTouchEvent(ev);
    }


    @Override
    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        super.setScrollPosition(position, positionOffset, updateSelectedText);
    }


    @Override
    public boolean performClick() {
        Log.d("perform click", "here");
        return super.performClick();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View firstTab = ((ViewGroup)getChildAt(0)).getChildAt(0);
        View lastTab = ((ViewGroup)getChildAt(0)).getChildAt(((ViewGroup)getChildAt(0)).getChildCount()-1);
        ViewCompat.setPaddingRelative(getChildAt(0), (getWidth()/2) - (firstTab.getWidth()/2),0,(getWidth()/2) - (lastTab.getWidth()/2),0);

    }


}
