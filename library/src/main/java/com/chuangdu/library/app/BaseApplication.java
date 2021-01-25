package com.chuangdu.library.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.squareup.leakcanary.RefWatcher;


public class BaseApplication extends MultiDexApplication {
    private static BaseApplication INSTANCE;
    private RefWatcher refWatcher;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
//        BGASwipeBackHelper.init(this, null);

//        OkHttpClient okHttpClient = HttpManager.getFrescoHttpClient(HttpLoggingInterceptor.Level.BODY); // build on your own
//        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
//                .newBuilder(INSTANCE, okHttpClient)
//                .build();
//        Fresco.initialize(INSTANCE, config);
//初始化fresco
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient okHttpClient = HttpManager.getFrescoHttpClient(HttpLoggingInterceptor.Level.BODY); // build on your own
//                ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
//                        .newBuilder(INSTANCE, okHttpClient)
//                        .build();
//                Fresco.initialize(INSTANCE, config);
//                //内存泄漏检测
//                //refWatcher = LeakCanary.install(this);
//            }
//        }).start();

    }
}
