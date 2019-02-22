package cn.bfy.frame.receiver;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import cn.bfy.frame.BuildConfig;
import cn.bfy.frame.interfaces.FinishDownloadCallback;
import cn.bfy.frame.util.DownLoadItemManager;
import cn.bfy.frame.util.LogUtils;
import cn.bfy.frame.util.StringUtils;


/**
 * Description: 应用下载完毕接收器,用于拉起自动安装 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/9
 * @version 1.0
 */

public class DownloadManagerReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadManagerReceiver";
    private FinishDownloadCallback callback;

    public DownloadManagerReceiver(FinishDownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断当前文件是否是下载的应用apk,是就拉起安装.
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId == -1)
            return;
        DownLoadItemManager.DownLoadItem item = DownLoadItemManager.getDownLoadItemById(downloadId);
        if (item != null && item.getDownloadId() == downloadId) {
            upLoad(context,item);
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent();

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = dManager.query(query);
            String downloadLocalUri = null;
            String downloadMimeType = null;
            if (cursor.moveToFirst()) {
//                int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            }
            cursor.close();
            LogUtils.i(TAG,"downloadLocalUri = " + downloadLocalUri + " downloadMimeType = " + downloadMimeType);
            if (StringUtils.isNullOrEmpty(downloadLocalUri)){
                LogUtils.i(TAG, " download failed ");
                return ;
            }
            Uri downloadFileUri = Uri.parse(downloadLocalUri);
            LogUtils.i(TAG, " downloadFileUri = " + downloadFileUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (ContentResolver.SCHEME_FILE.equals(downloadFileUri.getScheme())) {
                    // FileUri - Convert it to contentUri.
                    File file = new File(downloadFileUri.getPath());
                    if (!file.exists()) {
                        LogUtils.e(TAG, "download file not exists " + file.getAbsolutePath());
                    }
                    downloadFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    LogUtils.i(TAG, " convert uri = " + downloadFileUri.toString());
                }
                install.setAction(Intent./*ACTION_INSTALL_PACKAGE*/ACTION_VIEW);
                install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(downloadFileUri, downloadMimeType);
            } else {
                install.setAction(Intent.ACTION_VIEW);
                install.setDataAndType(downloadFileUri, downloadMimeType);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(install);
        }
    }

    /**
     * 上报积分 消息
     * @param context
     * @param item
     */
    private void upLoad(Context context, DownLoadItemManager.DownLoadItem item){
//        FinishDownloadCallback callback = FinishDownloadHelper.getInstance();
        if (callback != null)
            callback.onCallbcak(context,item.getPackageName());
        else
            LogUtils.w(TAG,"upload failed");
    }

    private String getFileName(Context context, Uri contentUri) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);
        String[] contentParts = contentUri.getEncodedPath().split("/");
        Cursor q = downloadManager.query(new DownloadManager.Query().setFilterById(Integer.parseInt(contentParts[contentParts.length - 1])));
        if (q == null) {
            // Download no longer exists
            return null;
        }
        q.moveToFirst();

//        return q.getString(q.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        int fileUriIdx = q.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

        String fileUri = q.getString(fileUriIdx);
        String fileName = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (fileUri != null) {
                fileName = Uri.parse(fileUri).getPath();
            }
        } else {
            //Android 7.0以上的方式：请求获取写入权限，这一步报错
            //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
            int fileNameIdx = q.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            fileName = q.getString(fileNameIdx);
        }
        return fileName;
    }
}
