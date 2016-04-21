package com.sufutian.myzhbj.utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * 以json为对象  以url为key
 * Created by sufutian on 2016/4/6.
 */
public class CacheUtil {

    /**
     * 设置缓存
     * @param context
     * @param json
     * @param url
     */
    public static void setCache(Context context,String json,String url){

        SpUtil.setString(context,url,json);
    }

    /**
     * 获取缓存
     */
    public static  String getCache(Context context,String url){

        return SpUtil.getString(context,url,null);


    }

}
