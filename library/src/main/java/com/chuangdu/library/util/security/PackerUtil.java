package com.chuangdu.library.util.security;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

/**
 * Apk加壳工具类
 */
public class PackerUtil {

    public static void main(String[] args) {
        System.out.println("main方法执行");
//        packer("", "");
    }

    /**
     * Android APK加壳技术方案
     * 参考
     * http://blog.csdn.net/forlong401/article/details/12060605/
     * @param apkPath
     * @param unshellDexPath
     */
    public static void packer(String apkPath, String unshellDexPath) {
        if (TextUtils.isEmpty(apkPath) || TextUtils.isEmpty(unshellDexPath)) {
            return;
        }
        try {
            //需要加壳的程序
            File payloadSrcFile = new File(apkPath);
            //解壳dex
            File unShellDexFile = new File(unshellDexPath);
            //以二进制形式读出apk，并进行加密处理
            byte[] payloadArray = encrpt(readFileBytes(payloadSrcFile));
            //以二进制形式读出dex
            byte[] unShellDexArray = readFileBytes(unShellDexFile);
            int payloadLen = payloadArray.length;
            int unShellDexLen = unShellDexArray.length;
            //多出4字节是存放长度的。
            int totalLen = payloadLen + unShellDexLen +4;
            // 申请了新的长度
            byte[] newdex = new byte[totalLen];
            //添加解壳代码
            //先拷贝dex内容
            System.arraycopy(unShellDexArray, 0, newdex, 0, unShellDexLen);
            //添加加密后的解壳数据
            //再在dex内容后面拷贝apk的内容
            System.arraycopy(payloadArray, 0, newdex, unShellDexLen, payloadLen);
            //添加解壳数据长度
            //最后4为长度
            System.arraycopy(intToByte(payloadLen), 0, newdex, totalLen-4, 4);
            //修改DEX file size文件头
            fixFileSizeHeader(newdex);
            //修改DEX SHA1 文件头
            fixSHA1Header(newdex);
            //修改DEX CheckSum文件头
            fixCheckSumHeader(newdex);


            //把内容写到g:/classes.dex
            String str = "g:/classes.dex";
            File file = new File(str);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream localFileOutputStream = new FileOutputStream(str);
            localFileOutputStream.write(newdex);
            localFileOutputStream.flush();
            localFileOutputStream.close();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** 直接返回数据，读者可以添加自己加密方法 */
    private static byte[] encrpt(byte[] srcdata){
        return srcdata;
    }

    /**
     * 修改dex头，CheckSum 校验码
     * @param dexBytes
     */
    private static void fixCheckSumHeader(byte[] dexBytes) {
        Adler32 adler = new Adler32();
        //从12到文件末尾计算校验码
        adler.update(dexBytes, 12, dexBytes.length - 12);
        long value = adler.getValue();
        int va = (int) value;
        byte[] newcs = intToByte(va);
        //高位在前，低位在前掉个个
        byte[] recs = new byte[4];
        for (int i = 0; i < 4; i++) {
            recs[i] = newcs[newcs.length - 1 - i];
            System.out.println(Integer.toHexString(newcs[i]));
        }
        //效验码赋值（8-11）
        System.arraycopy(recs, 0, dexBytes, 8, 4);
        System.out.println(Long.toHexString(value));
        System.out.println();
    }


    /**
     * int 转byte[]
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        byte[] b = new byte[4];
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) (number % 256);
            number >>= 8;
        }
        return b;
    }

    /**
     * 修改dex头 sha1值
     * @param dexBytes
     * @throws NoSuchAlgorithmException
     */
    private static void fixSHA1Header(byte[] dexBytes)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //从32为到结束计算sha--1
        md.update(dexBytes, 32, dexBytes.length - 32);
        byte[] newdt = md.digest();
        //修改sha-1值（12-31）
        System.arraycopy(newdt, 0, dexBytes, 12, 20);
        //输出sha-1值，可有可无
        String hexstr = "";
        for (int i = 0; i < newdt.length; i++) {
            hexstr += Integer.toString((newdt[i] & 0xff) + 0x100, 16)
                    .substring(1);
        }
        System.out.println(hexstr);
    }

    /**
     * 修改dex头 file_size值
     * @param dexBytes
     */
    private static void fixFileSizeHeader(byte[] dexBytes) {
        //新文件长度
        byte[] newfs = intToByte(dexBytes.length);
        System.out.println(Integer.toHexString(dexBytes.length));
        byte[] refs = new byte[4];
        //高位在前，低位在前掉个个
        for (int i = 0; i < 4; i++) {
            refs[i] = newfs[newfs.length - 1 - i];
            System.out.println(Integer.toHexString(newfs[i]));
        }
        //修改（32-35）
        System.arraycopy(refs, 0, dexBytes, 32, 4);
    }


    /**
     * 以二进制读出文件内容
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] readFileBytes(File file) throws IOException {
        byte[] arrayOfByte = new byte[1024];
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        while (true) {
            int i = fis.read(arrayOfByte);
            if (i != -1) {
                localByteArrayOutputStream.write(arrayOfByte, 0, i);
            } else {
                return localByteArrayOutputStream.toByteArray();
            }
        }
    }
}
