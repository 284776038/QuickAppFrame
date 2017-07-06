package cn.richinfo.frame.download;

/**
 * Description: 下载服务异常 <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company:深圳彩讯科技有限公司 <br>
 * Email:284425176@qq.com <br>
 *
 * @author suma on 2017/5/26
 * @version 1.0
 */

public class DownloadException extends RuntimeException {

    private static final String PREFIX =" DownloadException : ";

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DownloadException(String message) {
        super(PREFIX + message);
    }
}
