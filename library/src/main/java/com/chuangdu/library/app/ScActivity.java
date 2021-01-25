package com.chuangdu.library.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chuangdu.library.event.DialogEvent;
import com.chuangdu.library.R;
import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.library.util.StatusBarUtil;
import com.chuangdu.library.util.ToastUtil;
import com.chuangdu.library.widget.DialogLoading;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;


/**
 *
 * @author sc
 * @date 2018/4/22
 */

public abstract class ScActivity extends AppCompatActivity {

    protected final String CLASS_NAME = this.getClass().getName();
//    protected ProgressDialog pd;

    protected DialogLoading mLoadingView;
    protected Context mContext;
    public View errorNoData;
    public View errorNoNetwork;
    public View view;
    private boolean isForegroud = false;


    private ImageView mToolBarLeftImageView;
    private TextView mToolBarTitle;
    private ImageView mToolBarRightImageView;
    private TextView mToolBarRightText;
    private View mToolBarLine;

    public ImageView getToolBarLeftImageView() {
        return mToolBarLeftImageView;
    }

    public TextView getToolBarTitle() {
        return mToolBarTitle;
    }

    public ImageView getToolBarRightImageView() {
        return mToolBarRightImageView;
    }

    public TextView getToolBarRightText() {
        return mToolBarRightText;
    }

    public View getToolBarLine() {
        return mToolBarLine;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setDarkStatusIcon(true);
        setContentView(getContentView());
        EventBus.getDefault().register(this);
        mContext = this;
        errorNoData = LayoutInflater.from(mContext).inflate(R.layout.view_error, null);
        errorNoNetwork = LayoutInflater.from(mContext).inflate(R.layout.view_error_network, null);

        StatusBarUtil.setTransparentForWindow(this);
        StatusBarUtil.StatusBarLightMode(this, StatusBarUtil.StatusBarLightMode(this));

        ButterKnife.bind(this);
        initSwipeBackFinish();
        initView();
        initClick();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
/*
        protected BGASwipeBackHelper mSwipeBackHelper;
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);*/

    }



    protected View getToolBarView() {
        return findViewById(R.id.toolbar);
    }

    public void initToolBar() {
        initToolBar(getTitle().toString());
    }

    public void initToolBarOnlyStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View toolbar = findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.height = ScreenUtils.getStatusHeight(this);
            toolbar.setLayoutParams(layoutParams);
            findViewById(R.id.toolbar).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.toolbar).setVisibility(View.GONE);
        }
    }

    public void initToolBar(String title) {
        View view = getToolBarView();
        if (view == null) {
            return;
        }
        //4.4以下 无法沉浸 状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
        }
        mToolBarLeftImageView = (ImageView) view.findViewById(R.id.toolbar_back);
        if (mToolBarLeftImageView != null) {
            mToolBarLeftImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mToolBarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        if (mToolBarTitle != null) {
            mToolBarTitle.setText(title);
        }
        mToolBarRightImageView = (ImageView) view.findViewById(R.id.toolbar_right);
        mToolBarRightText = (TextView) view.findViewById(R.id.toolbar_right_txt);
        mToolBarLine = view.findViewById(R.id.toolbar_line);
    }

    public void setActionBackVisible(boolean visible) {
        if (mToolBarLeftImageView != null) {
            mToolBarLeftImageView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
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

    public void showToastLong(String msg) {
        ToastUtil.getInstance().toastLong(msg);
    }

    public void handleToast(int code, String msg, int status, String desc) {
        showToast(msg);
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

    public void finishlater(){
        Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe((v) -> {
            finish();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hideLoadingView(DialogEvent event) {
        hideLoadingView();
    }
}
