package com.chuangdu.library.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;

import com.chuangdu.library.R;

/**
 * @author sc
 */
public class ToastUtil {

    private static String mLastToast = null;

    private ToastUtil() {
    }

    private static ToastUtil instance;

    private static Context mContext;

    public static ToastUtil getInstance() {
        if (instance == null) {
            synchronized (ToastUtil.class) {
                if (instance == null) {
                    instance = new ToastUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 在Application中调用，省去每次toast需要传的context,
     */
    public static void init(Context mContext) {
        ToastUtil.mContext = mContext;
    }

    /** Toast对象 */
    private static Toast mToast;
    private TextView mTextView;

    /**
     * 显示toast
     */
    public void toast(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        mLastToast = content;
        if (mToast == null) {
            mToast = new Toast(mContext);
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.toast_layout, null);
            mTextView = view.findViewById(R.id.toast_text);
            mToast.setView(view);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mTextView.setText(content);
        mToast.setGravity(Gravity.BOTTOM, 0, 150);
        mToast.show();
    }

    /**
     * 取消吐司
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 显示一个长吐司
     * @param content
     */
    public void toastLong(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        mLastToast = content;
        if (mToast == null) {
            mToast = new Toast(mContext);
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.toast_layout, null);
            mTextView = view.findViewById(R.id.toast_text);
            mToast.setView(view);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mTextView.setText(content);
        mToast.setGravity(Gravity.BOTTOM, 0, 150);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 显示toast
     */
    public void toast(int resId) {
        String content = mContext.getString(resId);
        toast(content);
    }

    @VisibleForTesting
    public String getLastToast() {
        return mLastToast;
    }
}
