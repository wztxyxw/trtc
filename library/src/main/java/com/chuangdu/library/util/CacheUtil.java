package com.chuangdu.library.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;

import java.io.File;
import java.util.List;

public class CacheUtil {
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        //deleteDir(context.getFilesDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            deleteDir(context.getExternalFilesDir(null));
            deleteDir(new File(getImageLoaderDirPath(context)));
            deleteDir(new File(AppUtils.getImgPath()));

        }
    }

    private static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        //Slog.d("clearAllCache dir:" + dir.getAbsolutePath());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                //Slog.d("clearAllCache done -> "+success);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean clearCacheImg() {
        File dir = new File(AppUtils.getCachePath());
        if (dir == null) {
            return false;
        }
        //Slog.d("clearAllCache dir:" + dir.getAbsolutePath());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                //Slog.d("clearAllCache done -> "+success);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public static File getExternalFileDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getExternalFilesDir(null);
        } else {
            return context.getFilesDir();
        }
    }

    public static String getImageLoaderDirPath(Context context) {
        File file = getExternalFileDir(context);
        if (file == null) {
            return "/mnt/sdcard/.imageloader/";
        } else {
            return file.getAbsolutePath() + '/'
                    + ".imageloader" + '/';
        }
    }

    /**
     * 判断当前app是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isApplicationInBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 屏幕是否亮着
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return manager.isScreenOn();
    }
}
