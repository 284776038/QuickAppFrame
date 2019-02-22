package cn.bfy.frame.presenter;

import android.support.annotation.NonNull;

import cn.bfy.frame.util.schedulers.BaseSchedulerProvider;
import cn.bfy.frame.view.WebviewView;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author : Pan
 * time   : 2017/4/28
 * desc   : xxxx描述
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:XXXXXXXXXXXXXXXXXXXX
 */

public class WebviewPresenter implements BasePresenter {
    @NonNull
    private final WebviewView mView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeSubscription mSubscriptions;

    public WebviewPresenter(@NonNull WebviewView view, @NonNull BaseSchedulerProvider schedulerProvider) {
        mView = checkNotNull(view);
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null!");
        mSubscriptions = new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }


    /*public void onFinishDownload(Context context, final String packageName) {
        UserInfoModel loginModel = new Gson().fromJson(SharePreUtils.getInstance().getString(Constant.USER_INFO), UserInfoModel.class);
        if (loginModel != null) {
            LogUtils.i(TAG,"上报 积分 packageName = " + packageName + " skey=" + loginModel.getsKey());
            mSubscriptions.add(RichApiService.getRichApiInstance().finishDownload(loginModel.getsKey(), packageName)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<FinishDownloadModel>()
                    {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e(TAG,e.getMessage());
                        }

                        @Override
                        public void onNext(FinishDownloadModel finishDownloadModel) {
                            if (finishDownloadModel != null && finishDownloadModel.getRetcode() == 1){
                                LogUtils.i(TAG,"上报 成功 package =" + packageName);
                            }else {
                                LogUtils.i(TAG,"上报 失败 package =" + packageName);
                            }
                        }
                    }));
        }
    }*/



}
