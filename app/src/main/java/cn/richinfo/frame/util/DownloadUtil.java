package cn.richinfo.frame.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import cn.richinfo.frame.R;


/**
 * Description: 下载工具类,调用{@link DownloadManager}进行下载 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/19
 * @version 1.0
 */

public class DownloadUtil {
    private static final String TAG = "DownloadUtil";

    public static void downloadApk(Context context, String url, String packageName){
        if (context == null || StringUtils.isNullOrEmpty(url) || StringUtils.isNullOrEmpty(packageName)){
            return;
        }
        String fileName = getFileName(url);
        if (fileName != null) {
            doDownload(context,fileName,url,packageName);
        }
    }

    private static void doDownload(Context context, String fileName, String url, String packageName){
        LogUtils.i(TAG, String.format(" doDownload  url = %s  fileName = %s packageName = %s "
                ,url,fileName,packageName));
        if (context == null)
            return;
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
        //设置允许下载的网络连接类型
//                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //允许MediaScanner()扫描该文件
        request.allowScanningByMediaScanner();
        request.setTitle(fileName);
        //默认只有下载中有通知,改为下载中和下载完成后均有通知栏通知
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    设置下载中通知栏提示的介绍
        request.setDescription(String.format("从%s应用下载文件",context.getString(R.string.app_name)));
        long downloadId = dm.enqueue(request);
        DownLoadItemManager.DownLoadItem item = new DownLoadItemManager.DownLoadItem(downloadId,url);
        item.setPackageName(packageName);
//        item.setMimeType(mimetype);
//        item.setContentLength(contentLength);
        DownLoadItemManager.add(item);
    }

    /**
     * 返回文件或者null
     * @param url
     * @return
     */
    private static String getFileName(String url){
//        http://roodo.zj.chinamobile.com/roodo-mgr/roodo/file/startupimg/17/5/18/20170518184431.apk
        String result = null;
        if (StringUtils.isNullOrEmpty(url)) {
            return null;
        }
        int start = url.lastIndexOf("/") + 1;
        if (start != -1){
            result = url.substring(start);
        }
        if (result != null && result.length()>20){
            result = result.substring(result.length() - 21);
        }
        return result;
    }

}
