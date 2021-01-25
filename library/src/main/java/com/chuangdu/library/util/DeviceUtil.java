package com.chuangdu.library.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtil {

    public static String getWifiMac(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if(!TextUtils.isEmpty(wifiMac)){
            Slog.d("wifiMac : ", wifiMac);
            return wifiMac;
        }
        return null;
    }
    public static String getImei(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if(!TextUtils.isEmpty(imei)){
            Slog.d("imei : ", imei);
            return imei;
        }
        return null;
    }
    public static String getSn(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = tm.getSimSerialNumber();
        if(!TextUtils.isEmpty(sn)){
            Slog.d("sn : ", sn);
            return sn;
        }
        return null;
    }
}
