package cn.bfy.frame.util;

import android.util.Log;

import cn.bfy.frame.BuildConfig;

public class LogUtils {
    public static boolean isDebug = BuildConfig.DEBUG;
    public static final LogUtils instance = new LogUtils();

    public static LogUtils getInstance() {
        return instance;
    }

    public void setDebugMode(boolean isDebug) {
        LogUtils.isDebug = isDebug;
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
