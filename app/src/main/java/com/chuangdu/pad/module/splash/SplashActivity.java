package com.chuangdu.pad.module.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chuangdu.library.app.BaseSplashActivity;
import com.chuangdu.library.app.rxpermissions.RxPermissions;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.StatusBarUtil;
import com.chuangdu.library.widget.YesNoDialog;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.common.Constant;
import com.chuangdu.pad.common.PathUtil;
import com.chuangdu.pad.event.X5DownloadEvent;
import com.chuangdu.pad.models.VersionModel;
import com.chuangdu.pad.module.splash.contract.DaggerSplashComponent;
import com.chuangdu.pad.module.splash.contract.SplashContract;
import com.chuangdu.pad.module.splash.contract.SplashModule;
import com.chuangdu.pad.module.splash.presenter.SplashPresenter;
import com.chuangdu.pad.utils.AppUpdateUtils;
import com.chuangdu.pad.utils.AppUtils;
import com.chuangdu.pad.utils.PermissionUtil;
import com.chuangdu.pad.utils.Utility;
import com.chuangdu.pad.widget.X5WebView;
import com.chuangdu.suyangpad.R;
import com.tencent.smtt.sdk.QbSdk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author sc
 */

@Route(path = PathUtil.PATH_SPLASH)
public class SplashActivity extends BaseSplashActivity implements SplashContract.View {

    @BindView(R.id.version)
    TextView tvVersion;
    @BindView(R.id.show_layout)
    LinearLayout llShowLayout;
    @BindView(R.id.comein)
    TextView tvComeIn;

    @BindView(R.id.init)
    TextView tvInit;

    @Inject
    SP mSP;
    @Inject
    public SplashPresenter mPresenter;
//    @Inject
//    UserRepository mUserRepository;

    public static void start(Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void initView() {

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        DaggerSplashComponent.builder().appComponent(Application.getAppComponent()).splashModule(new SplashModule(this)).build().inject(this);

//        StatusBarUtil.setTransparent(this);
//        StatusBarUtil.StatusBarLightMode(this, StatusBarUtil.StatusBarLightMode(this));

        tvVersion.setText(AppUtils.getVerName(this));
        putJPushId();
        init();

    }

    private void putJPushId(){
        //RetrofitRequestTool.setDevToken(mSP, JPushInterface.getRegistrationID(Application.getApplication()));
    }

    @OnClick({R.id.cancel_btn, R.id.agree_btn, R.id.privacy, R.id.greement, R.id.comein, R.id.iv_main})
    public void onClick(View v){
        if (v.getId() == R.id.cancel_btn) {
            finish();
        } else if (v.getId() == R.id.agree_btn) {
            mSP.putBoolean(Constant.IS_AGREE, true);
            llShowLayout.setVisibility(View.GONE);
            next();
//        } else if (v.getId() == R.id.privacy){
//            ARouter.getInstance().build(PathUtil.PATH_WEB)
//                    .withString(PathUtil.DATA_TYPE_TITLE, "隐私协议")
//                    .withString(PathUtil.DATA_TYPE_URL, DBManager.getUrlModel().privacyUrl)
//                    .navigation();
//        } else if (v.getId() == R.id.greement){
//            ARouter.getInstance().build(PathUtil.PATH_WEB)
//                    .withString(PathUtil.DATA_TYPE_TITLE, "服务协议")
//                    .withString(PathUtil.DATA_TYPE_URL, DBManager.getUrlModel().serviceAgreementUrl)
//                    .navigation();
        } else if (v.getId() == R.id.comein) {
            if (null != mMyCountDownTimer) {
                mMyCountDownTimer.cancel();
                timeNext();
            }
        } else if (v.getId() == R.id.comein) {
            if (null != mMyCountDownTimer) {
                mMyCountDownTimer.cancel();
                timeNext();
            }
        }
    }

    private void init(){
        RxPermissions.getInstance(SplashActivity.this).request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe((aBoolean) -> {
                    if (aBoolean) {
                        //time();
                        appVersion();
                    } else {
                        showDialog();
                    }
                    // showInfo();

//                    mPresenter.appVersion();
                });
    }

    private void showInfo(){

        RxPermissions.getInstance(SplashActivity.this).request(Manifest.permission.RECORD_AUDIO)
                .subscribe((aBoolean) -> {
                    if (aBoolean) {
                        if (Utility.isFirst(SplashActivity.this, mSP)) {
                            llShowLayout.setVisibility(View.VISIBLE);
                        } else {
                            if (mSP.getBoolean(Constant.IS_AGREE, false)) {
                                llShowLayout.setVisibility(View.GONE);
                                next();
                            } else {
                                llShowLayout.setVisibility(View.VISIBLE);
                                tvComeIn.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        showDialog();
                    }
                });

    }
    MyCountDownTimer mMyCountDownTimer;



    private void time(){

        tvComeIn.setVisibility(View.VISIBLE);
        tvComeIn.setText("5s");

        mMyCountDownTimer = new MyCountDownTimer(5 * 1000, 1000);
        mMyCountDownTimer.setView(tvComeIn);
        mMyCountDownTimer.start();
        tvInit.setVisibility(View.GONE);
    }

    private void timeNext(){
//        if (mUserRepository.isHaveLogin() && mUserRepository.isHaveSetUserInfo()) {
//            ARouter.getInstance().build(PathUtil.PATH_MAIN).navigation();
//        }

//        if (AppUtils.isTablet(this)) {
//            ARouter.getInstance().build(PathUtil.PATH_USER_STAGIEST).navigation();
//        } else {
//            ARouter.getInstance().build(PathUtil.PATH_LOGIN).navigation();
//        }

        //ARouter.getInstance().build(PathUtil.PATH_USER_STAGIEST).navigation();
        ARouter.getInstance().build(PathUtil.PATH_BROWSER).navigation();

        finish();
    }

    private void showDialog(){
        YesNoDialog dialog = YesNoDialog.newInstance("温馨提示", "请先设置允许使用权限", "去设置", "关闭", new YesNoDialog.Listener() {
            @Override
            public boolean onYes(YesNoDialog dialog) {
                PermissionUtil.gotoPermission(SplashActivity.this);
                finishlater();
                return false;
            }

            @Override
            public boolean onNo(YesNoDialog dialog) {
                //showInfo();

                return false;
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    private void next(){
        startNext();
    }
    private void startNext(){
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String type = null;
//        String id = null;
//        String page = null;
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Uri uri = intent.getData();
//            if (uri != null) {
//                type = uri.getQueryParameter("type");
//
//                if (!TextUtils.isEmpty(type) && Integer.parseInt(type) == Constant.OpenType.ORDER_VIEW) {
//                    id = uri.getQueryParameter("data");
//
//                    if (mUserRepository.isHaveLogin() && mUserRepository.isHaveSetUserInfo()) {
//                        ARouter.getInstance().build(PathUtil.PATH_MAIN).withInt(PathUtil.DATA_TYPE_ORDER_ID, Integer.parseInt(id)).navigation();
//                    }
//                    EventBus.getDefault().post(new MainOrderEvent(Integer.parseInt(id)));
//                }
//                if (!TextUtils.isEmpty(type) && Integer.parseInt(type) == Constant.OpenType.MAIN) {
//                    page = uri.getQueryParameter("data");
//
//                    if (mUserRepository.isHaveLogin() && mUserRepository.isHaveSetUserInfo()) {
//                        ARouter.getInstance().build(PathUtil.PATH_MAIN).withInt(PathUtil.DATA_TYPE_PAGE, Integer.parseInt(page)).navigation();
//                    }
//
//                    EventBus.getDefault().post(new MainPageEvent(Integer.parseInt(page)));
//                }
//            } else {
//                if (mUserRepository.isHaveLogin() && mUserRepository.isHaveSetUserInfo()) {
//                    ARouter.getInstance().build(PathUtil.PATH_MAIN).navigation();
//                }
//            }
//            finishlater();
//
//        } else {
//
//            if (Utility.isFirstRun(SplashActivity.this, mSP)) {
//                Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe((v) -> {
//                    ARouter.getInstance().build(PathUtil.PATH_SPLASH_GUIDE).navigation();
//                    finishlater();
//                });
//            } else {
//                time();
//            }
//        }
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        //getData();
        time();
    }


    private void appVersion(){

        X5WebView mWebView = new X5WebView(this, null);
        if (null != mWebView.getX5WebViewExtension()) {
            mPresenter.appVersion();
            tvInit.setVisibility(View.GONE);
        } else {
            tvInit.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class MyCountDownTimer extends CountDownTimer {
        private final long millisInFuture;
        private final long countDownInterval;
        private TextView bt;

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.millisInFuture = millisInFuture;
            this.countDownInterval = countDownInterval;
        }

        public void setView(View view) {
            bt = (TextView) view;
        }

        @Override
        public void onFinish() {
            timeNext();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt.setText(getString(R.string.splash_time, (millisUntilFinished / 1000  + 1) + ""));
        }
    }
    VersionModel mVersionModel;

    private void update(){
        try {
            String channel = Application.getApplication().getPackageManager().getApplicationInfo(Application.getApplication().getPackageName(), PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");

            if (!TextUtils.isEmpty(channel) && channel.equals(Constant.VERIFIED_CHANNEL)) {
                if (null != mVersionModel && mVersionModel.xiaomiType) {
                    Constant.APP_SHOW = true;
                    mSP.putBoolean(Constant.LAST_APP_SHOW, true);
                } else {
                    Constant.APP_SHOW = false;
                    mSP.putBoolean(Constant.LAST_APP_SHOW, false);
                }
            }
            if (!TextUtils.isEmpty(channel) && channel.equals(Constant.ONE_KEY_VERIFIED_CHANNEL)) {
                if (null != mVersionModel && !mVersionModel.akeyFlag) {
                    Constant.APP_SHOW_ONE_KEY = true;
                    mSP.putBoolean(Constant.LAST_APP_SHOW_ONE_KEY, true);
                } else {
                    Constant.APP_SHOW_ONE_KEY = false;
                    mSP.putBoolean(Constant.LAST_APP_SHOW_ONE_KEY, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mVersionModel.version = 2000;
        if (null != mVersionModel && mVersionModel.version > AppUtils.getVersionCode(this)) {
            AppUpdateUtils.init(this).showVer(mVersionModel.url, mVersionModel.remark, mVersionModel.version, mVersionModel.forceFlag, new AppUpdateUtils.CallBack() {
                @Override
                public void onNext() {
                    //showInfo();
                    time();
                }

                @Override
                public void onFinish() {
                    finish();
                }
            });
        } else {
            //showInfo();
            time();
        }
    }
    @Override
    public void versionSuccess(VersionModel model) {
        mVersionModel = model;
        updateHandler.sendEmptyMessage(0);
    }

    @Override
    public void versionFail(String msg) {
        //showInfo();
    }

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            update();
        }
    };
    boolean isX5Finish;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void x5DownloadEvent(X5DownloadEvent event) {
        if (!isX5Finish) {
            toast("初始化完成，即将重新启动");

            x5Finish();
        }
        isX5Finish = true;
    }

    private void x5Finish(){
//        YesNoDialog dialog = YesNoDialog.newInstance("提示", "初始化完成，请重新启动", "重新启动",  new YesNoDialog.Listener() {
//            @Override
//            public boolean onYes(YesNoDialog dialog) {
//                AppUtils.reboot(getApplicationContext());
//                return false;
//            }
//
//            @Override
//            public boolean onNo(YesNoDialog dialog) {
//                return false;
//            }
//        });
//
//
//        dialog.show(getSupportFragmentManager(), "");
        Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe((v) -> {
            AppUtils.reboot(getApplicationContext());
        });
    }
}
