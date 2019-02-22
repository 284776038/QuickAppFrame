package cn.bfy.frame.download;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import cn.bfy.frame.BuildConfig;
import cn.bfy.frame.R;
import cn.bfy.frame.util.DownLoadItemManager;
import cn.bfy.frame.util.NotificationUtil;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　XXXXXXXXXXXXXXX
 * @company    : XXXXXXXXXXXXXXX
 * @author     : OuyangJinfu
 * @e-mail     : jinfu123.-@163.com
 * @createDate : 2017/7/6 0006
 * @modifyDate : 2017/7/6 0006
 * @version    : 1.0
 * @desc       : 默认的下载文件回调通知
 * </pre>
 */

public class DefaultDownloadListener implements DownloadListener {

    private Reference<Context> mRef;

    public DefaultDownloadListener(Context context) {
        mRef = new SoftReference<>(context);
    }

    @Override
    public void onSuccess(Request request) {
        if (mRef.get() == null) { return; }
//        NotificationUtil.getNotificationManager(mRef.get()).cancel(request.id);
        DownLoadItemManager.add(new DownLoadItemManager.DownLoadItem(request));
        File file = new File(request.filePath);
        Intent install = new Intent();
        Uri downloadFileUri = Uri.fromFile(file);
        String downloadMimeType = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContentResolver.SCHEME_FILE.equals(downloadFileUri.getScheme())) {
                downloadFileUri = FileProvider.getUriForFile(mRef.get(),
                    BuildConfig.APPLICATION_ID + ".provider", file);
            }
            install.setAction(Intent.ACTION_INSTALL_PACKAGE);
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(downloadFileUri, downloadMimeType);
        } else {
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(downloadFileUri, downloadMimeType);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        NotificationUtil.showPendingIntentNotification(mRef.get(),
            "下载完成", install, request.id);
        DownloadManager.unregisterListener(request);
    }

    @Override
    public void onPregress(Request request, int progress) {
        if (mRef.get() == null) { return; }
        NotificationUtil.showProgressNotification(mRef.get(),
            mRef.get().getString(R.string.app_name), progress, request.id);
    }

    @Override
    public void onFailed(Request request) {
        if (mRef.get() == null) { return; }
        NotificationUtil.showProgressNotification(mRef.get(),
            mRef.get().getString(R.string.app_name), -1, request.id);
        DownloadManager.unregisterListener(request);
    }

    @Override
    public void onCanceled(Request request) {
        if (mRef.get() == null) { return; }
        NotificationUtil.getNotificationManager(mRef.get()).cancel(request.id);
        DownloadManager.unregisterListener(request);
    }
}
