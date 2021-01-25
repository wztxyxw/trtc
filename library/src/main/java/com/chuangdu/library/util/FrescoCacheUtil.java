package com.chuangdu.library.util;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.text.DecimalFormat;

public class FrescoCacheUtil {
    /**
     * 显示缓存大小
     * @return
     */
    public static String showCacheSize() {
        long cacheSize = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        if (cacheSize <= 0) {
            return "0.00B";
        } else {
            float cacheSizeTemp1 = changFloatToTwoDecimal(Math.round(cacheSize / 1024));
            float cacheSizeTemp2 = changFloatToTwoDecimal(Math.round((cacheSize / 1024) / 1024));
            if (cacheSizeTemp1 < 1) {
                return cacheSize + "B";
            } else if (((cacheSizeTemp1 >= 1) && (cacheSizeTemp2 < 1))) {
                return cacheSizeTemp1 + "KB";
            } else if (cacheSizeTemp2 >= 1) {
                return cacheSizeTemp2 + "MB";
            } else {
                return "0.00B";
            }
        }
    }

    /**
     * 使Float保留两位小数
     * @param in
     * @return
     */
    private static float changFloatToTwoDecimal(float in) {
        DecimalFormat df = new DecimalFormat("0.00");
        String out = df.format(in);
        float result = Float.parseFloat(out);
        return result;
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
    }
}
