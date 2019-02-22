package cn.bfy.frame.ui.widget.webview;

import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import cn.bfy.frame.ui.fragment.BaseWebFragment;
import cn.bfy.frame.util.LogUtils;

/**
 * Description: 描述 </br>
 * Copyright: Copyright (c) 2017 </br>
 * Company:XXXXXXXXXXXXXXXXXXXX </br>
 * Email:284425176@qq.com </br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public class RodoWebViewClient extends WebViewClient {
    private static final String TAG = RodoWebViewClient.class.getSimpleName();

    private BaseWebFragment mFragment;
    private List<Intercept> mIntercepts;

    /**
     *
     * @param mFragment
     * @param mWebView
     * @param mIntercepts 需要拦截的url集合
     */
    public RodoWebViewClient(BaseWebFragment mFragment, WebView mWebView, List<Intercept> mIntercepts) {
        this.mFragment = mFragment;
        this.mIntercepts = mIntercepts;
    }


//    @Override

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // btn_scan111.setVisibility(View.GONE);
        LogUtils.i(TAG, "shouldOverrideUrlLoading url: " + url);
        if (mIntercepts != null){
            for (Intercept intercept : mIntercepts) {
                if (!TextUtils.isEmpty(intercept.getRegex()) && url.matches(intercept.getRegex())
                        || url.equals(intercept.getUrl())) {
                    boolean result = intercept.getCallback().onCallback(mFragment.getContext(), view, url);
                    LogUtils.i(TAG, String.format("shouldOverrideUrlLoading url : %s intercept  return = %s ", url, result));
                    return result;
                }
            }
        }
        //view.loadUrl(url);
        return false;
    }
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        String url = request.getUrl().toString();
//        return super.shouldOverrideUrlLoading(view, request);
//    }

    @Override
    public void onPageStarted(WebView view, String url,
                              android.graphics.Bitmap favicon) {
//        view.getSettings().setLoadsImagesAutomatically(false);
//        view.getSettings().setBlockNetworkImage(true);
        if (mFragment != null)
            mFragment.onPageStarted(view,url);
    }



    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
//        mActivity.cleanWebViewCache();
//        switch (errorCode){
//            case 404:
//                description = "资源找不到了";
//                break;
//            case 401:
//                //未授权,可能未登录
//                break;
//            case 403:
//                //服务器拒绝请求
//                break;
//        }
        LogUtils.i(TAG, "onReceivedError1 errorCode: " + errorCode);
        if (mFragment != null)
//            mFragment.showMess(description);
            mFragment.onError();
//        if (view.canGoBack()){
//            view.goBack();
//        }
    }

//    @Override
//    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//        super.onReceivedError(view, request, error);
//    }

    @Override
    public void onPageFinished(WebView view, String url) {
        //更新缓存的cookie,目前暂时未有账户系统,登录token等
//        CookieManager cookieManager = CookieManager.getInstance();
//        String CookieStr = cookieManager.getCookie(url);
//        LogUtils.i(TAG, "pageEnd url: " + url);
//        LogUtils.i(TAG, "pageEnd cookie: " + CookieStr);
        /*if (!view.getSettings().getLoadsImagesAutomatically()){
            view.getSettings().setLoadsImagesAutomatically(true);
        }*/
        if (mFragment != null)
            mFragment.onPageFinished(view,url);

    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //handler.cancel(); 默认的处理方式，WebView变成空白页
        //接受证书
        handler.proceed();
        //handleMessage(Message msg); 其他处理
    }








}
