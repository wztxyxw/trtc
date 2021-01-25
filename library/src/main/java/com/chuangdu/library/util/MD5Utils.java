package com.chuangdu.library.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5通用类
 * 
 * @author 浩令天下
 * @since 2017.04.15
 * @version 1.0.0_1
 * 
 */
public class MD5Utils {
	private static String key = "scwqsdacvOwqdsw";

	/**
	 * MD5方法
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public static String md5(String text){
		// 加密后的字符串
		try {
			String encodeStr = DigestUtils.md5Hex(text + key);
//			System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
			return encodeStr;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text
	 *            明文
	 * @param md5
	 *            密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String md5){
		// 根据传入的密钥进行验证
		String md5Text = md5(text);
		if (md5Text.equalsIgnoreCase(md5)) {
			System.out.println("MD5验证通过");
			return true;
		}

		return false;
	}
}
