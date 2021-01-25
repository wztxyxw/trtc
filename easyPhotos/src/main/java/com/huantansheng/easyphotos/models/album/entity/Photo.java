package com.huantansheng.easyphotos.models.album.entity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.huantansheng.easyphotos.constant.Type;
import com.huantansheng.easyphotos.utils.ImageUtils;

import java.io.File;

/**
 * 图片item实体类
 *
 * @author huan
 * @date 2017/10/20
 */

public class Photo implements Parcelable {
    private static final String TAG = "Photo";

    /** //图片Uri */
    public Uri uri;
    /** //图片名称 */
    public String name;
    /** //图片全路径 */
    public String path;
    /** //图片类型 */
    public String type;
    /** //图片宽度 */
    public int width;
    /** //图片高度 */
    public int height;
    /** //图片文件大小，单位：Bytes */
    public long size;
    /** //视频时长，单位：毫秒 */
    public long duration;
    /** //图片拍摄的时间戳,单位：毫秒 */
    public long time;
    /** 是否被选中,内部使用,无需关心 */
    public boolean selected;

    /** 用户选择时是否选择了原图选项 */
    public boolean selectedOriginal;

    /** //封面图片全路径 */
    public String imagePath;

    public Photo(){

    }
    public Photo(String path){
        this.path = path;
    }
    public Photo(Uri uri, String path){
        this.uri = uri;
        this.path = path;
    }
    public Photo(String name, Uri uri, String path, long time, int width, int height, long size, long duration, String type) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.time = time;
        this.width = width;
        this.height = height;
        this.type = type;
        this.size = size;
        this.duration = duration;
        this.selected = false;
        this.selectedOriginal = false;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Photo other = (Photo) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            Log.e(TAG, "equals: " + Log.getStackTraceString(e));
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "name='" + name + '\'' +
                ", uri='" + uri.toString() + '\'' +
                ", path='" + path + '\'' +
                ", time=" + time + '\'' +
                ", minWidth=" + width + '\'' +
                ", minHeight=" + height +
                '}';
    }

    //压缩图片
    public String getCompressPath() {
        return ImageUtils.savePhotoToSDCard(getSmallBitmap(path), name);//保存缩略图到本地
    }
    //压缩图片
    public String getCompressUri() {
        return Uri.fromFile(new File(ImageUtils.savePhotoToSDCard(getSmallBitmap(path), name))).toString();//保存缩略图到本地
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 5;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static Photo getPhoto(Context context, Uri uri) {
        Photo p = null;
        String path;
        String name;
        long dateTime;
        String type;
        long size;
        int width = 0;
        int height = 0;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
            dateTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED));
            type = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
            size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT));
            p = new Photo(name, uri, path, dateTime, width, height, size, 0, type);
        }
        cursor.close();

        return p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.type);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeLong(this.size);
        dest.writeLong(this.duration);
        dest.writeLong(this.time);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.selectedOriginal ? (byte) 1 : (byte) 0);
        dest.writeString(this.imagePath);
    }

    protected Photo(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.name = in.readString();
        this.path = in.readString();
        this.type = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.size = in.readLong();
        this.duration = in.readLong();
        this.time = in.readLong();
        this.selected = in.readByte() != 0;
        this.selectedOriginal = in.readByte() != 0;
        this.imagePath = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getType() {
        if (type.contains(Type.VIDEO)) {
            return "video/mp4";
        }
        return type;
    }
}
