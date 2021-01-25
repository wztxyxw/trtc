package com.chuangdu.library.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcelUtils {
    public static final int EXIST_SEPARATOR = 1;
    public static final int NON_SEPARATOR = 0;

    private ParcelUtils() {
    }

    /**
     * 序列化string
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, String obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeString(obj);
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化Long
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Long obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeLong(obj.longValue());
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化int
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Integer obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeInt(obj.intValue());
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化float
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Float obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeFloat(obj.floatValue());
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化double
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Double obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeDouble(obj.doubleValue());
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化Map
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Map obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeMap(obj);
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化Date
     * @param out
     * @param obj
     */
    public static void writeToParcel(Parcel out, Date obj) {
        if (obj != null) {
            out.writeInt(1);
            out.writeLong(obj.getTime());
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 从序列化中读取float
     * @param in
     * @return
     */
    public static Float readFloatFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? Float.valueOf(in.readFloat()) : null;
    }

    /**
     * 从序列化中读取double
     * @param in
     * @return
     */
    public static Double readDoubleFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? Double.valueOf(in.readDouble()) : null;
    }

    /**
     * 从序列化中读取Date
     * @param in
     * @return
     */
    public static Date readDateFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? new Date(in.readLong()) : null;
    }

    /**
     * 从序列化中读取int
     * @param in
     * @return
     */
    public static Integer readIntFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? Integer.valueOf(in.readInt()) : null;
    }

    /**
     * 从序列化中读取long
     * @param in
     * @return
     */
    public static Long readLongFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? Long.valueOf(in.readLong()) : null;
    }

    /**
     * 从序列化中读取string
     * @param in
     * @return
     */
    public static String readStringFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? in.readString() : null;
    }

    /**
     * 从序列化中读取map
     * @param in
     * @return
     */
    public static Map readMapFromParcel(Parcel in) {
        int flag = in.readInt();
        return flag == 1 ? in.readHashMap(HashMap.class.getClassLoader()) : null;
    }

    /**
     * 读取序列化类
     * @param in
     * @param cls
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> T readFromParcel(Parcel in, Class<T> cls) {
        int flag = in.readInt();
        Parcelable t = null;
        if (flag == 1) {
            t = in.readParcelable(cls.getClassLoader());
        }
        return (T) t;
    }

    /**
     * 读取序列化类
     * @param model
     * @param out
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> void writeToParcel(Parcel out, T model) {
        if (model != null) {
            out.writeInt(1);
            out.writeParcelable(model, 0);
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 写序列化类
     * @param model
     * @param model
     * @param <T>
     * @return
     */
    public static <T extends List<?>> void writeToParcel(Parcel out, T model) {
        if (model != null) {
            out.writeInt(1);
            out.writeList(model);
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 读取序列化类
     * @param in
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> readListFromParcel(Parcel in, Class<T> cls) {
        int flag = in.readInt();
        return flag == 1 ? in.readArrayList(cls.getClassLoader()) : null;
    }

    /**
     * 序列化list
     * @param collection
     * @param out
     * @return
     */
    public static void writeListToParcel(Parcel out, List<?> collection) {
        if (collection != null) {
            out.writeInt(1);
            out.writeList(collection);
        } else {
            out.writeInt(0);
        }
    }

    /**
     * 序列化byte
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> T bytesToParcelable(byte[] data, Class<T> cls) {
        if (data != null && data.length != 0) {
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            Parcelable t = readFromParcel(in, cls);
            in.recycle();
            return (T) t;
        } else {
            return null;
        }
    }

    /**
     * 反序列化byte
     * @param model
     * @return
     */
    public static byte[] parcelableToByte(Parcelable model) {
        if (model == null) {
            return null;
        } else {
            Parcel parcel = Parcel.obtain();
            writeToParcel(parcel, model);
            return parcel.marshall();
        }
    }

    /**
     * 反序列化list
     * @param cls
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> List<T> bytesToParcelableList(byte[] data, Class<T> cls) {
        if (data != null && data.length != 0) {
            Parcel in = Parcel.obtain();
            in.unmarshall(data, 0, data.length);
            in.setDataPosition(0);
            ArrayList t = readListFromParcel(in, cls);
            in.recycle();
            return t;
        } else {
            return null;
        }
    }

    /**
     * 反序列化List<Parcelable>
     * @param list
     * @return
     */
    public static byte[] parcelableListToByte(List<? extends Parcelable> list) {
        if (list == null) {
            return null;
        } else {
            Parcel parcel = Parcel.obtain();
            writeListToParcel(parcel, list);
            return parcel.marshall();
        }
    }
}
