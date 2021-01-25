package com.chuangdu.library.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chuangdu.library.R;
import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.library.util.StatusBarUtil;
import com.chuangdu.library.util.ToastUtil;
import com.chuangdu.library.widget.DialogLoading;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static com.chuangdu.library.util.StatusBarUtil.FlymeSetStatusBarLightMode;
import static com.chuangdu.library.util.StatusBarUtil.MIUISetStatusBarLightMode;


/**
 *
 * @author SHANGXIANLOU
 * @date 2018/4/22
 */

public abstract class BaseSplashActivity extends AppCompatActivity {

    private final String CLASS_NAME = this.getClass().getName();
    protected DialogLoading mLoadingView;
    protected ProgressDialog pd;
    protected Context mContext;
    public View errorNoData;
    public View errorNoNetwork;
    public View view;
    private boolean isForegroud = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setDarkStatusIcon(true);
        setContentView(getContentView());
        mContext = this;
        errorNoData = LayoutInflater.from(mContext).inflate(R.layout.view_error, null);
        errorNoNetwork = LayoutInflater.from(mContext).inflate(R.layout.view_error_network, null);

//        setTransparentForWindow();
//        StatusBarUtil.setTransparentForWindow(this);
//        StatusBarUtil.StatusBarLightMode(this,StatusBarUtil.StatusBarLightMode(this));

        ButterKnife.bind(this);

        setTransparentForWindow();
        StatusBarLightMode(StatusBarUtil.StatusBarLightMode(this));


        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        initView();
        initClick();
        initData();
    }
    /**
     * 设置布局文件
     *
     * @return
     */
    public abstract int getContentView();

    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 设置点击事件
     */
    public abstract void initClick();

    /**
     * 初始化数据
     */
    public abstract void initData();

    public View $(int id) {
        return findViewById(id);
    }


    public void setErrorLayout(BaseQuickAdapter baseQuickAdapter) {
        baseQuickAdapter.setEmptyView(errorNoData);
    }

    public void setErrorNetwork(BaseQuickAdapter baseQuickAdapter) {
        baseQuickAdapter.notifyDataSetChanged();
        baseQuickAdapter.setEmptyView(errorNoNetwork);
    }

    public void startActivity(Class cla) {
        startActivity(new Intent(this, cla));
    }

    public void setDarkStatusIcon(boolean bDark) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = getWindow().getDecorView();

            if (decorView != null) {

                int vis = decorView.getSystemUiVisibility();

                if (bDark) {

                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                } else {

                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                }

                decorView.setSystemUiVisibility(vis);

            }

            getWindow().setStatusBarColor(Color.TRANSPARENT);


        }


    }

    /**
     * 全局等待对话框
     */
    public void showWaitDialog() {
        //如果ctx等于空或者isFinishing
        if (mContext == null || isFinishing()) {
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null
                        || !pd.isShowing()) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage(getString(R.string.loading));
                    pd.setCancelable(true);
                    pd.show();
                }

            }
        });

    }

    /**
     * 全局等待对话框
     */
    public void showWaitDialog(String msg) {
        //如果ctx等于空或者isFinishing
        if (mContext == null || isFinishing()) {
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null || !pd.isShowing()) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage(msg);
                    pd.setCancelable(true);
                    pd.show();
                } else {
                    pd.setMessage(msg);
                }

            }
        });

    }

    /**
     * 设置透明
     */
    public void setTransparentForWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public int StatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(this, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    public void unStatusBarLightMode() {
        int type = StatusBarUtil.StatusBarLightMode(this);
        if (type == 1) {
            MIUISetStatusBarLightMode(this, false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(getWindow(), false);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param type 1:MIUUI 2:Flyme 3:android6.0
     */
    public void StatusBarLightMode(int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(this, true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(getWindow(), true);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    public void dismissWaitDialog() {
        if (mContext == null || isFinishing()) {
            return;
        }
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null
                        && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });


    }
    public void showToastLong(String msg) {
        ToastUtil.getInstance().toastLong(msg);
    }

    public void handleToast(int code, String msg, int status, String desc) {
        showToast(msg);
//        if (code > 0){
//            showToast(msg);
//        } else {
//            if (status == 1004) {
//                AccountManager.init().logout();
//                AccountManager.init().isSessionValid(this);
//            } else {
//                showToast(getString(R.string.system_busy));
//            }
//        }
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
//    }

    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = new DialogLoading(this).builder();
        }
        mLoadingView.show();
    }

    public void showLoadingView(boolean fullScreen) {
        if (mLoadingView == null) {
            mLoadingView = new DialogLoading(this).builder();
        }
        mLoadingView.show();
    }


    public void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.dismiss();
        }
    }

    public void showToast(String msg) {
        ToastUtil.getInstance().toast(msg);
    }


    public void toast(String msg) {
        ToastUtil.getInstance().toast(msg);
    }

    public void toast(@StringRes int msgId) {
        ToastUtil.getInstance().toast(msgId);
    }


    @Override
    protected void onStop() {
        super.onStop();
        isForegroud = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForegroud = true;
        MobclickAgent.onPageEnd(CLASS_NAME);
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForegroud = true;
        MobclickAgent.onPageStart(CLASS_NAME);
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏

    }


    @Override
    protected void onStart() {
        super.onStart();
        isForegroud = true;
    }

    public boolean isForegroud(){
        return isForegroud;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void finishlater() {
        Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe((v) -> {
            finish();
        });
    }

    public void initBarHeight(View view){

        //4.4以下 无法沉浸 状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
        }
    }

    private long mClickTime = System.currentTimeMillis() + 500;
    private static final long INTERVAL_TIME = 500;

    public boolean isFastClick(){

        boolean fast = false;

        if (System.currentTimeMillis() - mClickTime < INTERVAL_TIME) {
            fast = true;
        }
        mClickTime = System.currentTimeMillis();
        return fast;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        navigationBarStatusBar(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }
    /**
     *
     * @param
     */
    private void setNavigationBar(){
        View decorView = getWindow().getDecorView();
        int option = SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(option);
    }
    /**
     * 导航栏，状态栏隐藏
     */
    public void navigationBarStatusBar(boolean hasFocus){
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
