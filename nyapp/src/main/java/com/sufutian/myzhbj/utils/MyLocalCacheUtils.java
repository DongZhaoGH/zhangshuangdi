package com.sufutian.myzhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 三级缓存 之 本地缓存的核心方法  把bitmap压缩成本地图片  bitmap.compress
 */
public class MyLocalCacheUtils {

    private static final String TAG = "MyLocalCacheUtils";
    //文件夹名字getDownloadCacheDirectory()
    public static String DIRNAME= Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/huancun_cache";

   public void setLocalCache(String url,Bitmap bitmap){

       //生成sd卡的缓存目录
       File dir=new File(DIRNAME);
       if(!dir.exists()||!dir.isDirectory()){
           dir.mkdirs();
           Log.d(TAG, "创建了本地缓存文件夹");
       }

       //根据url生成MD5加密的 文件名字
       String fileName = MD5Util.getMD5Password(url);
       File cacheFile=new File(dir,fileName);
       try {
           bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(
                   cacheFile));// 参1:图片格式;参2:压缩比例0-100; 参3:输出流
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
   }

    // 读本地缓存
    public Bitmap getLocalCache(String url) {
        try {
            File cacheFile = new File(DIRNAME, MD5Util.getMD5Password(url));
            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        cacheFile));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
