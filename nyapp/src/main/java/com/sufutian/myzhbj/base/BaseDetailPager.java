package com.sufutian.myzhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by sufutian on 2016/4/6.
 */
public abstract class BaseDetailPager {

    public  final Activity mActivity;
    public final View mRootView;

    public BaseDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = intniView();
    }

    public  abstract View  intniView();

    public abstract  void inniData();
}
