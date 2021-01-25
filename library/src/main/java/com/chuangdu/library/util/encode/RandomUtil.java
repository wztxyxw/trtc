package com.chuangdu.library.util.encode;

import android.text.TextUtils;

import java.util.Random;

/**
 * 将关键数据进行加密
 * @author sc
 */

public class RandomUtil {

    private static final String MESSAGE_KEY = "AESCipher1234567";
    private static final String WRAP_KEY = "YYcx1289qazRan@!";

    /**
     * 加密
     * ASE(8（随机8位）+ 数据(ASE) + 8（随机8位）)
     *
     * @param value 加密前的值
     * @return
     */
    public static String randomEncrypt(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        return AESCipher.encryptAES(sb.append(randomString(8))
                .append(AESCipher.encryptAES(value, RandomUtil.MESSAGE_KEY))
                .append(randomString(8)).toString(), RandomUtil.WRAP_KEY).replaceAll("\\+","-").replaceAll("/","_");
    }

    /**
     * 解密
     *
     * @param value 解密后的值
     * @return
     */
    public static String randomDecrypt(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        if (value.length() == 11 || value.length() == 4) {
            return value;
        }
        value = AESCipher.decryptAES(value.replace(" ", "+"), WRAP_KEY);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        return AESCipher.decryptAES(value.substring(8, value.length() - 8),
                RandomUtil.MESSAGE_KEY);
    }

    /**
     * 产生一个随机的字符串
     */
    private static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            sb.append(str.charAt(num));
        }
        return sb.toString();
    }
}
