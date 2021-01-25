package com.chuangdu.pad.common.dagger;

import com.chuangdu.library.util.SP;
import com.chuangdu.pad.api.UserApi;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.common.retrofit.HttpsUtil;
import com.chuangdu.pad.data.user.UserRepository;
import com.chuangdu.pad.data.user.local.UserLocalSource;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 功能描述：
 *
 * @author sc
 * @since 2018/9/22
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    /**
     * 获取UserRepository
     * @return UserRepository
     */
    UserRepository userRepository();

    /**
     * 获取UserLocalSource
     * @return UserLocalSource
     */
    UserLocalSource userLocalSource();

    /**
     * 获取 UserApi
     * @return UserApi
     */
    UserApi userApi();
//
//    /**
//     * 获取 WorkApi
//     * @return WorkApi
//     */
//    WorkApi workApi();
//
//
//    /**
//     * 获取AMapManager
//     * @return AMapManager
//     */
//    AMapManager amapManager();

    /**
     * 获取SP
     * @return SP
     */
    SP sp();

    /**
     * dagger注入
     * @param application Application
     */
    void inject(Application application);

    /**
     * dagger注入
     * @param util HttpsUtil
     */
    void inject(HttpsUtil util);
}

