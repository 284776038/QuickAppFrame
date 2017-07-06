package cn.richinfo.frame.interfaces;

import android.content.Context;
import android.webkit.WebView;

/**
 * Description: 拦截url用回调 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:深圳彩讯科技有限公司 <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public interface InterceptUrlCallback {

    /**
     *
     * @param context
     * @param webView
     * @return True if the host application wants to leave the current WebView
     *         and handle the url itself, otherwise return false.
     */
    boolean onCallback(Context context, WebView webView, String url);
}
