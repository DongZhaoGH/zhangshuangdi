package com.sufutian.myzhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sufutian.myzhbj.R;
import com.sufutian.myzhbj.utils.SpUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by sufutian on 2016/4/11.
 */
public class ListNewsActivity extends Activity{

    private WebView mWebView;
    private ProgressBar pb_loading;
    private WebSettings settings;
    private int mSelectSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listnews);

        mSelectSize= SpUtil.getInt(this,"mSelectSize",2);

        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

        //webview
        mWebView = (WebView) findViewById(R.id.wv_news_detail);

        //得到链接
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        //设置
        settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);//缩放按钮
        settings.setUseWideViewPort(true);// 双击缩放
        settings.setJavaScriptEnabled(true);//支持js  必须设置

       changeTextSize(mSelectSize);


        // mWebView.goBack();//跳到上个页面
        // mWebView.goForward();//跳到下个页面

        mWebView.setWebViewClient(new WebViewClient(){
            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载网页了");
                pb_loading.setVisibility(View.VISIBLE);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                pb_loading.setVisibility(View.INVISIBLE);
            }

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                /**
                 *  强制在当前webview加载,在跳转链接时强制在当前webview中加载
                 */
                view.loadUrl(url);
                return true;
            }

        });

        //执行js  处理视图 提示框 标题 加载进度
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //标题
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                //进度
            }
        });
    }



    /**
     * 退出
     * @param view
     */
    public void back(View view ){
        finish();
    }

    /**
     * 分享
     * @param view
     */
    public void share(View view ){

        showShare();


    }

    /**
     * 更改字体
     */
    String[] sizeArray=new String[]{"超大号字体", "大号字体", "正常字体", "小号字体",
            "超小号字体"};
    public  void changeTextSize(View view){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(sizeArray, mSelectSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectSize=which;

            }
        });

        builder.setNegativeButton("取消", null);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                changeTextSize(mSelectSize);
            }
        });
        builder.show();

    }

    /**
     * 改变字体大小
     * @param which
     */
    private void changeTextSize(int which) {
        switch (which){
            case 0:
                settings.setTextSize(WebSettings.TextSize.LARGEST);
                SpUtil.setInt(ListNewsActivity.this,"mSelectSize",0);
                break;
            case 1:
                settings.setTextSize(WebSettings.TextSize.LARGER);
                SpUtil.setInt(ListNewsActivity.this, "mSelectSize", 1);
                break;
            case 2:
                settings.setTextSize(WebSettings.TextSize.NORMAL);
                SpUtil.setInt(ListNewsActivity.this, "mSelectSize", 2);
                break;
            case 3:
                settings.setTextSize(WebSettings.TextSize.SMALLER);
                SpUtil.setInt(ListNewsActivity.this, "mSelectSize", 3);
                break;
            case 4:
                settings.setTextSize(WebSettings.TextSize.SMALLEST);
                SpUtil.setInt(ListNewsActivity.this, "mSelectSize",4);
                break;
        }

    }


    //------------------------------------
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

}
