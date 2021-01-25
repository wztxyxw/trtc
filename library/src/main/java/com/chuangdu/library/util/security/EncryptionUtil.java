package com.chuangdu.library.util.security;

import android.text.TextUtils;

import com.chuangdu.library.util.LogUtil;

import java.security.MessageDigest;

/**
 * 加密解密工具类
 */
public class EncryptionUtil {

    private static final String DEFAULT_KEY = "123456";

    /**
     * md5 加密
     */
    static public String md5Encode(String origin) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] e = origin.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(e);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }

    }

    /**
     * 将 s 进行 BASE64 编码
     */
    static public String base64Encode(String s) {
        if (s == null) {
            return null;
        }
        return Base64.encode(s.getBytes());
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     */
    static public String base64Decode(String s) {
        byte[] b;
        String result = null;
        if (TextUtils.isEmpty(s)) {
            return result;
        }
        try {
            b = Base64.decode(s);
            result = new String(b, "utf-8");
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return result;
    }

    /**
     * 将字符串str使用key通过aes加密
     *
     * @param key
     * @param str
     * @return
     */
    static public String aesEncode(String key, String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (TextUtils.isEmpty(key)) {
            key = DEFAULT_KEY;
        }
        String encodeStr = str;
        try {
            encodeStr = AES.encrypt(key, str);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return encodeStr;
    }

    /**
     * 将字符串str使用key通过aes解密
     *
     * @param key
     * @param str
     * @return
     */
    static public String aesDecode(String key, String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (TextUtils.isEmpty(key)) {
            key = DEFAULT_KEY;
        }
        String dncodeStr = str;
        try {
            dncodeStr = AES.decrypt(key, str);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return dncodeStr;
    }

    /**
     * 使用3DES算法加密字符串
     *
     * @param str
     * @return
     */
    static public String des3Encode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String encodeStr;
        byte[] secretArr = DES3.encryptMode(str.getBytes());
        encodeStr = new String(secretArr);
        return encodeStr;
    }

    /**
     * 使用3DES算法解密字符串
     *
     * @param str
     * @return
     */
    static public String des3Decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String decodeStr;
        byte[] originalArr = DES3.decryptMode(str.getBytes());
        decodeStr = new String(originalArr);
        return decodeStr;
    }

    /**
     * 使用DES加密字符串
     *
     * @param key
     * @param str
     * @return
     */
    static public String desEncode(String key, String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String encodeStr = DES.encode(key, str);
        return encodeStr;
    }

    /**
     * 使用DES解密字符串
     *
     * @param key
     * @param str
     * @return
     */
    static public String desDecode(String key, String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String decodeStr = DES.decode(key, str);
        return decodeStr;
    }

}
