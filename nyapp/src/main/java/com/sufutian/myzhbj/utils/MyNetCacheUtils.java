package com.sufutian.myzhbj.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 三级缓存之 网络缓存 核心 AsyncTask下载 ,bitmapfactory.options().inSampleSize 压缩
 * Created by sufutian on 2016/4/12.
 */
public class MyNetCacheUtils {


    /**
     * 本地缓存对象
     */
    private final MyLocalCacheUtils myLocalCacheUtils;
    /**
     * 内存缓存对象
     */
    private final MyMemoryCacheUtils myMemoryCacheUtils;

    public MyNetCacheUtils(MyLocalCacheUtils myLocalCacheUtils, MyMemoryCacheUtils myMemoryCacheUtils) {
        this.myLocalCacheUtils = myLocalCacheUtils;
        this.myMemoryCacheUtils = myMemoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
// AsyncTask 异步封装的工具, 可以实现异步请求及主界面更新(对线程池+handler的封装)
        new BitmapTask().execute(imageView, url);// 启动AsyncTask
    }

    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView imageView;
        private String url;

        //正在加载 子线程中
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    imageView.setTag(url);// 打标记,将当前imageview和url绑定在了一起
                }
            }).start();
            Bitmap bitmap = downLoad(url);
            return bitmap;
        }

        //预加载 主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //加载结束 主线程
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                String tag = (String) imageView.getTag();
                if (url.equals(tag)) {
                    imageView.setImageBitmap(bitmap);//给imageview设置标记,防止错乱

                    //写本地缓存
                    myLocalCacheUtils.setLocalCache(url, bitmap);

                    //第一次 写内存缓存
                    myMemoryCacheUtils.setMemoryCache(url, bitmap);
                }
            }


            super.onPostExecute(bitmap);
        }

        //加载进度 主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }


    public Bitmap downLoad(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream stream = conn.getInputStream();
                Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(stream);
                // 下载的时候 压缩
                new BitmapFactory.Options().inSampleSize = 2;
                new BitmapFactory.Options().inJustDecodeBounds = true;
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
