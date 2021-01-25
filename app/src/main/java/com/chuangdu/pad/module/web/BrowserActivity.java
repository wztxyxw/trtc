package com.chuangdu.pad.module.web;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.chuangdu.library.app.BaseSplashActivity;
import com.chuangdu.library.util.FileUtils;
import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.library.util.ToastUtil;
import com.chuangdu.library.widget.EditNameDialog;
import com.chuangdu.library.widget.MenuDialog;
import com.chuangdu.pad.api.UrlUtils;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.common.Constant;
import com.chuangdu.pad.common.PathUtil;
import com.chuangdu.pad.im.IMManager;
import com.chuangdu.pad.models.IdCardModel;
import com.chuangdu.pad.models.IdCardMsg;
import com.chuangdu.pad.models.JsUserModel;
import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UrlModel;
import com.chuangdu.pad.module.web.contract.BrowserContract;
import com.chuangdu.pad.module.web.contract.BrowserModule;
import com.chuangdu.pad.module.web.contract.DaggerBrowserComponent;
import com.chuangdu.pad.module.web.presenter.BrowserPresenter;
import com.chuangdu.pad.utils.AppUtils;
import com.chuangdu.pad.utils.IdCardUtil;
import com.chuangdu.pad.widget.X5WebView;
import com.chuangdu.suyangpad.BuildConfig;
import com.chuangdu.suyangpad.R;
import com.tencent.liteav.event.HangUpEvent;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.event.PutMsgEvent;
import com.tencent.qcloud.tim.uikit.utils.NetWorkUtils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;


import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

/**
 * @author sc
 */
@Route(path = PathUtil.PATH_BROWSER)
public class BrowserActivity extends BaseSplashActivity implements BrowserContract.View {
    private static final String TAG = "BrowserActivity";

    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */

    @BindView(R.id.navigation1)
    LinearLayout llNavigation1;

    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mMore;
    private Button mGo;
    private EditText mUrl;

    private static final String mHomeUrl = "https://192.168.1.7:9092/bg/home";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private URL mIntentUrl;

    IdCardUtil mIdCardUtil;

    String mSamId;

    @Inject
    SP mSP;
    @Inject
    BrowserPresenter mPresenter;

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		DaggerBrowserComponent.builder().appComponent(Application.getAppComponent()).browserModule(new BrowserModule(this)).build().inject(this);
//
//		EventBus.getDefault().register(this);
//
//		getWindow().setFormat(PixelFormat.TRANSLUCENT);
//
//		Intent intent = getIntent();
//		if (intent != null) {
//			try {
//				mIntentUrl = new URL(intent.getData().toString());
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (NullPointerException e) {
//
//			} catch (Exception e) {
//			}
//		}
//		//
//		try {
//			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
//				getWindow()
//						.setFlags(
//								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//			}
//		} catch (Exception e) {
//		}
//
//		/*
//		 * getWindow().addFlags(
//		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		 */
//		setContentView(R.layout.activity_browser);
//
//		ButterKnife.bind(this);
//
//		mViewParent = (ViewGroup) findViewById(R.id.webView1);
//
//		initBtnListenser();
//
//		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
//		mIdCardUtil = new IdCardUtil();
//		mIdCardUtil.init(this, new IdCardUtil.CallBack() {
//			@Override
//			public void success() {
//				LogUtil.e(TAG, "身份证模块加载成功");
//
//				mSamId = mIdCardUtil.samId();
//
//				LogUtil.e(TAG, "mSamId " + mSamId);
//			}
//
//			@Override
//			public void error(String msg) {
//				LogUtil.e(TAG, "身份证模块加载失败  " + msg);
//				ToastUtil.getInstance().toast(msg);
//			}
//		});
//	}

    @Override
    public int getContentView() {

        LogUtil.e(TAG, "getContentView");
        return R.layout.activity_browser;
    }


    @Override
    public void initView() {

        mHandlersss.postDelayed(runnablesss, 1000);


        DaggerBrowserComponent.builder().appComponent(Application.getAppComponent()).browserModule(new BrowserModule(this)).build().inject(this);

        EventBus.getDefault().register(this);

        mViewParent = (ViewGroup) findViewById(R.id.webView1);

        initBtnListenser();

        initBarHeight(llNavigation1);

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
        mIdCardUtil = new IdCardUtil();
        mIdCardUtil.init(this, new IdCardUtil.CallBack() {
            @Override
            public void success() {
                LogUtil.e(TAG, "身份证模块加载成功");

                mSamId = mIdCardUtil.samId();

                LogUtil.e(TAG, "mSamId " + mSamId);
            }

            @Override
            public void error(String msg) {
                LogUtil.e(TAG, "身份证模块加载失败  " + msg);
                //ToastUtil.getInstance().toast("身份证模块加载失败, 正在重启");
//				Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe((v) -> {
//					AppUtils.reboot(getApplicationContext());
//				});
            }
        });
        //setNavigationBar();


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        android.app.smdt.SmdtManager smdtManager = android.app.smdt.SmdtManager.create(getApplicationContext());
//        smdtManager.smdtSetExtrnalGpioValue(4, true);

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setSpeakerphoneOn(false);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 50, AudioManager.STREAM_VOICE_CALL);

//        audioManager.setSpeakerphoneOn(false);
//        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
//        audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.STREAM_VOICE_CALL);
    }

    Handler mHandlersss = new Handler();
    private AudioManager audioManager;

    Runnable runnablesss = new Runnable() {
        @Override
        public void run() {
            //要做的事情
            //       LogUtil.e(TAG, "jsg  ");
            try {
                android.app.smdt.SmdtManager smdtManager = android.app.smdt.SmdtManager.create(getApplicationContext());
//            int value = smdtManager.smdtGetGpioDirection(1);
                int read_value = smdtManager.smdtReadExtrnalGpioValue(4);
                if (read_value == 1) {
                    //readIo_txt.setText("IO输入为高");
                    audioManager.setSpeakerphoneOn(false);
                    // 听筒模式下设置为false
                    // 设置成听筒模式
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.STREAM_VOICE_CALL);

                    LogUtil.e(TAG, "IO输入为高");

                } else {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    audioManager.setSpeakerphoneOn(true);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.STREAM_VOICE_CALL);
                    //readIo_txt.setText("IO输入为低");
                    LogUtil.e(TAG, "IO输入为低");
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "IO异常" + e.getMessage());
            }
            mHandlersss.postDelayed(runnablesss, 1000);
        }
    };


    @Override
    public void initClick() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void initData() {

    }

    private void changGoForwardButton(WebView view) {
        if (view.canGoBack()) {
            mBack.setAlpha(enable);
        } else {
            mBack.setAlpha(disable);
        }
        if (view.canGoForward()) {
            mForward.setAlpha(enable);
        } else {
            mForward.setAlpha(disable);
        }
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setAlpha(enable);
            mHome.setEnabled(true);
        }
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }

    private void init() {

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                //super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //mUrl.setText(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    changGoForwardButton(view);
                }

                /* mWebView.showLog("test Log"); */
                if (!TextUtils.isEmpty(mUrl.getText())) {
                    mSP.putString(Constant.BROWSER_URL, mUrl.getText().toString());
                }

                llNavigation1.setVisibility(View.GONE);
                //mUrl.setText(mWebView.getUrl());


            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            // /////////////////////////////////////////////////////////
            //

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(BrowserActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "fake message: i'll download...",
                                                1000).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        //视频自动播放
        webSetting.setMediaPlaybackRequiresUserGesture(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }

        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();


        jsClient = new JavascriptClient();
        mWebView.addJavascriptInterface(jsClient, "nativeClient");

        String path = mSP.getString(Constant.BROWSER_URL);
        if (!TextUtils.isEmpty(path)) {
            mUrl.setText(path);
            mWebView.loadUrl(path);
        }

//		mUrl.setText("https://192.168.31.211/bg/home");
//		mWebView.loadUrl("https://192.168.31.211/bg/home");

        LogUtil.e(TAG, "mWebView  " + (null != mWebView.getX5WebViewExtension()));
        LogUtil.e(TAG, "wifi  " + NetWorkUtils.getCurrentNetworkType(this));
    }

    private void initBtnListenser() {
        mBack = (ImageButton) findViewById(R.id.btnBack1);
        mForward = (ImageButton) findViewById(R.id.btnForward1);
        mExit = (ImageButton) findViewById(R.id.btnExit1);
        mHome = (ImageButton) findViewById(R.id.btnHome1);
        mGo = (Button) findViewById(R.id.btnGo1);
        mUrl = (EditText) findViewById(R.id.editUrl1);
        mMore = (ImageButton) findViewById(R.id.btnMore);
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
            mBack.setAlpha(disable);
            mForward.setAlpha(disable);
            mHome.setAlpha(disable);
        }
        mHome.setEnabled(false);

        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

        mForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null && mWebView.canGoForward()) {
                    mWebView.goForward();
                }
            }
        });

        mGo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = mUrl.getText().toString();
                mWebView.loadUrl(url);
                mWebView.requestFocus();
            }
        });

        mMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(BrowserActivity.this, "not completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mGo.setVisibility(View.VISIBLE);
                    if (null == mWebView.getUrl()) {
                        return;
                    }
//					if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
//						mUrl.setText("");
//						mGo.setText("首页");
//						mGo.setTextColor(0X6F0F0F0F);
//					} else {
//						mUrl.setText(mWebView.getUrl());
//						mGo.setText("进入");
//						mGo.setTextColor(0X6F0000CD);
//					}

                    //mUrl.setText(mWebView.getUrl());
                    mGo.setText("进入");
                    mGo.setTextColor(0X6F0000CD);
                } else {
                    mGo.setVisibility(View.GONE);
//					String title = mWebView.getTitle();
//					if (title != null && title.length() > MAX_LENGTH) {
//						mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
//					} else {
//						mUrl.setText(title);
//					}

                    //mUrl.setText(mWebView.getUrl());

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        });

        mUrl.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String url = null;
                if (mUrl.getText() != null) {
                    url = mUrl.getText().toString();
                }

                if (url == null
                        || mUrl.getText().toString().equalsIgnoreCase("")) {
                    mGo.setText("请输入网址");
                    mGo.setTextColor(0X6F0F0F0F);
                } else {
                    mGo.setText("进入");
                    mGo.setTextColor(0X6F0000CD);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }
        });

        mHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null) {
                    mWebView.loadUrl(mHomeUrl);
                }
            }
        });

        mExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
            }
        });
    }

    @OnClick({R.id.show_btn, R.id.change_btn})
    public void onClick(View v) {
        if (v.getId() == R.id.show_btn) {
            if (System.currentTimeMillis() - mLastTime > SECONDS) {
                mTime = 1;
            } else {
                mTime++;
                if (mTime > 10) {
                    llNavigation1.setVisibility(View.VISIBLE);
                    mTime = 0;
                }
            }
            mLastTime = System.currentTimeMillis();
        }
        if (v.getId() == R.id.change_btn) {
            if (!BuildConfig.BUILD_TYPE.equals(Constant.RELEASE)) {
                if (System.currentTimeMillis() - mChangeLastTime < 3000) {
                    mClickCount++;

                } else {
                    mClickCount = 1;
                }
                mChangeLastTime = System.currentTimeMillis();
                if (mClickCount == 10) {
                    pickMenu();
                    mClickCount = 0;
                }
            }
        }
    }

    int mClickCount = 0;
    long mChangeLastTime;

    private void pickMenu() {

        ArrayList<String> urls = new ArrayList<>();

        ArrayList<UrlModel> models = UrlUtils.getUrlModels();

        String select = "手输";
        if (null == models || models.size() == 0) {
            return;
        }

        for (UrlModel m : models) {
            urls.add(m.name);
            if (m.url.equals(UrlUtils.API_MAIN)) {
                select = m.name;
            }
        }
        urls.add("手输");

        MenuDialog dialog = MenuDialog.newInstance(urls, select, new MenuDialog.Listener() {
            @Override
            public void onMenuItemSelected(String item) {

            }

            @Override
            public void onMenuItemSelected(int position) {
                if (position == (urls.size() - 1)) {
                    inputDialog();
                } else {
                    mSP.putString(Constant.MAIN_URL, models.get(position).url);
                    mPresenter.logout();
                    toast("重启应用");
                }
            }
        });
        dialog.setShowBottom(true);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void inputDialog() {
        EditNameDialog dialog = EditNameDialog.newInstance(this, "修改主地址", "输入主地址", "http://", "确定", "取消", new EditNameDialog.Listener() {
            @Override
            public void onDone(String result) {
                if (!TextUtils.isEmpty(result) && result.contains("http")) {
                    mSP.putString(Constant.MAIN_URL, result);
                    mPresenter.logout();
                    showToast("重启应用");
                    Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe((v) -> {
                        AppUtils.reboot(getApplicationContext());
                    });
                }
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }


    int SECONDS = 3000;
    int mTime = 0;
    long mLastTime = System.currentTimeMillis();

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    changGoForwardButton(mWebView);
                }
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null) {
            return;
        }
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null) {
            mTestHandler.removeCallbacksAndMessages(null);
        }
        if (mWebView != null) {
            mWebView.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    JavascriptClient jsClient;

    @Override
    public void uploadFileSuccess(List<UploadFileModel> models) {
        if (null != models && models.size() > 0) {
            mIdCardMsg.path = models.get(0).clientFileUrl;
            mIdCardMsg.fileUrl = models.get(0).fileUrl;

            LogUtil.e(TAG, mIdCardMsg.toString());

            jsClient.readIdCardCallBack(mIdCardMsg);
        }
    }

    @Override
    public void uploadFileFail(String msg) {
        jsClient.readIdCardFailCallBack();
    }

    @Override
    public void showLoadingView(boolean fullScreen) {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void toast(int msgId) {

    }

    private final class JavascriptClient {

        /**
         * 点击进入应用按钮
         */
        @android.webkit.JavascriptInterface
        public void doOpenNativePage(String param) {
        }

        @android.webkit.JavascriptInterface
        public void share(String key, String param) {
        }

        @android.webkit.JavascriptInterface
        public String getUserInfo() {
            JsUserModel model = new JsUserModel(mSP);
            return JSON.toJSONString(model);
        }

        @android.webkit.JavascriptInterface
        public void login(String imAccount, String imUserSig, String nickName, String headPicUrl) {
            IMManager.initIM(imAccount, imUserSig, nickName, headPicUrl, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    loginStatus(true);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    loginStatus(false);
                }
            });
        }

        @android.webkit.JavascriptInterface
        public boolean isLogin(String imAccount) {
            return IMManager.isLogin(imAccount);
        }

        @android.webkit.JavascriptInterface
        public void call(String imAccount, String nickname) {
            if (isFastClick()) {
                return;
            }
            if (IMManager.isLogin()) {

                FileUtils.saveBGBitmapAsPic(new FileUtils.SaveListener() {
                    @Override
                    public void callBack(boolean status, String path) {
                        IMManager.startVideoCall(imAccount, nickname, path);
                    }
                }, ScreenUtils.snapShotWithoutStatusBar(BrowserActivity.this));


            } else {
                ToastUtil.getInstance().toast("先登录");
            }
        }

        @android.webkit.JavascriptInterface
        public void camera() {

        }

        @android.webkit.JavascriptInterface
        public void idCard() {
            if (isFastClick()) {
                return;
            }

            startIdCard();
        }

        @android.webkit.JavascriptInterface
        public void readIdCard() {
            if (isFastClick()) {
                return;
            }
            startIdCard();
        }


        @SuppressLint("SetJavaScriptEnabled")
        public void hangUp() {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:hangUp()");
                }
            });
        }

        @SuppressLint("SetJavaScriptEnabled")
        public void readIdCardCallBack(IdCardMsg msg) {
            IdCardModel model = new IdCardModel(msg);
//			IdCardModel model = new IdCardModel();
//			model.name = msg.name;
//			model.address = msg.address;
//			model.picture_data = msg.picture_data;
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    String msg = JSON.toJSONString(model);

                    LogUtil.e(TAG, msg);

                    mWebView.loadUrl("javascript:readIdCardCallBack(" + msg + ")");
                }
            });
        }

        @SuppressLint("SetJavaScriptEnabled")
        public void readIdCardFailCallBack() {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:readIdCardFailCallBack()");
                }
            });
        }

        @SuppressLint("SetJavaScriptEnabled")
        public void loginStatus(boolean status) {
            mWebView.loadUrl("javascript:loginStatus(" + status + ")");
        }

        @SuppressLint("SetJavaScriptEnabled")
        public void putMsg(String imAccount) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    String s = "javascript:jumpMessage('" + imAccount + "')";
//					String s = "javascript:jumpMessage()";
                    LogUtil.e(TAG, "putMsg " + s);
                    mWebView.loadUrl(s);
                }
            });
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hangUpEvent(HangUpEvent event) {
        jsClient.hangUp();
    }

    private int mCount = 0;

    private void startIdCard() {
        stopIdCard();
        mCount = 0;
        readIdCard();
    }

    private void stopIdCard() {
        mHandler.removeCallbacks(mRunnable);
    }

    private void readIdCard() {

//		IdCardMsg msg = new IdCardMsg();
//		msg.name = "小二郎";
//		msg.address = "大马省大马市大马县大马村";
//		msg.sex = "男";
//		msg.path = "sdad";
//		msg.birth_day = "2";
//		msg.birth_month = "2";
//		msg.birth_year = "2";
//		msg.finger_info = "sdad";
//		msg.id_num = "320193199002021514";
//		msg.nation_str = "sdad";
//		msg.sign_office = "sdad";
//		msg.useful_e_date_day = "1";
//		msg.useful_e_date_month = "1";
//		msg.useful_e_date_year = "1";
//		msg.useful_s_date_day = "11";
//		msg.useful_s_date_month = "11";
//		msg.useful_s_date_year = "11";
//		showIdCard(msg);

//		IdCardMsg msg = mIdCardUtil.readIdCard();
//		if (null != msg) {
//			showIdCard(msg);
//		} else {
//			if (mCount == 10) {
//				jsClient.readIdCardFailCallBack();
//			} else {
//				mCount++;
//
//				mHandler.removeCallbacks(mRunnable);
//				mHandler.postDelayed(mRunnable, 1000);
//			}
//		}
        mIdCardUtil = new IdCardUtil();
        mIdCardUtil.init(this, new IdCardUtil.CallBack() {
            @Override
            public void success() {
                LogUtil.e(TAG, "身份证模块加载成功");

                mSamId = mIdCardUtil.samId();

                LogUtil.e(TAG, "mSamId " + mSamId);

                IdCardMsg msg = mIdCardUtil.readIdCard();
                if (null != msg) {
                    showIdCard(msg);
                } else {
                    if (mCount == 10) {
                        jsClient.readIdCardFailCallBack();
                    } else {
                        mCount++;

                        mHandler.removeCallbacks(mRunnable);
                        mHandler.postDelayed(mRunnable, 1000);
                    }
                }
            }

            @Override
            public void error(String msg) {
                LogUtil.e(TAG, "身份证模块加载失败  " + msg);
                //ToastUtil.getInstance().toast(msg);
                jsClient.readIdCardFailCallBack();
            }
        });
    }

    Handler mHandler = new Handler();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            readIdCard();
        }
    };

    IdCardMsg mIdCardMsg;

    private void showIdCard(IdCardMsg msg) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FileUtils.saveBitmapAsPic(new FileUtils.SaveListener() {
//                    @Override
//                    public void callBack(boolean status, String path) {
//                        msg.path = path;
//
//                        LogUtil.e(TAG, msg.toString());
//
//                        jsClient.readIdCardCallBack(msg);
//                    }
//                }, msg.picture_info);
//            }
//        }).start();

        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_circle_camera);

//		int bytes = bm.getByteCount();
//
//		ByteBuffer buf = ByteBuffer.allocate(bytes);
//		bm.copyPixelsToBuffer(buf);
//
//		byte[] byteArray = buf.array();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        msg.picture_info.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        msg.picture_data = ba;
        jsClient.readIdCardCallBack(msg);

//		mIdCardMsg = msg;
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		msg.picture_info.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//		mPresenter.uploadFile(baos.toByteArray());

    }
    ///sdcard/changdu/img/1601002184503.jpg

    private void uploadFile(String path) {
//        mPresenter.uploadFile(path);

//        Bitmap pic = getResources().ge;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_circle_camera);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        mPresenter.uploadFile(baos.toByteArray());


    }

    private void uploadFile(byte[] path) {
//        mPresenter.uploadFile(path);

//        Bitmap pic = getResources().ge;
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_circle_camera);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        mPresenter.uploadFile(path);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void putMsgEvent(PutMsgEvent event) {
        jsClient.putMsg(event.userId);
    }
}
