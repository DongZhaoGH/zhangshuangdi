package com.sufutian.myzhbj.utils;

import android.content.Context;

/**
 * Created by sufutian on 2016/4/12.
 */
public class ScreenAdapterUtils {

    public static int dp2px(Context context, float dp) {

        float densityDpi = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * densityDpi + 0.5f);
        return px;

    }


    public static float px2dp(Context context, int px) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        float dp = px / densityDpi;
        return dp;
    }
}
