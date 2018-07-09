package com.oyxy.util;

import java.security.MessageDigest;

/**
 * 使用方法：MD5.MD5Encode("明文")
 */
public class MD5 {
	/**
	 * 16进制字符
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 将字节数组转换为16进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 单字节转16进制字符
	 * 
	 * @param 待转换字节
	 * @return 16进制字符
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 使用MessageDigest类的MD5摘要算法生成源信息的信息摘要
	 * 
	 * @return 生成的32位16进制字符串信息摘要
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

}
