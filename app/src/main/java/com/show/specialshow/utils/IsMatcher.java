package com.show.specialshow.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsMatcher {
	/**
	 * 判断是不是手机号码
	 * @param mobiles
	 * @return
	 */
		public static boolean isMobileNO(String mobiles) {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}
		/**
		 * 判断是不是身份证号吗 
		 */
		public static boolean isIdCard(String idCard){
			Pattern p = Pattern
					.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
			Matcher m = p.matcher(idCard);
			return m.matches();
			
		}
		/**
		 * 判断是不是银行卡号
		 */
		public static boolean isbankCard(String bankCard){
			Pattern p = Pattern
					.compile("^\\d{16}|\\d{19}$");
			Matcher m = p.matcher(bankCard);
			return m.matches();
			
		}
}
