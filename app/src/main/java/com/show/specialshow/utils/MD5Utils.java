package com.show.specialshow.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String getMd5Str(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(input.getBytes("UTF-8"));
			StringBuffer buffer = new StringBuffer();
			for (byte b : result) {
				// 每个byte做与运算
				int number = b & 0xff;// 不按标准加密
				// 转换成16进制
				String numberStr = Integer.toHexString(number);
				if (numberStr.length() == 1) {
					buffer.append("0");
				}
				buffer.append(numberStr);
			}
			// 就标准的md5加密的结果
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
