package cn.richinfo.frame.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.richinfo.frame.R;
import cn.richinfo.frame.util.Constant;
import cn.richinfo.frame.util.FileUtil;
import cn.richinfo.frame.util.LogUtils;
import cn.richinfo.frame.util.SharePreUtils;
import cn.richinfo.frame.view.BaseWebView;

/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/8
 * @version 1.0
 */

public abstract class BaseWebFragment extends BaseFragment implements BaseWebView {
    private static final String TAG = "BaseWebFragment";
    /**
     * @link android.app.Fragment#startActivityForResult(Intent, int)}
     */
    public final static int FILECHOOSER_RESULTCODE = 51426;
    /**
     * File upload callback for platform versions prior to Android 5.0
     */
    public ValueCallback<Uri> mFileUploadCallbackFirst;
    /**
     * File upload callback for Android 5.0+
     */
    public ValueCallback<Uri[]> mFileUploadCallbackSecond;

    @BindView(R.id.webview)
    protected WebView mWebView;
    @BindView(R.id.progress_view)
    protected ProgressBar mProgress;
    @BindView(R.id.web_network_error_rly)
    protected RelativeLayout mErrorView;
    @BindView(R.id.btn_refresh)
    protected Button mBtnRefresh;

    /**
     * 页面加载是否错误,用于规避finish时隐藏error界面
     */
    private boolean isError = false;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setCookie(getContext(),getFirstUrl(),getCookie());
//    }
    @Override
    public int setContentView() {
        return R.layout.fragment_webview;
    }

    @Override
    public void initView(View root) {
        mProgress.setMax(100);
    }

    @Override
    public void onPageStarted(WebView ebView, String url) {
        isError = false;
        LogUtils.i(TAG,"onPageStarted");
        if (mProgress != null)
            mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        if (!isError){
            hideErrorView();
        }
        LogUtils.i(TAG,"onPageFinished");
        if (mProgress != null)
            mProgress.setVisibility(View.GONE);

    }

    @Override
    public void onProgressChange(WebView webView, int progress) {
        if (mProgress != null) {
            if (progress != 100) {
                if (mProgress.getVisibility() != View.VISIBLE) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                mProgress.setProgress(progress);
            } else {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showMess(String mess) {
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        isError = true;
        LogUtils.i(TAG,"onError");
        if (mErrorView != null){
            mErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        if (mWebView != null)
            mWebView.reload();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, String.format(" requestCode=%s resultCode=%s data=%s",requestCode,resultCode,data));
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if (mFileUploadCallbackFirst != null) {
                        Uri result = data.getData();
                        LogUtils.i(TAG, "result: " + result.toString());
                        String uriPath = FileUtil.getPath(this.getContext().getApplicationContext(), result);
                        //uriPath = new String(Base64.decode(uriPath));
                        LogUtils.i(TAG, "uriPath: " + uriPath);
                        result = Uri.fromFile(new File(uriPath));
                        LogUtils.i(TAG, "result2: " + result.toString());
                        mFileUploadCallbackFirst.onReceiveValue(result);
                        mFileUploadCallbackFirst = null;
                    }
                    else if (mFileUploadCallbackSecond != null) {
                        Uri[] dataUris;
                        try {
                            dataUris = new Uri[] { Uri.parse(data.getDataString()) };
                            LogUtils.i(TAG, "intent.getDataString(): " + data.getDataString());
                        }
                        catch (Exception e) {
                            dataUris = null;
                        }

                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            }
            else {
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                }
                else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }

    }

    /**
     * 返回键 是否被webview消耗
     * @return true : 被消耗
     */
    public boolean onBackPressed(){
        hideErrorView();
        if (mProgress != null)
            mProgress.setVisibility(View.GONE);
        if (mWebView != null){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.btn_refresh)
    public void onRefreshClick(View v){
        onRefresh();
    }

    public void clearWebView() {

        int sdk = Build.VERSION.SDK_INT;
        //清空缓存操作在高系统版本上有bug，故屏蔽
        if (null != mWebView && (sdk < 19)) {
            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearMatches();
        }
        mWebView.clearHistory();
    }

    protected void hideErrorView(){
        if (mErrorView!=null && mErrorView.getVisibility() == View.VISIBLE){
            mErrorView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取最初加载的页面url(带拼接参数)
     *
     * @return
     */
    protected String getFirstUrl() {
        return getBaseUrl() + getSkey();
    }

    /**
     * 获取基础url
     * @return
     */
    protected String getBaseUrl(){
        return "";
    }

    /**
     * 切换模块时,重置webView<p>
     * 使用必须重写{@link #getFirstUrl()}
     */
    public void resetWebView() {
        while(mWebView.canGoBack()){
            mWebView.goBack();
        }
        clearWebView();
    }

    protected String getSkey(){
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
