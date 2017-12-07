package cn.richinfo.frame.download;

import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/26
 * @version 1.0
 */

public class DownloadManager implements Downloadable{
    private static final String TAG = "DownloadManager";
    static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>(null);
    static final ConcurrentHashMap<String, AtomicReference<DownloadListener>> LISTENER = new ConcurrentHashMap<>();

    ExecutorService mExecutor;
    /**
     *  java.util.concurrent.ThreadPoolExecutor#submit(Runnable) return a RunnableFuture<br>
     *  java.util.concurrent.FutureTask is RunnableFuture Impl
     */
    SparseArray<FutureTask> mCache;


    private DownloadManager() {
        //临时实现 后期改为自定义大小
        mExecutor = Executors.newSingleThreadExecutor();
        mCache = new SparseArray<>(20);
    }

    public static DownloadManager getInstance(){
        for (int i = 0;;){
            Log.i(TAG," init DownloadManager count = " + i);
            DownloadManager curr = INSTANCE.get();
            if (curr != null)
                return curr;
            curr = new DownloadManager();
            if(INSTANCE.compareAndSet(null,curr)){
                return curr;
            }
        }
    }

    public static void registerListener(DownloadListener listener, Request request){
        int id = request.id;
        if (listener == null){
            throw new DownloadException(" DownloadListener is not null");
        }
        AtomicReference<DownloadListener> reference = LISTENER.get(id + "");
        if (reference == null) {
            reference = new AtomicReference<>();
            LISTENER.put(id + "", reference);
        }
        reference.set(listener);
    }

    public static void unregisterListeners(){
        /*DownloadListener curr = LISTENER.get();
        if (curr != null){
            LISTENER.compareAndSet(curr,null);
        }*/
        LISTENER.clear();
    }

    public static void unregisterListener(Request request) {
        int id = request.id;
        AtomicReference<DownloadListener> reference = LISTENER.get(id + "");
        if (reference != null) {
            DownloadListener curr = reference.get();
            if (curr != null) {
                reference.set(null);
            }
            LISTENER.remove(id + "");
        }
    }


    @Override
    public void download(Request request) {
        DownloadListener listener = null;
        AtomicReference<DownloadListener> reference = LISTENER.get(request.id + "");
        if (reference != null) {
            listener = reference.get();
        }
        if (listener == null){
            return;
        }
        //提前显示0% 不然等文件开始写入会延迟很久
        listener.onPregress(request, 0);
        FutureTask future = (FutureTask) mExecutor.submit(new DownloadTask(request, listener));
        mCache.put(request.id,future);
    }

    @Override
    public void cancel(Request request) {
        mCache.get(request.id).cancel(true);
        mCache.remove(request.id);
        DownloadListener listener = null;
        AtomicReference<DownloadListener> reference = LISTENER.get(request.id + "");
        if (reference != null) {
            listener = reference.get();
        }
        if (listener != null)
            listener.onCanceled(request);

    }

}
