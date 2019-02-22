package cn.bfy.frame.download;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/26
 * @version 1.0
 */

public class DownloadService extends IntentService{
    private static final String TAG = "DownloadService";
    private static final String OPERATION_EXTRA = "operation_extra";

    private static final String REQUEST_EXTRA = "request_extra";

    private static final String REQUEST_NOTIFY_EXTRA = "request_notify";

    private static final int DOWNLOAD_OPERATION = 0x100;
    private static final int CANCEL_OPERATION = DOWNLOAD_OPERATION + 1;

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    /**
     * 启动下载
     * @param context
     */
    public static void download(@NonNull Context context, @NonNull Request request) {
        startService(context,DOWNLOAD_OPERATION,request);
    }

    /**
     * 取消下载
     * @param context
     * @param request
     */
    public static void cancelDownload(@NonNull Context context, @NonNull Request request){
        startService(context,CANCEL_OPERATION,request);
    }

    private static void startService(Context context, int operation, Request request){
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(OPERATION_EXTRA, operation);
        intent.putExtra(REQUEST_EXTRA,request);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, " DownloadService onHandleIntent");
//        final Context context = getApplicationContext();
        int oper = intent.getIntExtra(OPERATION_EXTRA, 0);
        Request request = intent.getParcelableExtra(REQUEST_EXTRA);
        boolean notify = intent.getBooleanExtra(REQUEST_NOTIFY_EXTRA, false);
        if (notify) {
            DownloadManager.registerListener(new DefaultDownloadListener(this), request);
        }
        Log.d(TAG, " DownloadService onHandleIntent operation = " + oper);
        final DownloadManager dm = DownloadManager.getInstance();
        switch (oper) {
            case DOWNLOAD_OPERATION:
                dm.download(request);
                break;
            case CANCEL_OPERATION:
                dm.cancel(request);
                break;
            default:
                Log.d(TAG, "unknow operation!");
                return;
        }
        Log.d(TAG, " DownloadService onHandleIntent end");
    }
}
