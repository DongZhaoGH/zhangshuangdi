package com.sufutian.myzhbj.base.imlbasepager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sufutian.myzhbj.activity.MainActivity;
import com.sufutian.myzhbj.base.BaseDetailPager;
import com.sufutian.myzhbj.base.BasePager;
import com.sufutian.myzhbj.base.impbasedetailpager.InteractMenuDetailPager;
import com.sufutian.myzhbj.base.impbasedetailpager.NewsMenuDetailPager;
import com.sufutian.myzhbj.base.impbasedetailpager.PhotosMenuDetailPager;
import com.sufutian.myzhbj.base.impbasedetailpager.TopictMenuDetailPager;
import com.sufutian.myzhbj.config.LeftMenuPath;
import com.sufutian.myzhbj.domain.NewsMenu;
import com.sufutian.myzhbj.fragment.LeftMenuFragment;
import com.sufutian.myzhbj.utils.CacheUtil;

import java.util.ArrayList;

/**
 *
 * newspager 填充在contentfragment的viewpager上
 * nwespage 由 一个相对布局的标题  和  一个fragment组成
 * Created by sufutian on 2016/4/5.
 */
public class NewsPager extends BasePager{

    /**
     * 解析的json数据
     */
    private NewsMenu newsMenu;
    private ArrayList<BaseDetailPager> detailPagerArrayList;

    public NewsPager(Activity activity) {
        super(activity);

    }

    @Override
    public void innitData() {

        title.setText("新闻中心");

        // 隐藏菜单按钮
        btn_menu.setVisibility(View.VISIBLE);

        // 要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        fl_content.addView(view);


        //请求服务区 拿到json数据
        //先判断是否有缓存
        String cache = CacheUtil.getCache(mActivity, LeftMenuPath.MENUPATH);
        if(!TextUtils.isEmpty(cache)){
            processData(cache);
        }

        getDataFromServer();

    }

    /**
     *  //请求服务区 拿到json数据
     * @return
     */
    public void  getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, LeftMenuPath.MENUPATH, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //解析数据
                processData(result);
                //写缓存
                CacheUtil.setCache(mActivity,result,LeftMenuPath.MENUPATH);
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /**
     * 解析数据
     */
    private void processData(String json) {
        Gson gson = new Gson();
        newsMenu = gson.fromJson(json, NewsMenu.class);
        //同过mainactivity中 拿到侧拉菜单的对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenu = mainUI.getLeftMenuFragment();
        leftMenu.setMenuData(newsMenu.data);//把data传递给侧拉菜单
        //如此同时  初始化四个菜单详情页
        detailPagerArrayList = new ArrayList<>();
        //通过构造传递网络数据
        detailPagerArrayList.add(new NewsMenuDetailPager(mActivity,newsMenu.data.get(0).children));
        detailPagerArrayList.add(new TopictMenuDetailPager(mActivity));
        detailPagerArrayList.add(new PhotosMenuDetailPager(mActivity,btn_photo));
        detailPagerArrayList.add(new InteractMenuDetailPager(mActivity));
        // 将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }
    /**
     * 设置菜单详情页
     * @param i
     */
    public void setCurrentDetailPager(int i) {
        BaseDetailPager pager = detailPagerArrayList.get(i);// 获取当前应该显示的页面
        View view = pager.mRootView;// 当前页面的布局
        //帧布局移除之前的所有布局 重新添加详情页布局
        fl_content.removeAllViews();
        fl_content.addView(view);// 给帧布局添加布局
        //pager.intniView();  不用调用 构造方法已经调用了
        pager.inniData();
        title.setText(newsMenu.data.get(i).title);
        // 如果是组图页面, 需要显示切换按钮
        if (pager instanceof PhotosMenuDetailPager) {
            btn_photo.setVisibility(View.VISIBLE);
        } else {
            // 隐藏切换按钮
            btn_photo.setVisibility(View.GONE);
        }
    }
}
