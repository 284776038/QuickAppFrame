package cn.bfy.frame.ui.widget.webview;

import android.support.annotation.NonNull;

import cn.bfy.frame.interfaces.InterceptUrlCallback;

/**
 * Description: 拦截url,并回调操作 </br>
 * Copyright: Copyright (c) 2017 </br>
 * Company:XXXXXXXXXXXXXXXXXXXX </br>
 * Email:284425176@qq.com </br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public class Intercept {

    private String url;

    private InterceptUrlCallback callback;

    private String regex;

    public Intercept(@NonNull  String url, @NonNull InterceptUrlCallback interceptCallback) {
        this.url = url;
        this.callback = interceptCallback;
    }

    public Intercept(@NonNull InterceptUrlCallback interceptCallback, @NonNull String regex) {
        this.regex = regex;
        this.callback = interceptCallback;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCallback(InterceptUrlCallback callback) {
        this.callback = callback;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public InterceptUrlCallback getCallback() {
        return callback;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
