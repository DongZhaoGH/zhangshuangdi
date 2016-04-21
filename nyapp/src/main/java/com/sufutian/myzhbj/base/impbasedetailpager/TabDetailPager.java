package com.sufutian.myzhbj.base.impbasedetailpager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.activity.ListNewsActivity;
import com.sufutian.myzhbj.base.BaseDetailPager;
import com.sufutian.myzhbj.config.LeftMenuPath;
import com.sufutian.myzhbj.customview.NewsListView;
import com.sufutian.myzhbj.domain.NewsMenu;
import com.sufutian.myzhbj.domain.NewsTabBean;
import com.sufutian.myzhbj.utils.CacheUtil;
import com.sufutian.myzhbj.utils.SpUtil;
import com.sufutian.myzhbj.view.TopNewsViewpager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 此页面添加在contentFragment的viewpager的fragment上的  viewpager下面的新闻条目
 * Created by sufutian on 2016/4/6.
 */
public class TabDetailPager extends BaseDetailPager {

    private static final String TAG = "TabDetailPager";
    private ListView lv_list;
    /**
     * 网络传递的数据
     */
    private final NewsMenu.newsTabDetail tabDetail;

    private View view;
    private TopNewsViewpager vp_tab;
    private TopNewsAdapter adapter;
    private final String tabURL;
    private ArrayList<NewsTabBean.TopNews> topnews;
    private TextView tv_title;
    private CirclePageIndicator indicator;
    private NewsListView lv_tab_detail;
    private NewsAdapter newsAdapter;
    private NewsTabBean newsTabBean;
    private ArrayList<NewsTabBean.NewsData> newsList;
    private String mMoreUrl;


    private Handler handler;


    public TabDetailPager(Activity activity, NewsMenu.newsTabDetail newsTabDetail) {
        super(activity);
        tabDetail = newsTabDetail;

        //拿到详情页的url
        String url = tabDetail.url;
        tabURL = LeftMenuPath.SERVIERPATH + url;


    }

    /**
     * 初始化页面
     *
     */
    @Override
    public View intniView() {

        view = View.inflate(mActivity, R.layout.pager_tab_detail, null);

        //listview初始化布局
        View headView = View.inflate(mActivity, R.layout.tab_news_head, null);

        //找到  显示头条新闻的 viewpager
        vp_tab = (TopNewsViewpager) headView.findViewById(R.id.vp_tab_detail);

        //viewpager下面的滚动标题
        tv_title = (TextView) headView.findViewById(R.id.tv_title);

        //圆点指示器
        indicator = (CirclePageIndicator) headView.findViewById(R.id.indicator);

        //列表新闻的listview
        lv_tab_detail = (NewsListView) view.findViewById(R.id.lv_tab_detail);
        //添加头布局
        lv_tab_detail.addHeaderView(headView);

        //回调方法
        lv_tab_detail.setOnStateChangeListener(new NewsListView.onStateChangeListener() {
            @Override
            public void isRefreshing() {
                getDataFromServer(tabURL);//刷新 重新拿网络数据
            }

            @Override
            public void onLoadMore() { //脚布局 加载更多

                if (mMoreUrl != null) {
                    getMoreDataFromServer();//获取下一页数据
                } else {
                    Toast.makeText(mActivity, "没有数据了..", Toast.LENGTH_SHORT).show();

                }
                //隐藏footerview
                lv_tab_detail.hideView(true);
            }
        });

        //listview 的条目点击事件
        lv_tab_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int headViewCount = lv_tab_detail.getHeaderViewsCount();
                position -= headViewCount;//修正position

                //得到当前的列表新闻新闻
                NewsTabBean.NewsData newsData = newsList.get(position);

                String recorded_id = SpUtil.getString(mActivity, "recorded_id", "");
                if (!recorded_id.contains(newsData.id + "")) {//已读
                    recorded_id = recorded_id + newsData.id + ",";
                    SpUtil.setString(mActivity, "recorded_id", recorded_id);
                }

                //找到条目的view 局部刷新   标记为已读
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(Color.BLUE);

                //跳转新闻webview
                Intent intent = new Intent(mActivity, ListNewsActivity.class);
                intent.putExtra("url", newsData.url);
                mActivity.startActivity(intent);


            }
        });


        return view;
    }

    /**
     * 加载更多数据
     */
    public void getMoreDataFromServer() {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, true);

                lv_tab_detail.hideView(true);//加载更多完后控件的状态归为原始状态
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "网络连接失败:" + error, Toast.LENGTH_SHORT).show();
                lv_tab_detail.hideView(false);
            }
        });

    }

    /**
     * 初始化 适配  数据
     */
    @Override
    public void inniData() {
        //拿网络数据 和封装网络数据
        //先判断是否由json缓存 图片缓存bitmap的开源帮我们做了
        String cache = CacheUtil.getCache(mActivity, tabURL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }

        getDataFromServer(tabURL);


    }

    /**
     * 得到网络连接

     */
    private void getDataFromServer(final String tabURL) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, tabURL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, false);
                //设置json缓存
                CacheUtil.setCache(mActivity, result, tabURL);
                lv_tab_detail.hideView(true);//刷新完后 控件的状态归为原始状态
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "网络连接失败:" + error, Toast.LENGTH_SHORT).show();

                lv_tab_detail.hideView(false);
            }
        });

    }

    /**
     * 解析网络数据
     m
     */
    private void processData(String result, boolean isLoadMore) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);

        //加载更多的地址
        String moreUrL = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrL)) {
            mMoreUrl = LeftMenuPath.SERVIERPATH + moreUrL;
        } else {
            mMoreUrl = null;
        }


        if (!isLoadMore) {//刷新头新闻
            //头条新闻集合
            topnews = newsTabBean.data.topnews;
            /**
             *  头条新闻数据适配
             */
            adapter = new TopNewsAdapter();
            if (topnews != null) {
                vp_tab.setAdapter(adapter);
                indicator.setViewPager(vp_tab);
                indicator.setSnap(true);// 快照方式展示

                //事件给指示器
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        NewsTabBean.TopNews topNews = topnews.get(position);
                        tv_title.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                // 手动更新更新第一个头条新闻标题
                tv_title.setText(topnews.get(0).title);
                // 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
                indicator.onPageSelected(0);
            }

            /**
             * 列表新闻
             */
            newsList = newsTabBean.data.news;
            if (newsList != null) {
                newsAdapter = new NewsAdapter();
                lv_tab_detail.setAdapter(newsAdapter);

            }

            if (handler == null) {
                //自动轮播
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        int currentItem = vp_tab.getCurrentItem();
                        currentItem++;
                        if (currentItem > (topnews.size() - 1)) {
                            currentItem = 0;
                        }
                        vp_tab.setCurrentItem(currentItem);
                        handler.sendEmptyMessageDelayed(0, 3000);
                    }
                };
                handler.sendEmptyMessageDelayed(0, 3000);

                //轮播的逻辑处理
                vp_tab.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                // 停止广告自动轮播
                                // 删除handler的所有消息
                                handler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            case MotionEvent.ACTION_CANCEL:// 取消事件,
                                //当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
                                System.out.println("ACTION_CANCEL");
                                //启动广告
                                handler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                handler.sendEmptyMessageDelayed(0, 3000);
                                break;
                        }
                        return false;
                    }
                });

            }


        } else {//加载更多数据
            ArrayList<NewsTabBean.NewsData> news = newsTabBean.data.news;
            newsList.addAll(news);
            newsAdapter.notifyDataSetChanged();
        }


    }


    /**
     * 头条新闻数据适配
     */
    class TopNewsAdapter extends PagerAdapter {
        /**
         * 网络图片加载的开源框架
         */
        private final BitmapUtils bitmapUtils;

        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            //设置默认图片
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);

        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setImageResource(R.drawable.topnews_item_default);
            String topimage = topnews.get(position).topimage;
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            bitmapUtils.display(view, topimage);
            Log.d(TAG, "tabURL" + topimage);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 列表新闻适配器
     */

    class NewsAdapter extends BaseAdapter {
        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            //默认图片
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_liebiao_news,
                        null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            NewsTabBean.NewsData newsData = newsList.get(position);

            String recorded_id = SpUtil.getString(mActivity, "recorded_id", "");

            //判断是否已经读过 显示文字的颜色
            if (recorded_id.contains(newsData.id + "")) {
                holder.tvTitle.setTextColor(Color.BLUE);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }


            holder.tvTitle.setText(newsData.title);
            holder.tvDate.setText(newsData.pubdate);

            mBitmapUtils.display(holder.ivIcon, newsData.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
