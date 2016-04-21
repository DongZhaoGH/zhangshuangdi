package com.sufutian.myzhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.activity.MainActivity;
import com.sufutian.myzhbj.base.BasePager;
import com.sufutian.myzhbj.base.imlbasepager.GovPager;
import com.sufutian.myzhbj.base.imlbasepager.HomePager;
import com.sufutian.myzhbj.base.imlbasepager.NewsPager;
import com.sufutian.myzhbj.base.imlbasepager.SettingPager;
import com.sufutian.myzhbj.base.imlbasepager.SmartPager;
import com.sufutian.myzhbj.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by sufutian on 2016/4/5.
 *
 * 包含一个RadioGroup 和 viewpager布局
 *
 */
public class ContentFragment extends  BaseFragment {

    private RadioGroup rg_content;

    //五个标签页
    private ArrayList<BasePager> pagerList;
    private NoScrollViewPager mViewPager;

    private ContentAdapter adapter;

    public static final String TAG = "ContentFragment";

    @Override
    public View innitView() {

        View view=View.inflate(mainActivty, R.layout.fragment_content,null);

        //找到radiogroup
        rg_content = (RadioGroup) view.findViewById(R.id.rg_content);
        //找到viewpager
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.noscrollpager);
        return view;
    }


    @Override
    public void inniData() {

        pagerList = new ArrayList<BasePager>();
        pagerList.add(new HomePager(mainActivty));
        pagerList.add(new NewsPager(mainActivty));
        pagerList.add(new SmartPager(mainActivty));
        pagerList.add(new GovPager(mainActivty));
        pagerList.add(new SettingPager(mainActivty));

        //viewpager填充数据
        adapter=new ContentAdapter();
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BasePager basePager = pagerList.get(position);
                basePager.innitData();

                if (position == 0 || position == pagerList.size() - 1) {
                    // 首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    // 其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });

        //按钮点击事件  关联viewpager
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0, false);//false 没有动画
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });



        // 手动加载第一页数据
        pagerList.get(0).innitData();
        // 首页禁用侧边栏
        setSlidingMenuEnable(false);


    }


    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagerList.get(position);
            View view = basePager.mView;//viewpager 里再填充一个布局(标题和一个帧布局)!!!!

            // pager.initData();// 初始化数据, viewpager会默认加载下一个页面,
            // 为了节省流量和性能,不要在此处调用初始化数据的方法  在setOnPageChangeListener中 初始化
             // basePager.innitData();


            container.addView(view);
            System.out.print("填充了布局");
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mainActivty;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//不能使用侧拉菜单
        }
    }

    /**
     * 内容Fragment获取新闻中心内容
     */
    public  NewsPager getNewsPager(){
        NewsPager pager=(NewsPager) pagerList.get(1);
        return pager;
    }


}
