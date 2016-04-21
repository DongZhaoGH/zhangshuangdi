package com.sufutian.myzhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sufutian on 2016/4/7.
 */
public class TopNewsViewpager  extends ViewPager{


    private int startX;
    private int startY;
    public TopNewsViewpager(Context context) {
        super(context);
    }

    public TopNewsViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(true);//要求父控件不拦截

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dy) < Math.abs(dx)) {
                    int currentItem = getCurrentItem();
                    // 左右滑动
                    if (dx > 0) {
                        // 向右划
                        if (currentItem == 0) {
                            // 第一个页面,需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        // 向左划
                        int count = getAdapter().getCount();// item总数
                        if (currentItem == count - 1) {
                            // 最后一个页面,需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    // 上下滑动,需要拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
