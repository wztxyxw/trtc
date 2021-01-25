package com.chuangdu.pad.common;

import android.net.ParseException;
import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuangdu.pad.common.i.IBasePresenter;
import com.chuangdu.pad.common.i.IBaseView;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 功能描述：基础Presenter
 *
 * @author sc
 * @since 2018/9/22
 */
public abstract class BasePresenter implements IBasePresenter {

    protected CompositeDisposable mSubscriptions = new CompositeDisposable();
//    private AlertCommonDialog mCommonDialog;

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unsubscribe(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
    protected String errorMessage(Throwable e) {

        //HTTP错误
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            return "系统内部错误";
        } else if(e instanceof UnknownHostException || e instanceof ConnectException
                || e instanceof UnknownServiceException || e instanceof SocketTimeoutException
                || e instanceof ConnectTimeoutException || e instanceof HttpException){

            return "网络异常";
//        } else if (e instanceof RequestErrorException){
//            return e.getMessage();
        } else {
            return "网络异常";
        }
    }

//    protected void showNetworkError(Throwable e, @StringRes int defErrorMsg, IBaseView view, UserRepository userRepository) {
//        showNetworkError(e, view, userRepository);
//    }
//
//    protected void showNetworkError(Throwable e, String defErrorMsg, IBaseView view, UserRepository userRepository) {
//        if (!showNetworkError(e, view, userRepository)) {
//
//        }
//    }
//
//    protected boolean showNetworkError(Throwable throwable, IBaseView view, UserRepository userRepository) {
//        view.hideLoadingView();
//        boolean dealFinish = false;
//        FragmentActivity activity = null;
//        if (view instanceof FragmentActivity) {
//            activity = (FragmentActivity) view;
//        } else if (view instanceof Fragment) {
//            activity = ((Fragment) view).getActivity();
//        }
//        if (throwable instanceof RequestErrorException) {
//            //登录过期
//            if (((RequestErrorException) throwable).getErrCode() == ErrorCode.LOGIN_OVERTIME && userRepository != null) {
//
////                EventBus.getDefault().post(new SocketEvent(SocketEvent.WORKING_STATUS, false));
//                logout(userRepository);
//                if (activity != null) {
//                    activity.finish();
//                }
//                dealFinish = true;
//            }
//        } else {
//            if ("timeout".equals(throwable.getMessage())) {
//                view.toast(R.string.network_timeout);
//                return true;
//            }
//
//            String errorMsg = throwable.getMessage();
//            LogUtil.e(this.getClass().getSimpleName(), "net work error: " + errorMsg);
//            LogUtil.e(this.getClass().getSimpleName(), throwable.getMessage());
//            int defaultNetworkError = R.string.network_error;
//            if (TextUtils.isEmpty(errorMsg)) {
//                view.toast(defaultNetworkError);
//            } else if (throwable instanceof ConnectException) {
//                view.toast(defaultNetworkError);
//            } else if (throwable instanceof HttpException) {
//                view.toast(defaultNetworkError);
//            } else if (throwable instanceof HttpRetryException) {
//                view.toast(defaultNetworkError);
//            } else if (throwable instanceof SocketTimeoutException) {
//                view.toast(defaultNetworkError);
//            } else if (throwable instanceof UnknownHostException) {
//                view.toast(defaultNetworkError);
//            } else {
//                LogUtil.e(this.getClass().getSimpleName(), "net work other error: " + errorMsg);
//                view.toast(errorMsg);
//            }
//        }
//        return dealFinish;
//    }
//
////    /**
////     * args  content  title  confirmText
////     */
////    private void showDialog(FragmentActivity activity, UserRepository userRepository, boolean logout, String... args) {
////        if (mCommonDialog != null && mCommonDialog.getDialog() != null && mCommonDialog.getDialog().isShowing()) {
////            mCommonDialog.dismiss();
////        }
////        String title = "";
////        String content = "";
////        String confirmText = "确定";
////        if (args.length > 0) {
////            content = args[0];
////        }
////        if (args.length > 1) {
////            title = args[1];
////        }
////        if (args.length > 2) {
////            confirmText = args[2];
////        }
////        mCommonDialog = new AlertCommonDialog.Builder(activity)
////                .setTitle(title)
////                .setContent(content)
////                .setAccomplishBtnText(confirmText)
////                .setAccomplishCallBack(() -> {
////                    if (activity instanceof OrderCompleteActivity) {
////                        activity.finish();
////                    } else {
////                        if (logout && !(activity instanceof LoginActivity)) {
////                            logout(userRepository, activity);
////                            activity.finish();
////                        }
////                    }
////                })
////                .build();
////    }
//
//    private void logout(UserRepository userRepository) {
//        userRepository.logout();
//        ARouter.getInstance().build(PathUtil.PATH_LOGIN).navigation();
//    }
//
//    protected void toast(@StringRes int resId) {
//        ToastUtil.getInstance().toast(resId);
//    }
//
//    protected void toast(String msg) {
//        ToastUtil.getInstance().toast(msg);
//    }
}
