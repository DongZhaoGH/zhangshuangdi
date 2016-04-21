package com.sufutian.myzhbj.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sufutian.myzhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义列表新闻的 listview 再添加一个刷新的头布局
 * Created by sufutian on 2016/4/8.
 */
public class NewsListView extends ListView {

    private static final String TAG ="NewsListView" ;
    private ImageView iv_arrow;
    private ProgressBar pb_refresh;
    private TextView tv_pullrefresh;
    private TextView tv_refreshtime;
    private int mHeight;
    private int startY=-1;
    private View headView;

    /**
     * 声明三种状态
     */

    public static final int PULL_REFRESH=0;    //下拉刷新
    public static final int RELEASE_REFRESH=1;//释放刷新
    public static final int REFRESHING=2;    //正在刷新

    public static int mCurrentState=PULL_REFRESH;
    private RotateAnimation upAnim;
    private RotateAnimation downAnim;
    private int paddingTop;
    private int mFooterHeight;
    private View footerView;

    private boolean isLoadMore=false;

    public NewsListView(Context context) {
        this(context, null);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        innitView();
        innitAnimation();
        innitFooterView();

    }


    /**
     * 初始化刷新头布局
     */
    private void innitView() {
        headView = View.inflate(getContext(), R.layout.refresh_head_tabdetail, null);

        //找到控件
        iv_arrow = (ImageView) headView.findViewById(R.id.iv_arrow);
        pb_refresh = (ProgressBar) headView.findViewById(R.id.pb_refresh);
        tv_pullrefresh = (TextView) headView.findViewById(R.id.tv_pullrefresh);
        tv_refreshtime = (TextView) headView.findViewById(R.id.tv_refreshtime);
        //添加头布局
        this.addHeaderView(headView);

        headView.measure(0, 0);
        mHeight = headView.getMeasuredHeight();//隐藏头布局
        headView.setPadding(0, -mHeight, 0, 0);

        setCurrentTime();//设置时间


        /**
         * 滑动监听显示脚布局
         */
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE&&!isLoadMore) {//没有滑动了 没有正在加载 才加载 避免重复加载
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition == (getCount() - 1)){//是最后一个 条目
                        footerView.setPadding(0, 0, 0, 0);
                        isLoadMore=true;
                        setSelection(getCount() - 1);
                        //回调
                        if(mOnStateChangeListener!=null){
                            mOnStateChangeListener.onLoadMore();
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY= (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if(startY==-1){// 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
                                // 导致startY没有赋值,此处需要重新获取一下
                    startY= (int) ev.getY();
                }
                int endY= (int) ev.getY();
                int dY=endY-startY;
                paddingTop = dY-mHeight;
                int firstVisiblePosition = getFirstVisiblePosition();
                if(firstVisiblePosition==0&&dY>0){

                    if(mCurrentState!=RELEASE_REFRESH&& paddingTop >0){
                        mCurrentState=RELEASE_REFRESH;
                        refreshState(mCurrentState);

                    }else if(paddingTop <0&&mCurrentState!=PULL_REFRESH){
                        mCurrentState=PULL_REFRESH;
                        refreshState(mCurrentState);

                    }

                    headView.setPadding(0, paddingTop,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY=-1;
                if(mCurrentState==RELEASE_REFRESH){
                    mCurrentState=REFRESHING;//若果当前是 释放刷新 抬起后显示正在刷新

                    refreshState(mCurrentState);
                    headView.setPadding(0, 0,0,0);

                    //回调状态
                    if(mOnStateChangeListener!=null){
                        mOnStateChangeListener.isRefreshing();
                    }
                }else if(mCurrentState==PULL_REFRESH){//当前不是释放刷新
                    headView.setPadding(0, -mHeight,0,0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 根据当前状态来刷新
     * @param mCurrentState
     */
    public  void refreshState(int mCurrentState) {

        switch (mCurrentState){
            case PULL_REFRESH:

                tv_pullrefresh.setText("下拉刷新");

                iv_arrow.setVisibility(View.VISIBLE);
                pb_refresh.setVisibility(View.INVISIBLE);

                iv_arrow.startAnimation(downAnim);
                break;
            case RELEASE_REFRESH:
                tv_pullrefresh.setText("松开刷新");
                iv_arrow.setVisibility(View.VISIBLE);
                pb_refresh.setVisibility(View.INVISIBLE);
                iv_arrow.startAnimation(upAnim);
                break;
            case REFRESHING:
                tv_pullrefresh.setText("正在刷新");
                iv_arrow.clearAnimation();// 清除箭头动画,否则无法隐藏
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_refresh.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 箭头动画
     *
     */
    public void innitAnimation(){

        upAnim = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        upAnim.setDuration(300);
        upAnim.setFillAfter(true);

        downAnim = new RotateAnimation(-180,-360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        downAnim.setDuration(300);
        downAnim.setFillAfter(true);
    }

    /**
     * 隐藏头布局的方法  当网络数据加载完后调用
     * 控件的状态归为原始状态
     * @param
     */
    public void hideView(boolean success){
        if(!isLoadMore){
            //隐藏头布局
            headView.setPadding(0, -mHeight, 0, 0);
            mCurrentState=PULL_REFRESH;
            tv_pullrefresh.setText("下拉刷新");
            pb_refresh.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);

            if(success){
                setCurrentTime();
            }
        }else{
            //隐藏脚布局
            footerView.setPadding(0,-mFooterHeight,0,0);
            isLoadMore=false;
        }
    }

    /**
     * 设置时间的方法
     */

    public void setCurrentTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());

        tv_refreshtime.setText(time);

    }

    /**
     * 定义接口回调状态
     */
    private onStateChangeListener mOnStateChangeListener;
    //定义接口
    public interface onStateChangeListener {
       public  void isRefreshing(); //回调方法
       public void onLoadMore();  //加载更多
    }
    //暴露接口方法
    public void setOnStateChangeListener(onStateChangeListener listener){
        mOnStateChangeListener=listener;
    }


    /**
     * 初始化脚布局
     */
    private void innitFooterView() {

        footerView = View.inflate(getContext(), R.layout.footerview_listview, null);
        footerView.measure(0, 0);
        mFooterHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -mFooterHeight, 0, 0);
        this.addFooterView(footerView);

    }




}
