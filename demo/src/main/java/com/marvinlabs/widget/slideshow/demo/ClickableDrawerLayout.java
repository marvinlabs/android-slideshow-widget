package com.marvinlabs.widget.slideshow.demo;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

/**
 * Created by Arasthel on 02/06/14.
 */
public class ClickableDrawerLayout extends DrawerLayout {

    public ClickableDrawerLayout(Context context) {
        super(context);
    }

    public ClickableDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(getDrawerLockMode(Gravity.LEFT) != LOCK_MODE_UNLOCKED) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
