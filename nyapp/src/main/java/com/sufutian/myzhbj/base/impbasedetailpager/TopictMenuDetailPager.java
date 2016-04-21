package com.sufutian.myzhbj.base.impbasedetailpager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sufutian.myzhbj.base.BaseDetailPager;

/**
 * Created by sufutian on 2016/4/6.
 */
public class TopictMenuDetailPager extends BaseDetailPager {
    public TopictMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View intniView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-专题");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void inniData() {

    }
}
