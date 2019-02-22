package cn.bfy.frame.download;

/**
 * Description: 描述 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:XXXXXXXXXXXXXXXXXXXX <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/26
 * @version 1.0
 */

public interface DownloadListener {

    /**
     * 通知下载成功
     */
    void onSuccess(Request request);

    /**
     * 通知当前的下载进度
     * @param progress
     */
    void onPregress(Request request, int progress);

    /**
     * 通知下载失败
     */
    void onFailed(Request request);

    /**
     * 通知下载取消事件
     */
    void onCanceled(Request request);
}

