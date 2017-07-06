package cn.richinfo.frame.network;

import com.google.gson.GsonBuilder;

import java.util.concurrent.atomic.AtomicReference;

import cn.richinfo.frame.util.Constant;
import cn.richinfo.frame.util.http.HttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author : Pan
 * time   : 2017/4/26
 * desc   : xxxx描述
 * version: 2.0 By Suma 增加了多线程处理
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:深圳彩讯科技有限公司
 */

public class RichApiService {
    public static AtomicReference<RichApi> instance = new AtomicReference<>(null);

    public static RichApi getRichApiInstance(){
        for (;;){
            RichApi curr = instance.get();
            if (curr != null){
                return curr;
            }
            curr = createRichService();
           if (instance.compareAndSet(null,curr)){
                return curr;
           }
        }
    }

    private static RichApi createRichService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .serializeNulls()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()))
                .client(HttpClient.INSTANCE.getOkHttpClient())
                .build();
        return retrofit.create(RichApi.class);
    }
}
