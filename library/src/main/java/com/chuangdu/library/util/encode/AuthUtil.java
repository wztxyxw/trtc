package com.chuangdu.library.util.encode;

/**
 * @author sc
 */
public class AuthUtil {

    /**
     * 加密
     */
    public static String aesEncrypt(String value) {
        return AESCipher.encryptAES(value, "yycx2017Taxi1234");
    }

    /**
     * 解密
     */
    public static String aesDecrypt(String value) {
        return AESCipher.decryptAES(value, "yycx2017Taxi1234");
    }
}
