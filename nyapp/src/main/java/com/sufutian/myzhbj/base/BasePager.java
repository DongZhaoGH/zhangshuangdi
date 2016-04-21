package com.sufutian.myzhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.activity.MainActivity;

/**
 * Created by sufutian on 2016/4/5.
 */
public class BasePager {

    public final Activity mActivity;
    /**
     * 初始化完布局得到当前页面的布局对象
     */
    public  final View mView;
    public FrameLayout fl_content;
    public TextView title;
    public ImageButton btn_menu;
    //组图按钮
    public ImageButton btn_photo;

    public BasePager(Activity activity) {

        mActivity = activity;

        mView = intniView();

    }

    /**
     * 初始化view
     * @return
     */
    public View intniView(){

        View view=View.inflate(mActivity, R.layout.base_pager,null);
        title = (TextView) view.findViewById(R.id.tv_title);
        btn_menu = (ImageButton) view.findViewById(R.id.btn_menu);

        btn_photo = (ImageButton) view.findViewById(R.id.btn_photo);

        btn_menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        //帧布局
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;


    }

    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }

    /**
     * 初始化数据
     */
    public void innitData(){

    }
}
