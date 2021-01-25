package com.chuangdu.library.util;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {


    /**
     * 不能实例化
     */
    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前线程名称
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String getImgPath() {
        String path = Environment.getExternalStorageDirectory() + "/chuangdu/img/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }

    public static String getCachePath() {
        String path = Environment.getExternalStorageDirectory() + "/chuangdu/cache/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }

    /**
     * 就用是否存在
     * @param packageName
     * @return
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param context
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
    /**
     * 复制文本到系统剪贴板
     *
     * @param mContext
     * @param msg
     */
    public static void setClipboardText(Context mContext, String msg) {

        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(msg);
    }

    /**
     * 获取系统剪贴板
     *
     * @param mContext
     * @return
     */
    public static String getClipboardText(Context mContext) {

        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        String text = "";
        if (cmb != null && cmb.getText() != null) {
            text = cmb.getText().toString().trim();
        }
        return text;

    }

    public static String getCameraImgPath() {
//        String foloder = ImageUtils.getCachePath(context) + "/sc/picture/";
        String foloder = AppUtils.getImgPath();
        File savedir = new File(foloder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 照片命名
        String picName = timeStamp + ".jpg";
        //  裁剪头像的绝对路径
        return foloder + picName;
    }

    public static String getDownloadPath(String name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + name;
        return path;
    }

    public static boolean isShow(){
        return isFileExits(getDownloadPath("sclocal.sc"));
    }

    /**
     * 判断文件是否存在， true表示存在，false表示
     * @param fileName 文件名
     * @return
     */
    public static boolean isFileExits(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getPackageName(Context context, String path) {
        String packageName = "";
        try {
            packageName = context.getPackageManager().
                    getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            packageName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

}