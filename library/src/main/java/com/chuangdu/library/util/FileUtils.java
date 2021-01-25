/*
    ShengDao Android Client, FileUtils
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.chuangdu.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

/**
 * [文件处理工具类]
 **/
@SuppressWarnings("ALL")
public class FileUtils {

    private static final String tag = FileUtils.class.getSimpleName();

    private static FileUtils instance;

    public interface SaveListener{
        public void callBack(boolean status, String path);
    }

    /**
     * 缓存路径
     **/
    private String rootPath;

    /**
     * 获取FileUtils实例，单例模式实现
     * 该方法缓存路径为SD卡
     * @return
     */
    public static FileUtils getInstance() {
        return getInstance(Environment.getExternalStorageDirectory().getPath());
    }

    /**
     * 获取FileUtils实例，单例模式实现
     * 该方法缓存路径为/data/data/cn.xxx.xxx(当前包)/files
     * @param context
     * @return
     */
    public static FileUtils getInstance(Context context) {
        return getInstance(context.getFilesDir().getPath());
    }

    /**
     * 获取长时间保存的数据的目录
     * @param context
     * @return
     */
    public static File getExternalFileDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //SD卡本包下的files文件路径
            return context.getExternalFilesDir(null);
        } else {
            // 内存本包下的files文件路径
            return context.getFilesDir();
        }
    }

    /**
     * 获取FileUtils实例，单例模式实现
     * 该方法缓存路径为设置的rootPath
     * @param rootPath
     * @return
     */
    public static FileUtils getInstance(String rootPath) {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    instance = new FileUtils(rootPath);
                }
            }
        }
        return instance;
    }

    public FileUtils(String rootPath) {
        this.rootPath = rootPath;
        if (TextUtils.isEmpty(rootPath)) {
            throw new IllegalArgumentException("FileUtils rootPath is not null.");
        }
    }

    /**
     * 获取保存到本地的资源路径
     * @param fileName 文件名
     * @return
     */
    public String getFilePath(String fileName) {
        StringBuilder path = new StringBuilder(rootPath);
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(path.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            path.append(File.separator);
            path.append(fileName);
        }
        return path.toString();
    }


    /**
     * 判断文件是否存在， true表示存在，false表示
     * @param fileName 文件名
     * @return
     */
    public boolean isFileExits(String fileName) {
        File file = new File(getFilePath(fileName));
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static void saveImage(String path, SaveListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveBitmapAsPicToCamera(listener, getBitMBitmap(path), System.currentTimeMillis()+ ".png");
            }
        }).start();
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

    /**
     * 根据路径 转bitmap
     * @param urlpath
     * @return
     */
    public static Bitmap getBitMBitmap(String urlpath) {

        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 保存图片到本地
     * @param bitmap   Bitmap对象
     * @param fileName 文件名
     * @return
     */
    public static boolean saveBitmapAsPicToCamera(SaveListener listener, Bitmap bitmap, String name) {
        if (bitmap == null) {
            return false;
        }
        OutputStream output = null;
        try {
            String path = getCameraPath() + name;
            LogUtil.e("saveBitmapAsPicToCamera path :", path);
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                if (file.createNewFile()) {
                    output = new FileOutputStream(file);
                    CompressFormat format = CompressFormat.PNG;
                    String tempFileName = name.toLowerCase(Locale.getDefault());
                    if (".jpg".endsWith(tempFileName)) {
                        format = CompressFormat.JPEG;
                    } else if (".png".endsWith(tempFileName)) {
                        format = CompressFormat.PNG;
                    }
                    bitmap.compress(format, 100, output);
                    output.flush();
                    listener.callBack(true, path);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listener.callBack(false, "");
        return false;
    }
    /**
     * 保存图片到本地
     * @param bitmap   Bitmap对象
     * @param fileName 文件名
     * @return
     */
    public static boolean saveBitmapAsPic(SaveListener listener, Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        OutputStream output = null;
        try {
            String path = getCameraImgPath();
            LogUtil.e("saveBitmapAsPicToCamera path :", path);
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                if (file.createNewFile()) {
                    output = new FileOutputStream(file);
                    CompressFormat format = CompressFormat.PNG;
                    String tempFileName = path.toLowerCase(Locale.getDefault());
                    if (".jpg".endsWith(tempFileName)) {
                        format = CompressFormat.JPEG;
                    } else if (".png".endsWith(tempFileName)) {
                        format = CompressFormat.PNG;
                    }
                    bitmap.compress(format, 100, output);
                    output.flush();
                    listener.callBack(true, path);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listener.callBack(false, "");
        return false;
    }
    /**
     * 保存图片到本地
     * @param bitmap   Bitmap对象
     * @param fileName 文件名
     * @return
     */
    public static boolean saveBGBitmapAsPic(SaveListener listener, Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        OutputStream output = null;
        try {
            String path = getBGImgPath();
            LogUtil.e("saveBitmapAsPicToCamera path :", path);
            File file = new File(path);
            if (!file.exists()) {
//                return true;
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            CompressFormat format = CompressFormat.PNG;
            String tempFileName = path.toLowerCase(Locale.getDefault());
            if (".jpg".endsWith(tempFileName)) {
                format = CompressFormat.JPEG;
            } else if (".png".endsWith(tempFileName)) {
                format = CompressFormat.PNG;
            }
            bitmap.compress(format, 100, output);
            output.flush();
            listener.callBack(true, path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        listener.callBack(false, "");
        return false;
    }
    /**
     * 保存字符串到本地
     * @param content  字符串内容
     * @param fileName 文件名
     * @return
     */
    public boolean saveStringToLocal(String content, String fileName) {
        if (!TextUtils.isEmpty(content)) {
            return saveByteToLocal(content.getBytes(), fileName);
        }
        return false;
    }

    /**
     * 保存字节到本地
     * @param bytes    字符串内容
     * @param fileName 文件名
     * @return
     */
    public boolean saveByteToLocal(byte[] bytes, String fileName) {
        FileOutputStream output = null;
        try {
            if (bytes != null) {
                File file = new File(rootPath, fileName);
                output = new FileOutputStream(file);
                output.write(bytes);
                output.flush();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 保存数据流到本地
     * @param instream 数据流
     * @param fileName 文件名
     * @return
     */
    public boolean saveInputStreamToLocalWithFileName(InputStream instream, String fileName) {
        File file = new File(rootPath, fileName);
        return saveInputStreamToLocalWithFile(instream, file);
    }

    /**
     * 保存数据流到本地
     * @param instream 数据流
     * @param outFile File类
     * @return
     */
    public static boolean saveInputStreamToLocalWithFile(InputStream instream, File outFile) {
        FileOutputStream buffer = null;
        try {
            if (instream != null) {
                buffer = new FileOutputStream(outFile);
                byte[] tmp = new byte[1024];
                int length = 0;
                while ((length = instream.read(tmp)) != -1) {
                    buffer.write(tmp, 0, length);
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
                if (buffer != null) {
                    buffer.flush();
                    buffer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 读取文件
     * @param filePath
     * @return
     */
    public String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从resource的raw中读取文件数据
     * @param context
     * @param resId
     * @return
     */
    public InputStream getRawStream(Context context, int resId) {
        try {
            return context.getResources().openRawResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从resource的asset中读取文件数据
     * @param context
     * @param fileName
     * @return
     */
    public static InputStream getAssetsStream(Context context, String fileName) {
        try {
            return context.getResources().getAssets().open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将byte[]转换成InputStream
     * @param b
     * @return
     */
    public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * InputStream转换成byte[]
     * @param instream
     * @return
     */
    public byte[] InputStream2Bytes(InputStream instream) {
        byte bytes[] = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] tmp = new byte[1024];
            int length = 0;
            while ((length = instream.read(tmp)) != -1) {
                output.write(tmp, 0, length);
            }
            bytes = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 数据流转字符串
     * @param instream
     * @return
     * @throws IOException
     */
    public String inputSteamToString(InputStream instream) {
        String result = null;
        try {
            byte bytes[] = InputStream2Bytes(instream);
            result = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
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

    public static String getBGImgPath() {
//        String foloder = ImageUtils.getCachePath(context) + "/sc/picture/";
        String foloder = AppUtils.getImgPath();
        File savedir = new File(foloder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        // 照片命名
        String picName = "bg.jpg";
        //  裁剪头像的绝对路径
        return foloder + picName;
    }
}
