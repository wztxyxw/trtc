package com.chuangdu.library.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;


/**
 *
 * @author Hello
 * @date 2017/11/8
 */
public class RsaUtil extends Coder {

    public static final String KEY_ALGORTHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** 公钥 */
    public static final String PUBLIC_KEY = "RSAPublicKey";
    /** 私钥 */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /** RSA密钥大小 */
    public static final int KEY_SIZE = 1024;
    /** RSA最大解密密文大小 */
    public static final int MAX_DECRYPT_BLOCK = 128;
    /** RSA最大加密明文大小 */
    public static final int MAX_ENCRYPT_BLOCK = 117;

    public static final String PUBLIC_KEY_DEFAULT = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0bdRCaU0XxwrN8XBRKzSYMDUF\n" +
            "h1tcB4VdG1eRXhyl+CdJfmQGLqi4/raAK/FgCOxqlVcTX66OugrZbX/Ajz0geupn\n" +
            "7jasdVhASyLz4OqYo21ZxWSX9lQHj/rIEqpDsfLcNSYm7dyIy6QVaSR0s3I18dG0\n" +
            "cuc1+nojzOnU7iJxLQIDAQAB";

    public static final String PRIVATE_KEY_DEFAULT = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKra+wo/pHz6/VK0\n" +
            "wgHto5jnZ+L+bUSN7yiViWP5tvtMK7KrbyaF22ADM/3nu9PoR2rcrHuOFlJFbOBX\n" +
            "JfxnEM6e1wOUKAxT83xzFv4zUTpnCQLO1SnHcH6nnug/8hDcVTjKjvfyWw0C1G/N\n" +
            "7zs2omWiSoU1GDGHOpqpIPnrEgpZAgMBAAECgYBXonBnRlOJY3eyB1MHiCuEjxUD\n" +
            "IjtLkX2dFG5fIEORy/UR9MrTOI9YROxTDJyTya1wC3ZQ/YVHd6KrwyeRug+tBLVj\n" +
            "M/nsW8JcXpoDTSmjECIptdjoiXHR2WNfQTz0hEZZLxRzNmlgZRH1W1VC/KAS/GSC\n" +
            "jiPzPamSofFU5joxMQJBANgS9G2CdH7711AbS3beRC7hb+LAcRpnWagggX6Ie5le\n" +
            "aQm2Zus1KmZlTP2axl2Q438+NTVGPWrgfiFq9X7oj60CQQDKbQc6WPolDEw3dXFS\n" +
            "8T4krf3FB+LXUKkscn5EmwiHv+oZpa8kKLwkGqzaUSIyN3muE1mJaH8s6Or2q9gO\n" +
            "nkrdAkA9hLAI2cKXQNadnRfQQRoCivbTBV1uWgyBvLeBshQWp3WaaU9GGj33J1JT\n" +
            "RhTCIPVUjf6SK2xjGIVwuEYrGsWFAkB54/MRQFjIDEKstmxZvGhmH9sKVLhUXP6x\n" +
            "g/PijdQyIe7uKKVwyZKYzvM8ttQ3ls4PcxlcM2849LvfcxSzoL+FAkBpN3h3xQen\n" +
            "mvTuzcuF4mFHl375YLcD2zRGyPGCs88OKkyzCIEwfCOfOGMmSf49TGVaSD643n+2\n" +
            "6Tyewh91R+bz";

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 取得公钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得私钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }


    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        //解密密钥
        byte[] keyBytes = decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipherDoFinal(cipher, data, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 用私钥解密
     *
     * @param data 解密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipherDoFinal(cipher, data, MAX_DECRYPT_BLOCK);
    }

    /**
     * 用公钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        //对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipherDoFinal(cipher, data, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 用公钥解密
     *
     * @param data 解密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipherDoFinal(cipher, data, MAX_DECRYPT_BLOCK);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       //加密数据
     * @param privateKey //私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));

    }


    /**
     * 分段大小
     *
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IOException
     */
    private static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize) throws IOException, BadPaddingException, IllegalBlockSizeException {
        if (segmentSize <= 0) {
            throw new RuntimeException("分段大小必须大于0");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * 公钥加密-外部调用
     *
     * @param str
     * @return
     */
    public static String encryptByPublic(String str, String publicKey) {
        try {
            return encryptBASE64(encryptByPublicKey(str.getBytes("UTF-8"), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密-外部调用
     *
     * @param key
     * @return
     */
    public static String decryptByPublic(String key, String publicKey) {
        try {
            return new String(decryptByPublicKey(decryptBASE64(key), publicKey), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥加密-外部调用
     *
     * @param str
     * @return
     */
    public static String encryptByPrivate(String str, String privateKey) {
        try {
            return encryptBASE64(encryptByPrivateKey(str.getBytes("UTF-8"), privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密-外部调用
     *
     * @param key
     * @return
     */
    public static String decryptByPrivate(String key, String privateKey) {
        try {
            return new String(decryptByPrivateKey(decryptBASE64(key), privateKey), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void test() {
        try {
//            Map<String,Object> map = initKey();
//            System.out.println(getPublicKey(map));
//            System.out.println(getPrivateKey(map));

            String pbkey = encryptByPublic("RSA的加密测试：12345678901", PUBLIC_KEY_DEFAULT);
            System.out.println(pbkey);
            pbkey = "DugAm4AriOaIgcmWfl2NMh1BYDBY88S2i5k5oAaI9HGuPq4PC3Vu/7gTln1PPWgi4iStSlMPAxY+\n" +
                    "XLM2KjyhTsbugopZwnDqe8stTKL5dU668XgBKKYptjU3yOmd9AJj8nhLdv1FSAbUKszmSGqLO8L8\n" +
                    "H+PV0NSU3CI/wxXkGN4=";
            System.out.println(decryptByPrivate(decodeURL(pbkey), PRIVATE_KEY_DEFAULT));

//            String pvkey = encryptByPrivate("测试RSA",PRIVATE_KEY_DEFAULT);
//            System.out.println(pvkey);
//            System.out.println(decryptByPublic(pvkey,PUBLIC_KEY_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

