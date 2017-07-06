package cn.richinfo.frame.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

import cn.richinfo.frame.util.DownLoadItemManager;
import cn.richinfo.frame.util.LogUtils;

/**
 * Description: 监听apk安装成功 {@link Intent#ACTION_PACKAGE_ADDED} <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:深圳彩讯科技有限公司 <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/9
 * @version 1.0
 */

public class ApkInstallReceiver extends BroadcastReceiver {
        private static final String TAG = "ApkInstallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i(TAG,"action = " + action);
        String packageName = intent.getDataString();
        LogUtils.i(TAG,"apk install success,packname=" + packageName);
        DownLoadItemManager.DownLoadItem item = DownLoadItemManager.removeByPackageName(packageName);
        boolean isDelete = false;
        if (item != null) {
//            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            dm.remove(item.getDownloadId());
            File file = new File(item.getFilePath());
            isDelete = file.delete();
        }
        LogUtils.i(TAG," remove " + packageName + " " + isDelete);
    }

}
