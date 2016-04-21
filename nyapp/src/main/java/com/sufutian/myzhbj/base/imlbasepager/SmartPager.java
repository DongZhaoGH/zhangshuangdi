package com.sufutian.myzhbj.base.imlbasepager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sufutian.myzhbj.base.BasePager;

public class SmartPager extends BasePager{
    public SmartPager(Activity activity) {
        super(activity);
    }

    @Override
    public void innitData() {

        title.setText("智慧服务");
        // 隐藏菜单按钮
        btn_menu.setVisibility(View.VISIBLE);


        // 要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("智慧服务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        fl_content.addView(view);

    }
}
