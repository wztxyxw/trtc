package com.chuangdu.pad.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuangdu.library.app.BaseApplication;
import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.ToastUtil;
import com.chuangdu.pad.api.UrlUtils;
import com.chuangdu.pad.common.dagger.AppComponent;
import com.chuangdu.pad.common.dagger.AppModule;
import com.chuangdu.pad.common.dagger.DaggerAppComponent;
import com.chuangdu.pad.event.X5DownloadEvent;
import com.chuangdu.pad.im.ConfigHelper;
import com.chuangdu.pad.im.MessageNotification;
import com.chuangdu.pad.utils.AppUtils;
import com.chuangdu.suyangpad.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMFriendProfileOption;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


/**
 * @author sc
 */
public class Application extends BaseApplication {

    private static final String TAG = Application.class.getSimpleName();
    private static Application sApp;
    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    private static AppComponent sAppComponent;

    private static Application instance;

    public static Application getInstance() {
        return  instance;
    }

    synchronized public static Application getApplication() {
        return sApp;
    }

    @Inject
    public SP mSP;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        sApp = this;
//        if (BuildConfig.DEBUG) {
//            UrlUtils.API_MAIN = "http://api-test.jinpai56.com:8280/";
//        }
//        ToastUtil.init(this);
//        if (!BuildConfig.BUILD_TYPE.equals(Constant.RELEASE)) {
//            SP sp = new SP(this);
//            String url = sp.getString(Constant.MAIN_URL);
//            if (!TextUtils.isEmpty(url)) {
//                UrlUtils.API_MAIN = url;
//            } else {
//                UrlUtils.API_MAIN = UrlUtils.API_MAIN_TEST;
//            }
//        }
        if (!BuildConfig.BUILD_TYPE.equals(Constant.RELEASE)) {
            SP sp = new SP(this);
            String url = sp.getString(Constant.MAIN_URL);
            if (!TextUtils.isEmpty(url)) {
                UrlUtils.API_MAIN = url;
            } else {
                UrlUtils.API_MAIN = UrlUtils.API_MAIN_TEST;
            }
        }

        LogUtil.setIsShowLog(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
//        LitePal.initialize(this);
        initDagger();
        ARouter.init(sApp);
        ToastUtil.init(sApp);
//        RetrofitRequestTool.setAppType(mSP, Constant.APP_TYPE);

        initThirdSDK(this);
//        UMConfigure.init(this, Constant.UMENG_KEY, null, UMConfigure.DEVICE_TYPE_PHONE, null);
//        try {
//            String channel = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
//
//            initBugly(channel);
////            CrashReport.initCrashReport(getApplicationContext());
//
//            if (!TextUtils.isEmpty(channel) && channel.equals(Constant.VERIFIED_CHANNEL)) {
//                if (Utility.isFirst(this, mSP)) {
//                    Constant.APP_SHOW = false;
//                    mSP.putBoolean(Constant.LAST_APP_SHOW, false);
//                } else {
//                    Constant.APP_SHOW = false;
//                    Constant.APP_SHOW = mSP.getBoolean(Constant.LAST_APP_SHOW, false);
//                }
//            } else {
//                Constant.APP_SHOW = true;
//                mSP.putBoolean(Constant.LAST_APP_SHOW, true);
//            }
//
//            if (!TextUtils.isEmpty(channel) && channel.equals(Constant.ONE_KEY_VERIFIED_CHANNEL)) {
//                if (Utility.isFirst(this, mSP)) {
//                    Constant.APP_SHOW_ONE_KEY = false;
//                    mSP.putBoolean(Constant.LAST_APP_SHOW_ONE_KEY, false);
//                } else {
//                    Constant.APP_SHOW_ONE_KEY = false;
//                    Constant.APP_SHOW_ONE_KEY = mSP.getBoolean(Constant.LAST_APP_SHOW_ONE_KEY, false);
//                }
//            } else {
//                Constant.APP_SHOW_ONE_KEY = true;
//                mSP.putBoolean(Constant.LAST_APP_SHOW_ONE_KEY, true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        ConversationManagerKit.getInstance().setSP(mSP);
    }

    public static void initBugly(String channel){

        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(Application.getInstance());
        //设置渠道
        strategy.setAppChannel(channel);
        //App的版本
        strategy.setAppVersion(AppUtils.getVerName(Application.getInstance()));
        //App的包名
        strategy.setAppPackageName(Application.getInstance().getPackageName());

        // 初始化Bugly
        //CrashReport.initCrashReport(Application.getInstance(), Constant.BUGLY_KEY, true, strategy);
    }

    /**
     * 初始化第三方库
     * @param context
     */
    public static void initThirdSDK(Context context){
        initJPUSH(context);
        initTIMSDK(context);
        initX5();
//        Mojito.initialize(FrescoImageLoader.with(sApp), new SketchImageLoadFactory());
////        Mojito.initialize(GlideImageLoader.Companion.with(sApp), new SketchImageLoadFactory());
//        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
//                //使用使用IjkPlayer解码
//                .setPlayerFactory(IjkPlayerFactory.create())
//                .build());
//
//        SoundUtils.getInstance().initPool(sApp);
    }

    public static void initX5(){

        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogUtil.e("Tbs", " onDownloadFinish  " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                LogUtil.e("Tbs", " onInstallFinish  " + i);

                EventBus.getDefault().post(new X5DownloadEvent());
            }

            @Override
            public void onDownloadProgress(int i) {
                LogUtil.e("Tbs", " onDownloadProgress  " + i);
            }
        });
        QbSdk.setDownloadWithoutWifi(true);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtil.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(sApp,  cb);
    }

    public static void initJPUSH(Context context){

        /** 设置开启日志,发布时请关闭日志 初始化 JPush*/
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
//        JPushInterface.init(context);
    }

    public static void initTIMSDK(Context context){
//        // 配置 Config，请按需配置
//        TUIKitConfigs configs = TUIKit.getConfigs();
//        configs.setSdkConfig(new TIMSdkConfig(Constant.IM_ID));
//        configs.setCustomFaceConfig(new CustomFaceConfig());
//        configs.setGeneralConfig(new GeneralConfig());
//
//        TUIKit.init(context, Constant.IM_ID, new ConfigHelper().getConfigs());
//        TUIKit.addIMEventListener(new IMEventListener() {
//
//            @Override
//            public void onForceOffline() {
//                //被其他终端踢下线
//                LogUtil.e(Constant.TagType.CHAT_TAG, "IM onKickedOffline");
//                EventBus.getDefault().post(new IMEvent(IMEvent.LOGIN));
//            }
//
//            @Override
//            public void onUserSigExpired() {
//                //用户签名过期了，需要刷新 userSig 重新登录 SDK
//                LogUtil.e(Constant.TagType.CHAT_TAG, "IM onUserSigExpired");
//                EventBus.getDefault().post(new IMEvent(IMEvent.SIGEXPIRED));
//            }
//
//            @Override
//            public void onConnected() {
//                super.onConnected();
//                LogUtil.e(Constant.TagType.CHAT_TAG, "IM onConnected");
//            }
//
//            @Override
//            public void onDisconnected(int code, String desc) {
//                super.onDisconnected(code, desc);
//                LogUtil.e(Constant.TagType.CHAT_TAG, "IM onDisconnected");
//            }
//
//            @Override
//            public void onNewMessages(List<TIMMessage> msgs) {
//
//                super.onNewMessages(msgs);
//
//                if (null != msgs && msgs.size() > 0) {
//
//                    int count = 0;
//
//                    for (TIMMessage msg : msgs) {
//                        if (msg.getConversation().getType() == V2TIMConversation.V2TIM_C2C) {
//                            if (msg.getSender().equals("admin") && !msg.isRead()) {
//                                EventBus.getDefault().post(new SystemMsgEvent(msg));
//
//                                //msg.remove();
//                                if (BuildConfig.BUILD_TYPE.equals("release")) {
//                                    TIMManager.getInstance().deleteConversationAndLocalMsgs(V2TIMConversation.V2TIM_C2C, msg.getSender());
//                                } else {
//                                    TIMTextElem elem = new TIMTextElem();
//                                    elem.setText(DateUtils.formatSecondTimestamp(msg.getMessageLocator().getTimestamp(), DateStyle.YYYY_MM_DD_HH_MM_SS) + " " + DateUtils.format(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
//
//                                    TIMMessage message = new TIMMessage();
//                                    message.addElement(elem);
//                                    IMManager.sendMessage(msg.getSender(), message);
//                                }
//                                //return;
//                            } else {
//                                count++;
//                                EventBus.getDefault().post(msg);
//                            }
//                        }
//                        if (msg.getConversation().getType() == TIMConversationType.System) {
//                            EventBus.getDefault().post(new SystemMsgEvent(msg));
//                        }
//                    }
//                    if (count > 0) {
//                        EventBus.getDefault().post(new MessageCountEvent(count));
//                    }
//                }
//            }
//        });

        //instance.registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());


        TUIKit.init(sApp, Constant.IM_ID, new ConfigHelper().getConfigs());

        TUIKit.addIMEventListener(new IMEventListener() {
            @Override
            public void onRefreshConversation(List<V2TIMConversation> conversations) {
                super.onRefreshConversation(conversations);

                //EventBus.getDefault().post(new ConversationEvent());
            }

            @Override
            public void onNewMessage(V2TIMMessage msg) {
                LogUtil.e(TAG, "onNewMessage msg: " + msg.toString());
                //MessageHelper.notify(msg);
//                MessageNotification notification = MessageNotification.getInstance();
//                notification.notify(msg);
            }
        });
        sApp.registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());

        TIMUserConfig config = new TIMUserConfig();
        TIMFriendProfileOption timFriendProfileOption = new TIMFriendProfileOption();
        timFriendProfileOption.setExpiredSeconds(60 * 60 * 24 *  365);
        config.setTIMFriendProfileOption(timFriendProfileOption);

        config.enableReadReceipt(true);

        TIMManager.getInstance().setUserConfig(config);
    }

    public void initDagger() {

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        sAppComponent.inject(this);

    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    static class StatisticActivityLifecycleCallback implements android.app.Application.ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;
        private IMEventListener mIMEventListener = new IMEventListener() {
            @Override
            public void onNewMessage(V2TIMMessage msg) {
                LogUtil.e(TAG, "onNewMessage msg: " + msg.toString());

                if (msg.getSender().equals("admin") && !msg.isRead()) {
                    MessageNotification notification = MessageNotification.getInstance();
                    notification.notify(msg);
                }
            }
        };

        private ConversationManagerKit.MessageUnreadWatcher mUnreadWatcher = new ConversationManagerKit.MessageUnreadWatcher() {
            @Override
            public void updateUnread(int count) {
                // 华为离线推送角标
                //HUAWEIHmsMessageService.updateBadge(DemoApplication.this, count);
            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            LogUtil.e(TAG, "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // 若bundle不为空则程序异常结束
                // 重启整个程序
//                Intent intent = new Intent(activity, SplashActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                sApp.startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
                LogUtil.e(TAG, "application enter foreground");
                V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtil.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtil.e(TAG, "doForeground success");
                    }
                });
                TUIKit.removeIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().removeUnreadWatcher(mUnreadWatcher);
                MessageNotification.getInstance().cancelTimeout();
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // 应用切到后台
                LogUtil.e(TAG, "application enter background");
                int unReadCount = ConversationManagerKit.getInstance().getUnreadTotal();
                V2TIMManager.getOfflinePushManager().doBackground(unReadCount, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtil.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtil.e(TAG, "doBackground success");
                    }
                });
                // 应用退到后台，消息转化为系统通知
                TUIKit.addIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().addUnreadWatcher(mUnreadWatcher);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
