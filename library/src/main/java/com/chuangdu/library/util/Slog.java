package com.chuangdu.library.util;

import android.util.Log;

public final class Slog {
    public static boolean DEBUG = true;
    public static boolean SHOW_ACTIVITY_STATE = false;

    public static void setDebug(boolean debug, boolean showActivityStatus) {
        DEBUG = debug;
        SHOW_ACTIVITY_STATE = showActivityStatus;
    }

    private Slog() {
    }

    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    private static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static final void state(String packName, String state) {
        if (SHOW_ACTIVITY_STATE)
            Log.d(packName, state);
    }
}
