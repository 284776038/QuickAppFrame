package cn.bfy.frame;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * author : Pan
 * time   : 2017/4/19
 * desc   : Application
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2012
 * Company:XXXXXXXXXXXXXXXXXXXX
 */

public class RichApplication extends Application {
    public static Context mContext;
    public static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);

        mContext = this.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mContext = null;
        mRefWatcher = null;
    }

    public static Context getContext() {
        return mContext;
    }
}
