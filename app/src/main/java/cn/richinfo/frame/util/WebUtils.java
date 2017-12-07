package cn.richinfo.frame.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Description: WebView设置工具类 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public class WebUtils {
    private static final String TAG = WebUtils.class.getSimpleName();

    public static void initSetting(Context context, WebView webView){
        final String filesDir = context.getFilesDir().getPath();
        final String databaseDir = filesDir.substring(0, filesDir.lastIndexOf("/")) + Constant.DATABASES_SUB_FOLDER;

        final WebSettings settings = webView.getSettings();
        //NORMAL：正常显示，没有渲染变化。
        //SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。
        //NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        String userAgent = settings.getUserAgentString();
        Log.i(TAG, "userAgent: " + userAgent);
//        设置WebView代理字符串，如果String为null或为空，将使用系统默认值
        settings.setUserAgentString(userAgent + Constant.WEBVIEW_UA);

        String userAgent2 = settings.getUserAgentString();
        Log.i(TAG, "userAgent2: " + userAgent2);

        settings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT < 18) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        /*settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < 19) {
            settings.setDatabasePath(databaseDir);
        }*/
        setMixedContentAllowed(settings, true);

        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        // 允许DCOM，webview加载地图必须添加，否则地图会出现空白
        settings.setDomStorageEnabled(true);
        // 设置可以自动加载图片
        settings.setLoadsImagesAutomatically(true);
        // 控制滚动条位置
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 设置缓存模式
        settings.setAppCacheEnabled(true);
//        settings.setAppCacheEnabled(false);


//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 隐藏滚动条
        webView.setHorizontalScrollBarEnabled(false);
        // 启用页面的缩放
        settings.setSupportZoom(true);
        settings.setSupportMultipleWindows(true);

//        webView.addJavascriptInterface(new JavaWebFace(this)
//                , "newWindowInterface");
    }

    /**
     * 设置加载不安全资源的WebView加载行为。KITKAT版本以及以下默认为MIXED_CONTENT_ALWAYS_ALLOW方
     * 式，LOLLIPOP默认MIXED_CONTENT_NEVER_ALLOW。
     */
    protected static void setMixedContentAllowed(final WebSettings webSettings, final boolean allowed) {
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(allowed ? WebSettings.MIXED_CONTENT_ALWAYS_ALLOW : WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
    }
}
