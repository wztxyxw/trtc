# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#oss
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.thoughtworks.xstream.io.xml.**
-dontwarn com.thoughtworks.xstream.core.util.**
-dontwarn com.thoughtworks.xstream.converters.reflection.**
-dontwarn com.thoughtworks.xstream.converters.javabean.**
-dontwarn com.thoughtworks.xstream.converters.extended.**
-dontwarn com.thoughtworks.xstream.io.json.**
-dontwarn com.thoughtworks.xstream.mapper.**
-dontwarn com.thoughtworks.xstream.security.**
-dontwarn java.lang.invoke**

-dontwarn org.slf4j.**
-dontwarn org.slf4j.spi.**
-dontwarn javax.enterprise.event.**
-dontwarn javax.enterprise.util.**
-dontwarn javax.enterprise.inject.**
-dontwarn javax.swing.tree.**
-dontwarn javax.swing.event.**
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn org.dbunit.database.**
-dontwarn org.dbunit.operation.**
-dontwarn org.dbunit.dataset.datatype.**
-dontwarn org.hibernate.cfg.**
-dontwarn javax.mail.internet.**
-dontwarn javax.mail.**
-dontwarn java.lang.management.**
-dontwarn javax.naming.**
-dontwarn java.lang.management.**
-dontwarn org.eclipse.jetty.jmx.**
-dontwarn java.beans.**
-dontwarn sun.net.www.protocol.http.**
-dontwarn com.sun.net.httpserver.**
-dontwarn org.mortbay.log.**
-dontwarn org.mortbay.util.ajax.**
-dontwarn org.ietf.jgss.**
-dontwarn javax.imageio.**
-dontwarn org.dbunit.dataset.**
-dontwarn org.hibernate.**
-dontwarn org.eclipse.jetty.server.handler.jmx.**
-dontwarn javax.enterprise.context.**
-dontwarn javax.enterprise.context.**
-dontwarn org.fourthline.cling.support.contentdirectory.ui.**
-dontwarn org.fourthline.cling.support.shared.**
-dontwarn org.fourthline.cling.support.shared.log.**
-dontwarn javax.enterprise.context.**
-dontwarn org.fourthline.cling.transport.impl.**
-dontwarn org.seamless.swing.**
-dontwarn org.seamless.swing.logging.**
-dontwarn org.eclipse.jetty.server.session.jmx.**
-dontwarn org.eclipse.jetty.servlet.jmx.**
-keep public class org.apache.commons.codec.** { *; }
-keep public class org.apache.commons.codec.binary.** { *; }
-keep public class org.apache.commons.codec.digest.** { *; }
-keep public class org.apache.commons.codec.language.** { *; }
-keep public class org.apache.commons.codec.net.** { *; }

-optimizationpasses 5 #proguard对你的代码进行迭代优化的次数，首先要明白optimization 会对代码进行各种优化，每次优化后的代码还可以再次优化，所以就产生了 优化次数的问题，这里面的 passes 应该翻译成 ‘次数’
-dontusemixedcaseclassnames #不使用大小写形式的混淆名
-dontskipnonpubliclibraryclasses #不跳过library的非public的类
-dontoptimize #不进行优化，优化可能会在某些手机上无法运行。
-dontpreverify #不进行预校验，该校验是java平台上的，对android没啥用处
-keepattributes Singature #避免混淆泛型
-keepattributes *Annotation* #对注解中的参数进行保留
-dontshrink #不缩减代码，需要注意，反射调用的代码会被认为是无用代码而删掉，所以要提前keep出来
-keepclassmembers enum * { #保持枚举类中的属性不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View { # 保留我们自定义控件（继承自View）不被混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers public class xxx extends xxx { #不混淆任何view子类的get和set方法
    void set*(***);
    *** get*();
}
-keepclassmembers class * implements android.os.Parcelable { #aidl文件不能去混淆
    public static final android.os.Parcelable$Creator CREATOR;
}
-keepclassmembers class **.R$* { #资源类变量需要保留
    public static *;
}
-keep public class com.chuangdu.jinpai.wxapi.** {*;}
-keep public class com.chuangdu.jinpaihz.wxapi.** {*;}
-keep public class com.vo.chuangdu.models.** {*;}
-keep public class com.vo.chuangdu.models.**
-keep public class com.vo.chuangdu.module.** {*;}
-keep public class com.vo.chuangdu.entiy.** {*;}
-keep public class com.vo.chuangdu.data.** {*;}
-keep public class com.vo.chuangdu.utils.** {*;}
-keep public class com.vo.chuangdu.widget.** {*;}
-keep public class com.vo.chuangdu.event.** {*;}
-keep public class com.vo.chuangdu.im.** {*;}
-keep public class com.vo.chuangdu.activity.** {*;}
-keep public class com.vo.chuangdu.models.rsp.** {*;}
-keep public class com.vo.chuangdu.entry.** {*;}
-keep public class com.vo.chuangdu.common.** {*;}
-keep public class com.vo.chuangdu.common.**
-keep public class com.vo.chuangdu.event.rsp.** {*;}
-keep public class com.vo.chuangdu.api.** {*;}
-keep public class com.vo.network.** {*;}
-keep public class com.sc.library.**
-keep public class com.sc.library.app.** {*;}
-keep public class com.sc.library.widget.** {*;}
-keep public class com.sc.library.util.** {*;}

-keep public class com.android.lib.** {*;}

#播放器
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# RxJava避免混淆
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okhttp3.internal.**
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

# ButterKnife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# LitePal
-keep public class org.litepal.** {*;}
-keep public class * extends org.litepal.** {*;}

#讯飞语音
-keep class com.iflytek.**{*;}

### 高德地图相关 ###
-dontwarn com.amap.api.maps.**

#3D 地图 V5.0.0之后：
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

### 支付宝 ###
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn org.androidannotations.api.rest.**

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class * extends Android.webkit.WebChromeClient {
      public void openFileChooser(...);
}

#友盟
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.umeng.umverify.** {*;}
-keep class cn.com.chinatelecom.gateway.lib.** {*;}
-keep class com.unicom.xiaowo.login.** {*;}
-keep class com.cmic.sso.sdk.** {*;}
-keep class com.mobile.auth.** {*;}
-keep class android.support.v4.** { *;}
-keep class org.json.**{*;}
-keep class com.alibaba.fastjson.** {*;}
-keep class com.umeng.** {*;}

#车机打电话sdk
-keep class cn.com.changan.autocheck.** {*;}
-keep class com.incall.** {*;}
-keep class wseemann.media.** {*;}
-dontwarn com.incall.**

-dontwarn com.amap.api.navi.view.SlidingUpPanelLayout

#imsdk
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }

#Fresco
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}

-keepattributes Exceptions,InnerClasses

-keepattributes Signature

#RongCloud SDK
-keep class io.rong.** {*;}
-keep class cn.rongcloud.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

#VoIP
-keep class io.agora.rtc.** {*;}

#红包
-keep class com.google.gson.** { *; }
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.rylib.** {*;}

-ignorewarnings

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**

-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-keep class com.baidu.tts.**{*;}
    -keep class com.baidu.speechsynthesizer.**{*;}

#EasyPhotos

-keep class com.huantansheng.easyphotos.models.** { *; }

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keep public class * extends android.app.Service

#混淆HMS Core SDK导致功能异常
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

#oppo
-keep public class * extends android.app.Service

#xiaomi
#这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
-keep class com.vo.chuangdu.im.thirdpush.XiaomiMsgReceiver {*;}
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-keep class com.xiaomi.push.**

#vivo
-dontwarn com.vivo.push.**

-keep class com.vivo.push.**{*;}

-keep class com.vivo.vms.**{*;}

-keep class com.vo.chuangdu.im.thirdpush.**{*;}
-keep class com.vo.chuangdu.im.thirdpush.**

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}

