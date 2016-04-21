package com.sufutian.myzhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.utils.ScreenAdapterUtils;
import com.sufutian.myzhbj.utils.SpUtil;

import java.util.ArrayList;

/**
 * Created by sufutian on 2016/4/4.
 */
public class GuideActivity extends Activity{

    private ViewPager mViewPager;

    private int[] imageID={
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3
    };

    private ArrayList<ImageView> imageList=new ArrayList<>();
    private LinearLayout ll_counter;
    private myAdapter adapter;
    private ImageView redPoint;

    //红点距离灰点的左边距
    private int mLeftDis;
    private Button btn_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        ll_counter = (LinearLayout) findViewById(R.id.ll_guide);
        redPoint = (ImageView) findViewById(R.id.iv_point_guide);
        btn_guide = (Button) findViewById(R.id.btn_guide);

        //点击完进入主界面
        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                //设置不是第一次进入
                SpUtil.setBoolean(getApplication(),"isFirstLoad",false);
                finish();
            }
        });
        inniDate();
        inniView();
    }

    private void inniDate() {

        for (int i=0;i<imageID.length;i++){
            ImageView view=new ImageView(getApplication());
            view.setBackgroundResource(imageID[i]);
            imageList.add(view);

            //初始化小圆点
            ImageView grayPoint =new ImageView(this);
            grayPoint.setImageResource(R.drawable.guide_gray_point_shape);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if(i>0){  //隔10像素

               int px = ScreenAdapterUtils.dp2px(this, 10);
                params.leftMargin=px;
            }

            grayPoint.setLayoutParams(params);
            ll_counter.addView(grayPoint);


        }



    }

    private void inniView() {

        //填充viewpager
        adapter=new myAdapter();
        mViewPager.setAdapter(adapter);

        //viewpager滑动事件 来滑动红色点
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动时
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin= (int) (positionOffset* mLeftDis)+position* mLeftDis;

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;// 修改左边距
                // 重新设置布局参数
                redPoint.setLayoutParams(params);
            }
            @Override
            public void onPageSelected(int position) {

                //
                if(position==imageList.size()-1){
                    btn_guide.setVisibility(View.VISIBLE);
                }else{
                    btn_guide.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //获取圆点间的距离
        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 移除监听,避免重复回调
                redPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // layout方法执行结束的回调   两个灰色点之间的左边距离
                mLeftDis = ll_counter.getChildAt(1).getLeft() - ll_counter.getChildAt(0).getLeft();
            }
        });


    }

    private class myAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView iv=imageList.get(position);
            container.addView(iv);
            return iv;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
