package cn.richinfo.frame.ui.widget.webview;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;

import java.io.File;

import cn.richinfo.frame.R;
import cn.richinfo.frame.ui.fragment.BaseWebFragment;
import cn.richinfo.frame.ui.widget.RationaleForPerissionDialog;
import cn.richinfo.frame.util.Constant;
import cn.richinfo.frame.util.DownLoadItemManager;
import cn.richinfo.frame.util.LogUtils;
import cn.richinfo.frame.util.NetworkUtil;
import cn.richinfo.frame.util.PermissionUtils;
import cn.richinfo.frame.util.StringUtils;


/**
 * Description: webview下载监听 </br>
 * Copyright: Copyright (c) 2017 </br>
 * Company:XXXXXXXXXXXXXXXXXXXX </br>
 * Email:284425176@qq.com </br>
 *
 * @author suma on 2017/5/9
 * @version 1.0
 */

public class RodoDownLoadListener implements DownloadListener {
    private static final String TAG = /*RodoDownLoadListener.class.getSimpleName()*/"RodoDownLoadListener";

    private BaseWebFragment mFragment;
    private Context mContext;

    private File mFolder;

    public RodoDownLoadListener(BaseWebFragment mFragment) {
        this.mFragment = mFragment;
        init(mFragment);
    }

    private void init(BaseWebFragment fragment){
        mContext = fragment.getContext().getApplicationContext();
        File file = Environment.getExternalStoragePublicDirectory(Constant.APP_NAME);
        mFolder = new File(file, Constant.DIR_NAME_DOWNLOAD);
    }

    /**
     * Notify the host application that a file should be downloaded
     *
     * @param url                The full url to the content that should be downloaded
     * @param userAgent          the user agent to be used for the download.
     * @param contentDisposition Content-disposition http header, if
     *                           present.
     * @param mimetype           The mimetype of the content reported by the server
     * @param contentLength      The file size reported by the server
     */
    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, final long contentLength) {
        if (mFragment == null || mContext == null)
            return;
        if(PermissionUtils.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            mFragment.showMess("请于系统设置中开启读写SD卡权限");
            return;
        }
        //非wifi状态提示
        if (!NetworkUtil.isWifi(mContext)){
            RationaleForPerissionDialog dlg = RationaleForPerissionDialog.getInstance(mFragment.getFragmentManager(),"not wifi connect",null);
            dlg.setOnConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download(url, userAgent, contentDisposition, mimetype, contentLength);
                }
            });
            return;
        }
        download(url, userAgent, contentDisposition, mimetype, contentLength);
    }

    private void download(String url, String userAgent, String contentDisposition, String mimetype, long contentLength){
        String fileName = null;
        String downloadName = null;
        if (isNeedDownload() &&
                (downloadName = getDownloadFileName(getName(fileName = getFileName(contentDisposition)),getSuffix(fileName)))!= null) {
            doDownload(downloadName,url,userAgent,contentDisposition,mimetype,contentLength);
        }
    }

    private boolean isNeedDownload(){
        if (!mFolder.exists()){
            return mFolder.mkdirs();
        }
        return true;
    }

    /**
     * 获取新文件名,内置重复文件名
     * @param name 文件名不带后缀
     * @param suffix 文件后缀名 例如.zip
     * @return
     */
    private String getDownloadFileName(String name,String suffix){
        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(suffix)){
//                        LogUtils.i(TAG," getDownloadFileName => null" );
            return null;
        }
//        File folder = Environment.getExternalStoragePublicDirectory(APP_NAME);
        File file;
        int i = 1;
        StringBuilder fileName = new StringBuilder(name + suffix);
        while(true){
            file = new File(mFolder,fileName.toString());
            if (file.exists()){
                fileName.delete(0,fileName.length()-1);
                fileName.append(name).append("(").append(i).append(")").append(suffix);
                i++;
                continue;
            }
            LogUtils.i(TAG," getDownloadFileName => " + fileName.toString());
            return fileName.toString();
        }
    }

    /**
     * 返回文件或者null
     * @param contentDisposition
     * @return
     */
    private String getFileName(String contentDisposition){
//        http://roodo.zj.chinamobile.com/roodo-mgr/roodo/file/startupimg/17/5/18/20170518184431.apk
        if (StringUtils.isNullOrEmpty(contentDisposition)) {
//                        LogUtils.i(TAG," getFileName => null" );
            return null;
        }
        //contentDisposition=attachment;filename="ces.rar"
        int start = contentDisposition.indexOf("\"") + 1;
        int end = contentDisposition.lastIndexOf("\"");
        if (start!=-1 && end != start){
            return contentDisposition.substring(start,end);
        }
        return null;
    }

    /**
     * 获取不带后缀的文件名
     * @param fileName
     * @return
     */
    private String getName(String fileName){
        if (StringUtils.isNullOrEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1)
            return fileName.substring(0,index);
        return null;
    }

    private String getSuffix(String fileName){
        if (StringUtils.isNullOrEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1)
            return null;
        return fileName.substring(index,fileName.length());
    }


    private void doDownload(String fileName,String url, String userAgent, String contentDisposition, String mimetype, long contentLength){
        LogUtils.i(TAG, String.format(" doDownload  url = %s  fileName = %s userAgent = %s contentDisposition = %s mimetype = %s contentLength = %s"
                ,url,fileName,userAgent,contentDisposition,mimetype,contentLength));
        if (mContext == null)
            return;
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
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
        request.setDescription(String.format("从%s应用下载文件",mContext.getString(R.string.app_name)));
        long downloadId = dm.enqueue(request);
        DownLoadItemManager.DownLoadItem item = new DownLoadItemManager.DownLoadItem(downloadId,url);
        item.setMimeType(mimetype);
        item.setContentLength(contentLength);
        DownLoadItemManager.add(item);
    }
}
