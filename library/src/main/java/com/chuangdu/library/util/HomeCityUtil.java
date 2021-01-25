package com.chuangdu.library.util;//package com.sc.library.util;
//
//import android.content.Context;
//
//import com.sc.library.location.LocationUtil;
//import com.sc.library.location.ScLocation;
//import com.sc.library.location.ScLocationListener;
//
///**
// * 检测当前城市的工具类
// * 定位成功以后，把结果保存到preference中
// */
//
//public class HomeCityUtil {
//    private static final String KEY_CURRENT_CITY = "current_city";
//    private LocationUtil mLocationUtil;
//    /**
//     * 定位当前城市
//     * @param context context
//     */
//    public void detect(final Context context) {
//        if (mLocationUtil != null) {
//            return;
//        }
//
//        mLocationUtil = new LocationUtil(context, new ScLocationListener() {
//            @Override
//            public void onLocationChanged(ScLocation location) {
//                if (location != null) {
//                    String addr = location.province + "&" + location.city;
//                    SP spUtils = SP.getInstance(context);
//                    spUtils.put(KEY_CURRENT_CITY, addr);
//                    Slog.d("HomeCityUtil", "get city:" + addr);
//                }
//
//                mLocationUtil.stopLocation();
//                mLocationUtil = null;
//            }
//        });
//
//        mLocationUtil.startLocation(false);
//    }
//
//    /**
//     * 获得前面定位到当前城市
//     * @param context context
//     * @return 已符号"&"分割，形如 省&市
//     */
//    public String getCity(Context context) {
//        SP sp = SP.getInstance(context);
//        return sp.get(KEY_CURRENT_CITY);
//    }
//}
