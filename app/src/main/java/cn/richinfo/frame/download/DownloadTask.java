package cn.richinfo.frame.download;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.richinfo.frame.util.LogUtils;
import okhttp3.OkHttpClient;
import okhttp3.Response;


/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:深圳彩讯科技有限公司 <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/26
 * @version 1.0
 */

public class DownloadTask implements Runnable {

    private static final String TAG = "DownloadTask";
    Request request;
    DownloadListener listener;
    AtomicBoolean isPaused;
    AtomicBoolean isCanceled;



    public DownloadTask(Request request, DownloadListener listener) {
        if (request == null || listener == null) {
            throw new DownloadException("DownloadTask init failed");
        }
        this.request = request;
        this.listener = listener;
        isPaused = new AtomicBoolean(false);
        isCanceled = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        LogUtils.d(TAG, " run " + request.toString());
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        long downloadLength = 0;   //记录已经下载的文件长度
        //文件下载地址
        String downloadUrl = request.url;
        //下载文件的名称
        String fileName = getDownloadFileName(request.url);
        //下载文件存放的目录
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        //创建一个文件
        file = new File(directory + fileName);
        if (file.exists()) {
            file.delete();
        }
        boolean isCreated = false;
        try {
            isCreated = file.createNewFile();
        } catch (IOException e) {
            LogUtils.w(TAG,e.getMessage());
            if (listener != null) {
                listener.onFailed(request);
            }
            return;
        }
        if (!isCreated){
            if (listener != null) {
                listener.onFailed(request);
            }
            return;
        }
        request.filePath = file.getAbsolutePath();
//        DownloadConfig config = getConfig(file.getAbsolutePath());
        //得到下载内容的大小
        long contentLength;
        try {

            contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                if (listener != null) {
                    listener.onFailed(request);
                }
                return;
            }
            LogUtils.d(TAG, String.format("url = %s\nfileName = %s\nfile = %s\ncontentLength = %s",
                downloadUrl,fileName,file.getAbsoluteFile(),contentLength));
        } catch (IllegalArgumentException e) {
            if (listener != null) {
                listener.onFailed(request);
            }
            return;
        }

        OkHttpClient client = new OkHttpClient();
        /**
         * HTTP请求是有一个Header的，里面有个Range属性是定义下载区域的，它接收的值是一个区间范围，
         * 比如：Range:bytes=0-10000。这样我们就可以按照一定的规则，将一个大文件拆分为若干很小的部分，
         * 然后分批次的下载，每个小块下载完成之后，再合并到文件中；这样即使下载中断了，重新下载时，
         * 也可以通过文件的字节长度来判断下载的起始点，然后重启断点续传的过程，直到最后完成下载过程。
         */
        try {
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .addHeader("RANGE", "bytes=" + 0 + "-")  //断点续传要用到的，指示下载的区间
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                int prevProgress = 0;
                int currProgress = 0;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled.get()) {
                        if (listener != null) {
                            listener.onCanceled(this.request);
                        }
                        return;
                    }  else {
                        total += len;
                        savedFile.write(b, 0, len);
                        //计算已经下载的百分比
                        currProgress = (int) ((total + downloadLength) * 100 / contentLength);
                        if (prevProgress != currProgress) {
                            prevProgress = currProgress;
                            if (listener != null) {
                                listener.onPregress(this.request, currProgress);
                            }
                        }
                    }

                }
                response.body().close();
                if (listener != null) {
                    listener.onSuccess(this.request);
                }
                return;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFailed(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFailed(request);
            }
        } finally {
            close(is);
            close(savedFile);
            if (isCanceled.get() && file != null) {
                file.delete();
            }
        }
    }

    public Request getRequest() {
        return request;
    }

    private String getDownloadFileName(String url){
//        return url.substring(url.lastIndexOf("/"));
        return File.separator+System.currentTimeMillis() + ".apk";
    }

    /**
     * 得到下载内容的大小
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
