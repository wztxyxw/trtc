package com.chuangdu.library.util;

import com.chuangdu.library.util.encode.BASE64Decoder;
import com.chuangdu.library.util.encode.BASE64Encoder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 *
 * @author Hello
 * @date 2017/11/8
 */
public class Coder {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     */
    public static String encryptBASE64(byte[] key) {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * CRC32加密
     *
     * @param data
     * @return
     */
    public static long encryptCRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    /**
     * URL编码
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String encodeURL(String url) throws Exception {
        return URLEncoder.encode(url, "UTF-8");
    }

    /**
     * URL解码
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String decodeURL(String url) throws Exception {
        return URLDecoder.decode(url, "UTF-8");
    }
}
