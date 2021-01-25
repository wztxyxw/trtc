//package com.chuangdu.pad.module.web;
//
//import android.annotation.SuppressLint;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.fastjson.JSON;
//import com.chuangdu.library.util.FileUtils;
//import com.chuangdu.library.util.LogUtil;
//import com.chuangdu.library.util.SP;
//import com.chuangdu.library.util.ScreenUtils;
//import com.chuangdu.library.util.ToastUtil;
//import com.chuangdu.pad.common.Application;
//import com.chuangdu.pad.common.ImBaseActivity;
//import com.chuangdu.pad.common.PathUtil;
//import com.chuangdu.pad.im.IMManager;
//import com.chuangdu.pad.models.IdCardModel;
//import com.chuangdu.pad.models.IdCardMsg;
//import com.chuangdu.pad.models.JsUserModel;
//import com.chuangdu.pad.models.UploadFileModel;
//import com.chuangdu.pad.module.web.contract.DaggerMainWebComponent;
//import com.chuangdu.pad.module.web.contract.MainWebContract;
//import com.chuangdu.pad.module.web.contract.MainWebModule;
//import com.chuangdu.pad.module.web.presenter.MainWebPresenter;
//import com.chuangdu.pad.utils.IdCardUtil;
//import com.chuangdu.suyangpad.R;
//import com.tencent.liteav.event.HangUpEvent;
//import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
//import com.tencent.qcloud.tim.uikit.event.PutMsgEvent;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * @author sc
// */
//@Route(path = PathUtil.PATH_USER_STAGIEST)
//public class MainWebActivity2 extends ImBaseActivity implements MainWebContract.View {
//    private final String TAG = "MainWebActivity";
//
//    @BindView(R.id.input)
//    EditText etInput;
//
//    @BindView(R.id.header_layout)
//    View vHeaderLayout;
//
//
//    protected ProgressBar mProgressBar;
//
//    protected WebView mWebView;
//
//    protected ImageView no_net_image;
//
//    protected View mNetworkErrorView;
//
////    private TextView tvRightText;
//
//    IdCardUtil mIdCardUtil;
//
//    String mSamId;
//
//    @Inject
//    SP mSP;
//    @Inject
//    MainWebPresenter mPresenter;
//
//    @Override
//    public int getContentView() {
//        return R.layout.activity_main_web;
//    }
//
//    @Override
//    public void initView() {
//        //initToolBar("数据统计");
//
//        EventBus.getDefault().register(this);
//
//        DaggerMainWebComponent.builder().appComponent(Application.getAppComponent()).mainWebModule(new MainWebModule(this)).build().inject(this);
//        //Application.getAppComponent().inject(this);
//
//        //initBarHeight(vHeaderLayout);
//
////        tvRightText = getToolBarRightText();
////        tvRightText.setVisibility(View.VISIBLE);
////        tvRightText.setText("关闭");
//
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_web);
//        mWebView = (WebView) findViewById(R.id.web_view);
//        no_net_image = (ImageView) findViewById(R.id.no_net_image);
//        mNetworkErrorView = findViewById(R.id.network_error);
//
//
//        WebSettings settings = mWebView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setAppCacheEnabled(true);
//
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        mWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode,
//                                        String description, String failingUrl) {
//                mNetworkErrorView.setVisibility(View.VISIBLE);
//                view.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
////                Slog.d(TAG, "onReceivedTitle=" + title);
////                 if (!TextUtils.isEmpty(title)) {
////                     setActionBarTitle(title);
////                 }
//
//                etInput.setText(view.getUrl());
//            }
//
//            @Override
//            public void onProgressChanged(WebView view, int progress) {
//                // Activities and WebViews measure progress with different scales.
//                // The progress meter will automatically disappear when we reach 100%
//                //Slog.d(LOG_TAG, "onProgressChanged=" + progress);
//
//                if (progress == 100) {
//                    mProgressBar.setVisibility(View.GONE);
//                    //String title = mWebView.getTitle();
//                    //if (!TextUtils.isEmpty(title)) {
//                    //    setActionBarTitle(title);
//                    //}
//                } else {
//                    if (mProgressBar.getVisibility() == View.GONE) {
//                        mProgressBar.setVisibility(View.VISIBLE);
//                    }
//                    mProgressBar.setProgress(progress);
//                }
//                super.onProgressChanged(view, progress);
//            }
//        });
//        //mWebView.loadUrl(DBManager.getUrlModel().statisticsUrl);
//
//        if (mWebView != null) {
//            jsClient = new JavascriptClient();
//            mWebView.addJavascriptInterface(jsClient, "nativeClient");
//        }
//
//        mIdCardUtil = new IdCardUtil();
//        mIdCardUtil.init(this, new IdCardUtil.CallBack() {
//            @Override
//            public void success() {
//                LogUtil.e(TAG, "身份证模块加载成功");
//
//                mSamId = mIdCardUtil.samId();
//
//                LogUtil.e(TAG, "mSamId " + mSamId);
//            }
//
//            @Override
//            public void error(String msg) {
//                LogUtil.e(TAG, "身份证模块加载失败  " + msg);
//                ToastUtil.getInstance().toast(msg);
//            }
//        });
//
//        mWebView.loadUrl("https://192.168.31.211/bg/home");
//
//    }
//
//    @Override
//    public void initClick() {
////        tvRightText.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                finish();
////            }
////        });
//    }
//
//    @OnClick({R.id.search_go_btn, R.id.clear_btn, R.id.read_btn})
//    public void onClick(View v){
//        if (v.getId() == R.id.search_go_btn) {
//            go();
//        }
//        if (v.getId() == R.id.clear_btn) {
//            etInput.setText("");
//        }
//        if (v.getId() == R.id.read_btn) {
//            //mIdCardUtil.readIdCard();
//            uploadFile("/sdcard/chuangdu/img/1603435862366.jpg");
//        }
//    }
//
//    @Override
//    public void initData() {
//
//    }
//
//    private void go(){
//        if (!TextUtils.isEmpty(etInput.getText())) {
//            mWebView.loadUrl(etInput.getText().toString());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }
//
//    /**
//     *
//     */
//    //@Override
//    //public void onActionBackPress() {
//    //    onBackPressed();
//    //}
//
//
//
//    @Override
//    public void onBackPressed() {
//        if (mWebView != null && mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            mWebView.loadUrl("");
//            finish();
//        }
//    }
//
//    @Override
//    public void uploadFileSuccess(List<UploadFileModel> models) {
//        if (null != models && models.size() > 0) {
//            mIdCardMsg.path = models.get(0).clientFileUrl;
//            mIdCardMsg.fileUrl = models.get(0).fileUrl;
//
//            LogUtil.e(TAG, mIdCardMsg.toString());
//
//            jsClient.readIdCardCallBack(mIdCardMsg);
//        }
//    }
//
//    @Override
//    public void uploadFileFail(String msg) {
//
//    }
//
//    @Override
//    public void showLoadingView(boolean fullScreen) {
//
//    }
//
//    @Override
//    public void hideLoadingView() {
//
//    }
//
//    @Override
//    public void toast(String msg) {
//
//    }
//
//    @Override
//    public void toast(int msgId) {
//
//    }
//
//    public static class Headers implements Serializable {
//        public Map<String, String> mHeader = new HashMap<String, String>();
//
//        public void add(String key, String value) {
//            mHeader.put(key, value);
//        }
//    }
//
//    JavascriptClient jsClient;
//
//    private final class JavascriptClient {
//
//        /**
//         * 点击进入应用按钮
//         */
//        @android.webkit.JavascriptInterface
//        public void doOpenNativePage(String param) {
//        }
//
//        @android.webkit.JavascriptInterface
//        public void share(String key, String param) {
//        }
//
//        @android.webkit.JavascriptInterface
//        public String getUserInfo() {
//            JsUserModel model = new JsUserModel(mSP);
//            return JSON.toJSONString(model);
//        }
//
//        @android.webkit.JavascriptInterface
//        public void login(String imAccount, String imUserSig, String nickName, String headPicUrl) {
//            IMManager.initIM(imAccount, imUserSig, nickName, headPicUrl, new IUIKitCallBack() {
//                @Override
//                public void onSuccess(Object data) {
//                    loginStatus(true);
//                }
//
//                @Override
//                public void onError(String module, int errCode, String errMsg) {
//                    loginStatus(false);
//                }
//            });
//        }
//        @android.webkit.JavascriptInterface
//        public boolean isLogin(String imAccount) {
//            return IMManager.isLogin(imAccount);
//        }
//
//        @android.webkit.JavascriptInterface
//        public void call(String imAccount, String nickname){
//            if (IMManager.isLogin()) {
//
//                FileUtils.saveBGBitmapAsPic(new FileUtils.SaveListener() {
//                    @Override
//                    public void callBack(boolean status, String path) {
//                        IMManager.startVideoCall(imAccount, nickname, path);
//                    }
//                }, ScreenUtils.snapShotWithoutStatusBar(MainWebActivity2.this));
//
//
//            } else {
//                ToastUtil.getInstance().toast("先登录");
//            }
//        }
//
//        @android.webkit.JavascriptInterface
//        public void camera(){
//
//        }
//
//        @android.webkit.JavascriptInterface
//        public void idCard(){
//
//            startIdCard();
//        }
//
//        @android.webkit.JavascriptInterface
//        public void readIdCard(){
//            startIdCard();
//        }
//
//
//        @SuppressLint("SetJavaScriptEnabled")
//        public void hangUp() {
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.loadUrl("javascript:hangUp()");
//                }
//            });
//        }
//
//        @SuppressLint("SetJavaScriptEnabled")
//        public void readIdCardCallBack(IdCardMsg msg) {
//            IdCardModel model = new IdCardModel(msg);
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    String msg = JSON.toJSONString(model);
//
//                    LogUtil.e(TAG, msg);
//
//                    mWebView.loadUrl("javascript:readIdCardCallBack(" + msg + ")");
//                }
//            });
//        }
//        @SuppressLint("SetJavaScriptEnabled")
//        public void readIdCardFailCallBack() {
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.loadUrl("javascript:readIdCardFailCallBack()");
//                }
//            });
//        }
//
//        @SuppressLint("SetJavaScriptEnabled")
//        public void loginStatus(boolean status) {
//            mWebView.loadUrl("javascript:loginStatus(" + status + ")");
//        }
//
//        @SuppressLint("SetJavaScriptEnabled")
//        public void putMsg(String imAccount) {
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.loadUrl("javascript:putMsg(" + imAccount + ")");
//                }
//            });
//        }
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void hangUpEvent(HangUpEvent event) {
//        jsClient.hangUp();
//    }
//
//    private int mCount = 0;
//
//    private void startIdCard() {
//        stopIdCard();
//        mCount = 0;
//        readIdCard();
//    }
//    private void stopIdCard() {
//        mHandler.removeCallbacks(mRunnable);
//    }
//    private void readIdCard(){
//        IdCardMsg msg = mIdCardUtil.readIdCard();
//        if (null != msg) {
//            showIdCard(msg);
//        } else {
//            if (mCount == 10) {
//                jsClient.readIdCardFailCallBack();
//            } else {
//                mCount++;
//
//                mHandler.removeCallbacks(mRunnable);
//                mHandler.postDelayed(mRunnable, 1000);
//            }
//        }
//    }
//
//    Handler mHandler = new Handler();
//
//    Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            readIdCard();
//        }
//    };
//
//    IdCardMsg mIdCardMsg;
//    private void showIdCard(IdCardMsg msg){
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                FileUtils.saveBitmapAsPic(new FileUtils.SaveListener() {
////                    @Override
////                    public void callBack(boolean status, String path) {
////                        msg.path = path;
////
////                        LogUtil.e(TAG, msg.toString());
////
////                        jsClient.readIdCardCallBack(msg);
////                    }
////                }, msg.picture_info);
////            }
////        }).start();
//        mIdCardMsg = msg;
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        msg.picture_info.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//        mPresenter.uploadFile(baos.toByteArray());
//
//    }
//    ///sdcard/changdu/img/1601002184503.jpg
//
//    private void uploadFile(String path){
////        mPresenter.uploadFile(path);
//
////        Bitmap pic = getResources().ge;
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_circle_camera);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//        mPresenter.uploadFile(baos.toByteArray());
//
//
//    }
//    private void uploadFile(byte[] path){
////        mPresenter.uploadFile(path);
//
////        Bitmap pic = getResources().ge;
////        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_circle_camera);
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//        mPresenter.uploadFile(path);
//
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void putMsgEvent(PutMsgEvent event) {
//        jsClient.putMsg(event.userId);
//    }
//
//
//
//}