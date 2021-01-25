package com.chuangdu.library.util;

import android.util.Log;

import com.chuangdu.library.BuildConfig;


public class LogUtil {

    public static final String LOG = ">>>>mvp";

    public static boolean isShowLog = true;//true:打印log，false:不打印log

    public static void setIsShowLog(boolean isShowLog) {
        LogUtil.isShowLog = isShowLog;
    }
    public static void e(String message) {
        if (!isShowLog) {
            return;
        }
        Log.e(LOG, message);
    }

    public static void e(Throwable throwable) {
        e(throwable.getLocalizedMessage());
    }
    public static void e(String tag, String message) {
        if (!isShowLog) {
            return;
        }
        Log.e(tag, message);
    }

    public static void i(String message) {
        if (!isShowLog) {
            return;
        }
        Log.i(LOG, message);
    }

    public static void d(String tag, String message) {
        if (!isShowLog) {
            return;
        }
        Log.d(tag, message);
    }

    public static void v(String message) {
        if (!isShowLog) {
            return;
        }
        Log.v(LOG, message);
    }

}
