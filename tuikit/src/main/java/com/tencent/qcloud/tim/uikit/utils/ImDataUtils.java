package com.tencent.qcloud.tim.uikit.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sc
 */
public class ImDataUtils {

   /**
    * 固定保留size位小数
    */
   public static String doubleFormat(double value, int size) {
      NumberFormat nf = NumberFormat.getNumberInstance();
      nf.setMinimumFractionDigits(size);
      nf.setMaximumFractionDigits(size);
//      nf.setRoundingMode(RoundingMode.HALF_UP);
      //如果想输出的格式用逗号隔开，可以设置成true
      nf.setGroupingUsed(false);
      return nf.format(value);
   }

   /**
    * 最大保留size位小数
    */
   public static String doubleFormatMax(double value, int size) {
      NumberFormat nf = NumberFormat.getNumberInstance();
      nf.setMaximumFractionDigits(size);
//      nf.setRoundingMode(RoundingMode.HALF_UP);
      //如果想输出的格式用逗号隔开，可以设置成true
      nf.setGroupingUsed(false);
      return nf.format(value);
   }

   /**
    * 最小保留size位小数
    */
   public static String doubleFormatMin(double value, int size) {
      NumberFormat nf = NumberFormat.getNumberInstance();
      nf.setMinimumFractionDigits(size);
//      nf.setRoundingMode(RoundingMode.HALF_UP);
      //如果想输出的格式用逗号隔开，可以设置成true
      nf.setGroupingUsed(false);
      return nf.format(value);
   }

   public static void main(String[] a) {
//      System.out.println(doubleFormat(1.093, 2));
//      System.out.println(doubleFormatMin(1, 2));
//      System.out.println(doubleFormatMax(1.000, 2));

      String i = "80:AD:DD:3D:5D:57:3C:09:7F:2A:00:CA:B1:BD:DB:DB";
      i = i.replaceAll(":", "");
      System.out.println(i.toLowerCase());
   }

   /**
    * 隐藏中间4位
    *
    * @param mobile
    * @return
    */
   public static String halfCover(String mobile) {
      if (TextUtils.isEmpty(mobile)) {
         return "";
      }
      if (mobile.length() != 11) {
         return mobile;
      }
      StringBuilder sb = new StringBuilder();
      sb.append(mobile.substring(0, 3));
      sb.append("****");
      sb.append(mobile.substring(7));
      return sb.toString();
   }

   /**
    * 隐藏中间4位
    *
    * @param name
    * @return
    */
   public static String halfNameCover(String name) {
      if (TextUtils.isEmpty(name)) {
         return "";
      }
      if (name.length() == 1) {
         return name;
      }

      StringBuilder sb = new StringBuilder(name);
      String s = "*";
      for (int i = 1; i < name.length(); i++){
         sb.replace(i - 1, i, s);
      }

      return sb.toString();
   }

   /**
    * 判断字符串中是否包含中文
    * @param str
    * 待校验字符串
    * @return 是否为中文
    * @warn 不能校验是否为中文标点符号
    */
   public static boolean isContainChinese(String str) {
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher m = p.matcher(str);
      if (m.find()) {
         return true;
      }
      return false;
   }
   public static String distance(double value) {
      if (value > 1000) {
         return doubleFormatMax(value/1000, 2) + "公里";
      } else {
         return doubleFormatMax(value, 0) + "米";
      }
   }

   public static String distanceKM(double value) {
      if (value > 1) {
         return doubleFormatMax(value, 2) + "公里";
      } else {
         return doubleFormatMax(value * 1000, 0) + "米";
      }
   }
   public static int dp2px(Context context, float dipValue) {
      try {
         final float scale = context.getResources().getDisplayMetrics().density;
         return (int) (dipValue * scale + 0.5f);
      } catch (Exception e) {
         return (int) dipValue;
      }
   }

   public static int px2dp(Context context, float px) {
      try {
         final float scale = context.getResources().getDisplayMetrics().density;
         return (int) (px / scale + 0.5f);
      } catch (Exception e) {
         return (int) px;
      }
   }

   public static int getPhoneWidthPixels(Context context) {
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      DisplayMetrics var2 = new DisplayMetrics();
      if (wm != null) {
         wm.getDefaultDisplay().getMetrics(var2);
      }

      return var2.widthPixels;
   }

   public static int getPhoneHeightPixels(Context context) {
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      DisplayMetrics var2 = new DisplayMetrics();
      if (wm != null) {
         wm.getDefaultDisplay().getMetrics(var2);
      }

      return var2.heightPixels;
   }

   public static String getShowTime(int count) {
      return String.format("%02d:%02d", count / 60, count % 60);
   }
}
