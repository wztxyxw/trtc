package com.chuangdu.pad.utils;

import android.os.Environment;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.chuangdu.library.util.ToastUtil;
import com.chuangdu.pad.common.Constant;
import com.chuangdu.pad.widget.UpdateViewUtils;
import com.maning.updatelibrary.InstallUtils;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sc
 */
public class AppUpdateUtils {

    private FragmentActivity mContext;
    private String mName;
    private static AppUpdateUtils updateUtils;

    public static AppUpdateUtils init(FragmentActivity context){

        if (null == updateUtils){
            updateUtils = new AppUpdateUtils();
        }
        updateUtils.mContext = context;

        return updateUtils;
    }

    public interface CallBack{
        void onNext();
        void onFinish();
    }
    public void update(boolean show, CallBack callBack) {

//        Api.getInstance().getApi(ApiService.class).upload()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<BaseResponse<VersionModel>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseResponse<VersionModel> rsp) {
//                        if (rsp != null && ErrorCode.isSuccess(rsp.getErrCode())) {
//
//                            VersionModel model = rsp.getData();
//                            int code = AppUtils.getVersionCode(mContext);
//                            int sCode = model.code;
//                            UrlUtils.APP_URL = model.url;
//                            if (sCode > code) {
//                                showVer(model.url, model.desc, sCode, model.forceFlag);
//                            } else {
//                                callBack.onNext();
//                                if (show) {
//                                    Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
//                                }
//                                deleteFile(model.url);
//                            }
//                        } else {
//                            callBack.onNext();
//                            if (show) {
//                                Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.getInstance().toast("系统繁忙，请稍候再试");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }

//    CommonProgressDialog pBar;

    public void showVer(String url, String msg, int code, boolean flag) {

        if (Constant.APP_TYPE == Constant.AppType.SHIPPER) {
            mName = "chuangdu_shipper_" + code + ".apk";
        } else {
            mName = "chuangdu_driver_" + code + ".apk";
        }
        final String finalUrl = url.replaceAll("http:","https:");

        mUpdateViewUtils = UpdateViewUtils.newInstance(mContext, msg, flag, new UpdateViewUtils.Listener() {
            @Override
            public void close() {

            }

            @Override
            public void update() {
                downloadPath = AppUtils.getDownloadPath(mName);
                File file = new File(downloadPath);
                if (file.exists()) {
                    if (AppUtils.getVersionCode(mContext, downloadPath) < code) {
                        if (!downing) {
                            file.delete();
                            onLoading(finalUrl);
                        } else {
                            ToastUtil.getInstance().toast("已经在下载中...");
                        }
                    } else {
                        install(downloadPath);
                    }
                } else {
                    onLoading(finalUrl);
                }
            }
        });


    }
    UpdateViewUtils mUpdateViewUtils;
    public void showVer(String url, String msg, int code, boolean flag, CallBack callBack) {

        if (Constant.APP_TYPE == Constant.AppType.SHIPPER) {
            mName = "suyang-pad-" + code + ".apk";
        } else {
            mName = "suyang-pad-" + code + ".apk";
        }
        final String finalUrl = url.replaceAll("http:","https:");

        mUpdateViewUtils = UpdateViewUtils.newInstance(mContext, msg, flag, new UpdateViewUtils.Listener() {
            @Override
            public void close() {
                if (flag) {
                    callBack.onFinish();
                } else {
                    callBack.onNext();
                }
            }

            @Override
            public void update() {
                downloadPath = AppUtils.getDownloadPath(mName);
                File file = new File(downloadPath);
                if (file.exists()) {
                    if (AppUtils.getVersionCode(mContext, downloadPath) < code) {
                        if (!downing) {
                            file.delete();
                            onLoading(finalUrl);
                        } else {
                            ToastUtil.getInstance().toast("已经在下载中...");
                        }
                    } else {
                        install(downloadPath);
                    }
                } else {
                    onLoading(finalUrl);
                }
            }
        });

    }


    public void deleteFile(String url){
        try {
            downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + mName;
            File file = new File(downloadPath);
            if (file.exists()){
                file.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onLoading(String url) {
//        pBar = new CommonProgressDialog(mContext);
//        pBar.setCanceledOnTouchOutside(false);
//        pBar.setMessage("正在下载");
//        pBar.setIndeterminate(true);
//        pBar.setCancelable(false);
//        pBar.show();
        //下载APK
        InstallUtils.with(mContext)
                //必须-下载地址
                .setApkUrl(url)
                //非必须，默认update
                .setApkPath(AppUtils.getDownloadPath(mName))
                //非必须-下载回调
                .setCallBack(new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                        downing = true;
                        //下载开始
                    }

                    @Override
                    public void onComplete(String path) {

                        downing = false;
                        mUpdateViewUtils.hintView();

                        //下载完成
                        install(path);
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        downing = true;
                        //下载中
                        mUpdateViewUtils.setProgress((int) (current * 100 / total));
//                        showDownloadNotificationUI((int) (current * 100 / total));
                    }

                    @Override
                    public void onFail(Exception e) {
                        downing = false;
                        //下载失败
                        mUpdateViewUtils.hintView();
                    }

                    @Override
                    public void cancle() {
                        downing = false;
                        //下载取消
                    }
                })
                //开始下载
                .startDownload();
    }

    private boolean downing = false;
    //    Timer timer;
    String downloadPath;
    private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
    private void install(String path) {
//        File file = new File(path);
//        if (AppUtils.getVersionCode(mContext, path) <= AppUtils.getVersionCode(mContext)) {
//            file.delete();
//            ToastUtil.getInstance().toast("更新失败");
//        } else {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
//
//            mContext.startActivity(intent);
//            mContext.finish();
//        }

        /**
         * 安装APK工具类
         * @param context       上下文
         * @param filePath      文件路径
         * @param callBack      安装界面成功调起的回调
         */
        InstallUtils.installAPK(mContext, path, new InstallUtils.InstallCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(mContext, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hintView(){
        if (null != mUpdateViewUtils) {
            mUpdateViewUtils.hintView();
        }
    }

    public boolean isShow(){
        if (null != mUpdateViewUtils) {
            return mUpdateViewUtils.isShow();
        } else {
            return false;
        }
    }
}
