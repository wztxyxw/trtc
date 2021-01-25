package com.chuangdu.pad.common.dagger;

import android.content.Context;

import com.chuangdu.library.util.SP;
import com.chuangdu.pad.api.UrlUtils;
import com.chuangdu.pad.api.UserApi;
import com.chuangdu.pad.common.retrofit.RetrofitUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 功能描述：
 * @since 2018/9/22
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    public SP provideSP(Context context) {
        return new SP(context);
    }

    /**
     * 获取用户信息API
     */
    @Provides
    @Singleton
    public UserApi provideUserApi(SP sp) {
        return RetrofitUtil.getService(UserApi.class, UrlUtils.API_MAIN, sp);
    }
//    /**
//     * 获取域名API
//     */
//    @Provides
//    @Singleton
//    public WorkApi provideWorkApi(SP sp) {
//        return RetrofitUtil.getService(WorkApi.class, UrlUtils.API_MAIN, sp);
//    }
}
