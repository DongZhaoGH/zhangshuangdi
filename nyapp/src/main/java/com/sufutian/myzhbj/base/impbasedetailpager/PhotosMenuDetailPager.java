package com.sufutian.myzhbj.base.impbasedetailpager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.sufutian.myzhbj.base.BaseDetailPager;
import com.sufutian.myzhbj.config.LeftMenuPath;
import com.sufutian.myzhbj.domain.PhotosBean;
import com.sufutian.myzhbj.utils.CacheUtil;
import com.sufutian.myzhbj.utils.MyBitmapUtils;

import java.util.ArrayList;

/**
 * Created by sufutian on 2016/4/6.
 */



public class PhotosMenuDetailPager extends BaseDetailPager implements View.OnClickListener{
    //组图按钮
    private ImageButton btn;
    private ListView lv_photo;
    private GridView gv_photo;
    private ArrayList<PhotosBean.PhotoNews> news;

    public PhotosMenuDetailPager(Activity activity, ImageButton btn_photo) {
        super(activity);
        btn=btn_photo;
       // btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(this);

    }

    @Override
    public View intniView() {
      View view=View.inflate(mActivity, R.layout.photodetailpager, null);
        lv_photo = (ListView) view.findViewById(R.id.lv_photo);
        gv_photo = (GridView) view.findViewById(R.id.gv_photo);

        return view;
    }

    @Override
    public void inniData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, LeftMenuPath.PHOTOS_URL,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        processData(result);

                        CacheUtil.setCache(mActivity, result, LeftMenuPath.PHOTOS_URL);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // 请求失败
                        error.printStackTrace();
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    protected void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

        news = photosBean.data.news;


        lv_photo.setAdapter(new PhotoAdapter());
        gv_photo.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致,
        // 所以可以共用一个adapter
    }




    /**
     * listview 适配器
     */
    private class PhotoAdapter extends BaseAdapter {

        private final BitmapUtils bitmapUtils;

        public PhotoAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView= View.inflate(mActivity,R.layout.item_photonews,null);
                holder=new ViewHolder();
                holder.iv_pic= (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            PhotosBean.PhotoNews photoNews = getItem(position);
            holder.tv_title.setText(photoNews.title);

            //加载动画
            bitmapUtils.display(holder.iv_pic,photoNews.listimage);
           // utils.downLoadBitmap(holder.iv_pic, photoNews.listimage);
            return convertView;
        }
    }
    private MyBitmapUtils utils=new MyBitmapUtils();


    class ViewHolder{
        ImageView iv_pic;
        TextView tv_title;
    }


    private boolean isListView = true;// 标记当前是否是listview展示
    @Override
    public void onClick(View v) {
        if (isListView) {
            // 切成gridview
            lv_photo.setVisibility(View.GONE);
            gv_photo.setVisibility(View.VISIBLE);
            btn.setImageResource(R.drawable.icon_pic_list_type);

            isListView = false;
        } else {
            // 切成listview
            lv_photo.setVisibility(View.VISIBLE);
            gv_photo.setVisibility(View.GONE);
            btn.setImageResource(R.drawable.icon_pic_grid_type);
            isListView = true;
        }
    }



}
