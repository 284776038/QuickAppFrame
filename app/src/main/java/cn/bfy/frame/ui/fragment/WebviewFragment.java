package cn.bfy.frame.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bfy.frame.R;
import cn.bfy.frame.download.DownloadService;
import cn.bfy.frame.download.Request;
import cn.bfy.frame.presenter.WebviewPresenter;
import cn.bfy.frame.receiver.ApkInstallReceiver;
import cn.bfy.frame.ui.widget.RationaleForPerissionDialog;
import cn.bfy.frame.ui.widget.webview.RodoDownLoadListener;
import cn.bfy.frame.ui.widget.webview.RodoWebChromeClient;
import cn.bfy.frame.ui.widget.webview.RodoWebViewClient;
import cn.bfy.frame.util.ApkInstall;
import cn.bfy.frame.util.DownLoadItemManager;
import cn.bfy.frame.util.LogUtils;
import cn.bfy.frame.util.NetworkUtil;
import cn.bfy.frame.util.NotificationUtil;
import cn.bfy.frame.util.PermissionUtils;
import cn.bfy.frame.util.WebUtils;
import cn.bfy.frame.view.WebviewView;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebviewFragment extends BaseWebFragment implements WebviewView {

    // UI references.
    @BindView(R.id.webview)
    WebView mWebview;
    private WebviewPresenter mPresenter;
    private String url;

    private List<Request> mRequests = new ArrayList<>();
    private ApkInstallReceiver mInstallReceiver;


    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public WebView getmWebview() {
        return mWebview;
    }

    public static WebviewFragment newInstance() {
        return new WebviewFragment();
    }

    @Override
    public int setContentView() {
        return R.layout.fragment_webview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstallReceiver = new ApkInstallReceiver();
        IntentFilter installFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        getContext().registerReceiver(mInstallReceiver,installFilter);
    }

    @Override
    public void initView(View root) {
        super.initView(root);
        WebUtils.initSetting(getContext().getApplicationContext(), mWebview);
        mWebview.setWebViewClient(new RodoWebViewClient(this, mWebview, null));
        mWebview.setWebChromeClient(new RodoWebChromeClient(this, mWebview));
        mWebview.setDownloadListener(new RodoDownLoadListener(this));
        mWebView.addJavascriptInterface(new RichInfoJSInterface(getActivity()),
                "Roodo");

        mWebview.loadUrl(url);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mInstallReceiver);
        for (Request request : mRequests) {
            cn.bfy.frame.download.DownloadManager.unregisterListener(request);
        }
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull WebviewPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onSuccess(Request request) {
        NotificationUtil.getNotificationManager(getContext()).cancel(request.id);
        DownLoadItemManager.add(new DownLoadItemManager.DownLoadItem(request));
//        mPresenter.onFinishDownload(getContext(),request.packageName);
        ApkInstall.installApk(getContext(),new File(request.filePath),request.url);
    }

    @Override
    public void onPregress(Request request, int progress) {
        NotificationUtil.showProgressNotification(getContext(), getString(R.string.app_name), progress, request.id);
    }

    @Override
    public void onFailed(Request request) {
        NotificationUtil.showProgressNotification(getContext(), getString(R.string.app_name), -1, request.id);
    }

    @Override
    public void onCanceled(Request request) {
        NotificationUtil.getNotificationManager(getContext()).cancel(request.id);
    }

    public class RichInfoJSInterface {
        Activity activity;

        RichInfoJSInterface(Activity activity) {
            this.activity = activity;
        }


        @JavascriptInterface
        public void share(String title, String description, String url, String logo) {
//            ShareUtils.shareUrlWithDisplay(activity, title, description, url);
        }

        @JavascriptInterface
        public void closeActivity() {
           getActivity().finish();
        }

        @JavascriptInterface
        public void downloadApk(String url, String packageName){
//            url = "https://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
            doDownloadApk(url,packageName);
        }

        private void doDownloadApk(final String url, final String packageName) {
            LogUtils.i("MoneyFragment","download " + url + " package = " + packageName);
            if(PermissionUtils.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                showMess("请于系统设置中开启读写SD卡权限");
                return;
            }
            //非wifi状态提示
            if (!NetworkUtil.isWifi(getContext())){
                RationaleForPerissionDialog dlg = RationaleForPerissionDialog.getInstance(getChildFragmentManager(),"not wifi connect",null);
                dlg.setOnConfirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    download(url, userAgent, contentDisposition, mimetype, contentLength);
//                    DownloadUtil.downloadApk(getContext(),url,packageName);
                        download(url,packageName);
                    }
                });
                return;
            }
//        DownloadUtil.downloadApk(getContext(),url,packageName);
            download(url,packageName);
        }

        private void download(String url, String packageName){
            Request request = new Request(url,packageName);
            mRequests.add(request);
            cn.bfy.frame.download.DownloadManager.registerListener(WebviewFragment.this, request);
            DownloadService.download(getContext(),request);
        }

    }
}
