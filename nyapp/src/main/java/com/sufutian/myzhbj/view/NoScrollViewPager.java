package com.sufutian.myzhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sufutian on 2016/4/5.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);

    }


    // 事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;// 不拦截子控件的事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //禁止了触摸事件
        return true;
    }
}
