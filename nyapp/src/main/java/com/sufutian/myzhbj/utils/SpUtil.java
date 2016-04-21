package com.sufutian.myzhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by sufutian on 2016/4/4.
 */
public class SpUtil {

    /**
     * 封装 sp boolean
     * @param context
     * @param name
     * @param value
     */
    public  static void setBoolean(Context context,String name,boolean value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name,value);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String name,boolean value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        return sp.getBoolean(name,value);
    }


    /**
     * 封装sp string
     * @param context
     * @param name
     * @param value
     */
    public  static void setString(Context context,String name,String value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String  getString(Context context,String name,String value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        return sp.getString(name, value);
    }


    /**
     * 封装sp int
     * @param context
     * @param name
     * @param value
     */
    public  static void setInt(Context context,String name, int value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static int  getInt(Context context,String name,int value){
        SharedPreferences sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
        return sp.getInt(name, value);
    }


}
