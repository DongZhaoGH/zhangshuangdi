package com.sufutian.myzhbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.utils.SpUtil;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_splash_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        rl_splash_bg = (RelativeLayout) findViewById(R.id.rl_splash_bg);


        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        //旋转
        RotateAnimation rotateAnimation=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        //缩放
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1.0f,0,1.0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        /**
         * @params boolean false用各个动画自己的插值器
         * true用动画集合的插值器
         *  //动画集合
         */
        AnimationSet animationSet=new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);

        rl_splash_bg.startAnimation(animationSet);

       //设置动画的监听
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent;
                //判断是否第一次进入
                if(SpUtil.getBoolean(getApplication(),"isFirstLoad",true)){//是第一次
                    //进入设置界面
                    intent=new Intent(getApplication(),GuideActivity.class);

                }else{//不是
                    intent=new Intent(getApplication(),MainActivity.class);
                }
                startActivity(intent);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


}
