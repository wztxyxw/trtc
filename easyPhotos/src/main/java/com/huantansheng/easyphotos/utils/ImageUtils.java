package com.huantansheng.easyphotos.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.chuangdu.library.util.AppUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 图片操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("ALL")
public class ImageUtils {

    /**
     * 获取存储路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 产生一个随机的字符串
     * 可用来图片命名
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        //String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     */
    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static String genProjectPath() {
        // 如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
        File rootdir = Environment.getExternalStorageDirectory();
        String filePath = rootdir.getPath() + ExtraKey.WORKPLACE_SDCARD_PATH;
        createPath(filePath);
        return filePath;
    }


    /**
     * 通过递归的方式删除file
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     */
    public static String savePhotoToSDCard(Bitmap photoBitmap, String photoName) {
        String targetPath = "";
        if (checkSDCardAvailable()) {
            File dir = new File(AppUtils.getCachePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(AppUtils.getCachePath(), photoName + ".jpg");
            targetPath = photoFile.getAbsolutePath();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return targetPath;
    }


    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
