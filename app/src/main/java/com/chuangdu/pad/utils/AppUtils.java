package com.chuangdu.pad.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.text.TextPaint;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/4/12.
 */

public class AppUtils {

    /**
     * 复制文本到系统剪贴板
     *
     * @param mContext
     * @param msg
     */
    public static void setClipboardText(Context mContext, String msg) {

        String text = msg.replaceAll(" ", "").replaceAll("\n", "");
        if (!"".equals(text)) {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(msg);
        } else {
//            ToastUtil.showToast(mContext, mContext.getString(R.string.copy_emtry_show));
        }
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


    public static ArrayList<String> geMatchertUrl(String s) {

        Pattern p = Pattern.compile("//[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher m = p.matcher(s);
        ArrayList<String> links = new ArrayList();
        while (m.find()) {
            links.add(m.group());
        }

        return links;

    }
    public static ArrayList<String> geMatchertUrls(String s) {

        Pattern p = Pattern.compile("http://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher m = p.matcher(s);
        ArrayList<String> links = new ArrayList();
        while (m.find()) {
            links.add(m.group());
        }

        return links;

    }

    public static String unicodetoString(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }



    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置字体加粗
     *
     * @param view
     * @param bold
     */
    public static void setFakeBoldText(TextView view, boolean bold) {

        TextPaint tp = view.getPaint();
        tp.setFakeBoldText(bold);
    }

    public static String getCachePath() {
        String path = Environment.getExternalStorageDirectory() + "/checheying/cache/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }
    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/checheying/img/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }
    public static String getDownloadPath(String name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + name;
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//
//        }

        return path;
    }

    public static String getCameraPath() {
        String path = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator+"Camera"+ File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }

    public static String getQrPath(Context context) {
        String path = Environment.getExternalStorageDirectory() + "/checheying/qr/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }

        path = path + "xuandou" + getVersionCode(context) + ".jpg";
        return path;
    }

    public static String getAPKPath() {
        String path = Environment.getExternalStorageDirectory() + "/checheying/apk/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();

        }
        return path;
    }
    public static void clearDir() {
        String path = Environment.getExternalStorageDirectory() + "/checheying/img/";
        File dir = new File(path);
        if (dir.exists()) {
            deleteAlltFile(dir);
        }
    }


    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param pPath
     */
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteAlltFile(dir);
    }

    private static void deleteAlltFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
            else if (file.isDirectory()) {
                deleteAlltFile(file); // 递规的方式删除文件夹
            }
        }

    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext, String path) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context, String path) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
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
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 重启
     */
    public static void reboot(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}




