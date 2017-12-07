package cn.richinfo.frame.view;

import android.webkit.WebView;

/**
 * Description: 描述 </br>
 * Copyright: Copyright (c) 2017 </br>
 * Company:XXXXXXXXXXXXXXXXXXXX </br>
 * Email:284425176@qq.com </br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public interface BaseWebView {

    void onPageStarted(WebView webView, String url);

    void onPageFinished(WebView webView, String url);

    void onProgressChange(WebView webView, int progress);

    void showMess(String mess);

    void onError();

    void onRefresh();

}
