package cn.richinfo.frame.ui.widget.webview;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cn.richinfo.frame.ui.fragment.BaseWebFragment;
import cn.richinfo.frame.util.LogUtils;


/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public class RodoWebChromeClient extends WebChromeClient {
    private static final String TAG = RodoWebChromeClient.class.getSimpleName();
    private BaseWebFragment mFragment;

    public RodoWebChromeClient(BaseWebFragment mFragment,WebView mailWebView) {
        super();
        this.mFragment = mFragment;

    }

    // file upload callback (Android 2.2 (API level 8) -- Android 2.3 (API level 10)) (hidden method)
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg)
    {
        LogUtils.i(TAG, "openFileChooser 2.2");
        openFileChooser(uploadMsg, null);
    }

    // file upload callback (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
    {
        LogUtils.i(TAG, "openFileChooser 3.0");
        openFileChooser(uploadMsg, acceptType, null);
    }

    // file upload callback (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
    {
        LogUtils.i(TAG, "openFileChooser 4.1");
        openFileInput(uploadMsg, null);

    }

    // file upload callback (Android 5.0 (API level 21) -- current) (public method)
    @SuppressWarnings("all")
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
    {
        openFileInput(null, filePathCallback);
        return true;
    }


    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond) {
        if (mFragment == null){
            return;
        }
        if (mFragment.mFileUploadCallbackFirst != null) {
            mFragment.mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFragment.mFileUploadCallbackFirst = fileUploadCallbackFirst;

        if (mFragment.mFileUploadCallbackSecond != null) {
            mFragment.mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFragment.mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");

        mFragment.startActivityForResult(
                Intent.createChooser(i, "请选择上传文件方式"),
                BaseWebFragment.FILECHOOSER_RESULTCODE);

    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        /*LogUtils.d("js", "web console message: " + consoleMessage.message()
                + "; line : " + consoleMessage.lineNumber()
                + "; sourceId : " + consoleMessage.sourceId());*/
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        LogUtils.d("js", "web console message: " + message
                + "; line : " + lineNumber
                + "; sourceId : " + sourceID);
    }
}
