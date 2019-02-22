package cn.bfy.frame.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

import cn.bfy.frame.BuildConfig;

/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/27
 * @version 1.0
 */

public class ApkInstall {


    private static final String TAG = "ApkInstall";

    public static void installApk(Context context, File file, String url){
        if (context == null || file == null || !file.exists()){
            return;
        }
        Intent install = new Intent();
        Uri downloadFileUri = Uri.fromFile(file);
        String downloadMimeType = getMimeType(file,url);
        LogUtils.i(TAG,"downloadFileUri = " + downloadFileUri.toString() + " downloadMimeType = " + downloadMimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContentResolver.SCHEME_FILE.equals(downloadFileUri.getScheme())) {
                downloadFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                LogUtils.i(TAG, " convert uri = " + downloadFileUri.toString());
            }
            install.setAction(Intent.ACTION_INSTALL_PACKAGE);
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(downloadFileUri, downloadMimeType);
        } else {
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(downloadFileUri, downloadMimeType);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(install);
    }

    public static String getMimeType(@NonNull File file, String url){
//        return MimeTypeMap.getFileExtensionFromUrl(url);
        return "application/vnd.android.package-archive";
    }



}
