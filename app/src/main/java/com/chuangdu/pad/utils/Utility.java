
package com.chuangdu.pad.utils;

import android.content.Context;

import com.chuangdu.library.util.SP;

import java.io.InputStream;

/**
 * @author sc
 */
@SuppressWarnings("ALL")
public class Utility {

    public static final String TAG = "Utility";

    public static final String PREF_KEY_IS_FIRST_RUN = "is_first_run";

    /**
     * 返回App是否第一次运行
     *
     * @param context
     * @return
     */
    public static synchronized boolean isFirst(Context context, SP sp) {

        boolean isFirstRun = sp.getBoolean(AppUtils.getVerName(context), true);

        return isFirstRun;
    }

    /**
     * 返回App是否第一次运行
     *
     * @param context
     * @return
     */
    public static synchronized boolean isFirstRun(Context context, SP sp) {

        boolean isFirstRun = sp.getBoolean(AppUtils.getVerName(context), true);
        if (isFirstRun) {
            sp.putBoolean(AppUtils.getVerName(context), false);
        }
        return isFirstRun;
    }

    public static String readAssert(Context context,  String fileName){
        String jsonString="";
        String resultString="";
        try {
            InputStream inputStream=context.getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
