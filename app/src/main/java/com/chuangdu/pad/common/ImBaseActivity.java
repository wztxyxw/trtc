package com.chuangdu.pad.common;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.suyangpad.R;

import butterknife.ButterKnife;

/**
 * 登录状态的Activity都要集成该类，来完成被踢下线等监听处理。
 */
public abstract class ImBaseActivity extends AppCompatActivity {

    private static final String TAG = ImBaseActivity.class.getSimpleName();

    private ImageView mToolBarLeftImageView;
    private TextView mToolBarTitle;
    private ImageView mToolBarRightImageView;
    private TextView mToolBarRightText;
    private View mToolBarLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }


        initView();
        initClick();
        initData();

    }
    public void initBarHeight(View view){

        //4.4以下 无法沉浸 状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
        }
    }

    public View getToolBarLine() {
        return mToolBarLine;
    }

    public TextView getToolBarRightText() {
        return mToolBarRightText;
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            view.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
//        }
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
    @Override
    protected void onStart() {
        LogUtil.e(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.e(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.e(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }
}
