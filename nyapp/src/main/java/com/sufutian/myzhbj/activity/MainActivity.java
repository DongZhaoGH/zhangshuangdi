package com.sufutian.myzhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.fragment.ContentFragment;
import com.sufutian.myzhbj.fragment.LeftMenuFragment;

/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity {

    private static String TAG_LEFT_MENU="TAG_LEFT_MENU";
    private static String TAG_CONTENT="TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //侧拉菜单控件
        setBehindContentView(R.layout.activity_left);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        int width = getWindowManager().getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width*200/320);//屏幕预留200像素宽度

        //初始化fragment
        innitFragment();
    }

    /**
     * 初始化fragment
     */
    private void innitFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //替换布局 打标记
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.fl_home, new ContentFragment(), TAG_CONTENT);
        transaction.commit();
    }

    /**
     * 通过标记 得到侧拉菜单fragment
     */
    public LeftMenuFragment getLeftMenuFragment(){
        LeftMenuFragment fragment= (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(TAG_LEFT_MENU);
        return fragment;

    }

    /**
     * 通过标记 得到content的fragment
     */
    public ContentFragment getContentFragment(){
        ContentFragment fragment= (ContentFragment) getSupportFragmentManager().findFragmentByTag(TAG_CONTENT);
        return fragment;
    }


}
