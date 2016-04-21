package com.sufutian.myzhbj.base.impbasedetailpager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.activity.MainActivity;
import com.sufutian.myzhbj.base.BaseDetailPager;
import com.sufutian.myzhbj.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 此页面添加在contentFragment的viewpager的fragment上
 * 此页面由 一个viewpager组成
 * Created by sufutian on 2016/4/6.
 */
public class NewsMenuDetailPager extends BaseDetailPager {

    private ViewPager vp_news_detail;

    private NewsMenuAdapter adapter;

    //页签网络数据
    private final ArrayList<NewsMenu.newsTabDetail> mChildren;
    /**
     * 页签页面对象的集合
     */
    private ArrayList<TabDetailPager> mPager;
    private TabPageIndicator indicator;


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.newsTabDetail> children) {
        super(activity);
        mChildren = children;
    }

    @Override
    public View intniView() {

       /* TextView view = new TextView(mActivity);
        view.setText("菜单详情页-新闻详情");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);*/

        View view=View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
        // viewpager
        vp_news_detail = (ViewPager) view.findViewById(R.id.vp_news_detail);
        // 找到指示器
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);


        //点击下一个标签
        ImageButton btn_next= (ImageButton) view.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vp_news_detail.getCurrentItem();
                currentItem++;
                vp_news_detail.setCurrentItem(currentItem);
            }
        });

        return view;
    }

    @Override
    public void inniData() {

        //初始化页签
        mPager = new ArrayList<TabDetailPager>();
        for (int i=0;i< mChildren.size();i++){
            //构造传递数据
            TabDetailPager pager = new TabDetailPager(mActivity,
                    mChildren.get(i));
            mPager.add(pager);

        }

        adapter=new NewsMenuAdapter();
        vp_news_detail.setAdapter(adapter);
        //指示器和viewpager关联
        indicator.setViewPager(vp_news_detail);


        // indicator设置监听 只有当显示第一个标题时(北京时)才可以打开侧拉菜单
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    if(position==0){
                        setSlidingMenuEnable(true);
                    }else{
                        setSlidingMenuEnable(false);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }



    /**
     * viewpager的数据适配器
     */
    class NewsMenuAdapter extends PagerAdapter{

        //制定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.newsTabDetail tabDetail = mChildren.get(position);
            return tabDetail.title;
        }

        @Override
        public int getCount() {
            return mPager.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public NewsMenuAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPager.get(position);
            View view = pager.mRootView;

            //初始化页签页面详情页的数据
            pager.inniData();
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }




    /**
     * 开启或禁用侧边栏
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
