package cn.richinfo.frame.download;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import cn.richinfo.frame.BuildConfig;
import cn.richinfo.frame.R;
import cn.richinfo.frame.util.ApkInstall;
import cn.richinfo.frame.util.DownLoadItemManager;
import cn.richinfo.frame.util.LogUtils;
import cn.richinfo.frame.util.NotificationUtil;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/7/6 0006
 * @modifyDate : 2017/7/6 0006
 * @version    : 1.0
 * @desc       :
 * </pre>
 */

public class DefaultDownloadListener implements DownloadListener {

    private Reference<Context> mRef;

    public DefaultDownloadListener(Context context) {
        mRef = new SoftReference<Context>(context);
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
    }

    @Override
    public void onCanceled(Request request) {
        if (mRef.get() == null) { return; }
        NotificationUtil.getNotificationManager(mRef.get()).cancel(request.id);
    }
}
