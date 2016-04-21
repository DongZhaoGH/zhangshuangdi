package com.sufutian.myzhbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.sufutian.myzhbj.R;

public class MyBitmapUtils {

    private static final String TAG ="MyBitmapUtils";
    /**
     * 网络缓存
     */
    private final MyNetCacheUtils myNetCacheUtils;
    /**
     * 本地缓存
     */
    private final MyLocalCacheUtils myLocalCacheUtils;
    /**
     * 内存缓存
     */
    private final MyMemoryCacheUtils myMemoryCacheUtils;

    public MyBitmapUtils() {

        myLocalCacheUtils = new MyLocalCacheUtils();
        myMemoryCacheUtils = new MyMemoryCacheUtils();

        myNetCacheUtils = new MyNetCacheUtils(myLocalCacheUtils,myMemoryCacheUtils);
    }

    public  void downLoadBitmap(ImageView imageview, String url){
        // 设置默认图片
        imageview.setImageResource(R.drawable.pic_item_list_default);

        //如果有内存缓存 结束
        Bitmap memoryCache = myMemoryCacheUtils.getMemoryCache(url);
        if(memoryCache!=null){
            imageview.setImageBitmap(memoryCache);
            Log.d(TAG, "内存加载了图片");
            return;
        }

        //如果有本地缓存 不加载网络
        Bitmap bitmap = myLocalCacheUtils.getLocalCache(url);
        if(bitmap!=null){
            imageview.setImageBitmap(bitmap);
            Log.d(TAG, "本地加载了图片");
            //第二次写内存缓存
            myMemoryCacheUtils.setMemoryCache(url, bitmap);
            return;
        }
        //网络 下载 及 缓存
        myNetCacheUtils.getBitmapFromNet(imageview, url);
        Log.d(TAG, "网络加载了图片");
    }
}
