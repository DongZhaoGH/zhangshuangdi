package com.sufutian.myzhbj.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 *三级缓存之 LruCache 底部linkedhashmap
 * LruCache<String, Bitmap>((int) maxMemory/8)
  设置内存的最大值 回收 最近最少使用 算法
 */
public class MyMemoryCacheUtils {

    private static final String TAG = "MyMemoryCacheUtils";
    private LruCache<String, Bitmap> mMemoryCache;

    public MyMemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();//分配给app的内存大小
        mMemoryCache=new LruCache<String, Bitmap>((int) maxMemory/2){

            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getRowBytes() * value.getHeight();// 计算图片大小:每行字节数*高度
                return byteCount;

            }
        };
    }


    //写内存缓存
    public void setMemoryCache(String url,Bitmap bitmap){
        mMemoryCache.put(url,bitmap);
    }

    //读内存缓存
    public Bitmap getMemoryCache(String url){
        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;
    }
}
